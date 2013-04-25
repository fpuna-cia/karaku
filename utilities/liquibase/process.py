#!/usr/bin/python

import sys
import re
import datetime
import time
from lxml import etree


class Change():
	INDEX = 0
	def __init__(self, table='TABLENAME', action='ACTION'):

		self.time = datetime.datetime.now().strftime('%Y%m%d')# + '.{0}'.format(millis)
		self.table = table 
		self.action = action
		self.changes = []

	def __str__(self):

		a_ret = '{0}-{1}-{2}'.format(self.time, self.table.upper(), self.action.upper())
		if hasattr(self, 'params'):
			return '{0}{1}'.format(a_ret, self.params.upper())
		else:
			return a_ret

	def to_file(self):
		print '<include file=\'{0}{1}.xml\'/>'.format(prefix, self.__str__())
		target = file('{0}{1}.xml'.format(target_folder, self.__str__()), 'w')
		target.write(XML_PREFIX)
		root = etree.Element('changeSet')
		root.set('author', author)
		root.set('id', id_string.replace('##', '{0}'.format(int(id_start) + Change.INDEX)))
		Change.INDEX += 1
		for change in self.changes:
			root.append(change)
		
		target.write(etree.tostring(root, pretty_print=True))
		target.write(XML_SUFFIX)

def configure_change(change):
	#Agregamos la secuencia a todos los cambios
	if add_seq:
		add_create_sequence(change)

def fix_tag(tag):

	if tag.tag == 'createTable':
		return fix_create_table(tag)


def fix_create_table(create_table):
	for child in create_table:
		if child.tag == 'column':
			default_value_numeric = child.get('defaultValueNumeric')
			if default_value_numeric:
				#agregamos el parentesis que falta
				default_value_numeric += ')'
				child.set('defaultValueNumeric', default_value_numeric)
	return create_table

def get_secuence_name(change):
	if change.table:
		return '{0}_id_seq'.format(change.table.lower())

def add_create_sequence(change):
	"""
	<createSequence incrementBy="${liquibase.sequence.increment_by}"
			maxValue="${liquibase.sequence.max_value}" minValue="${liquibase.sequence.min_value}"
			schemaName="${liquibase.audit.schema}" sequenceName="AUDIT_TRAIL_ID_SEQ"
			startValue="${liquibase.sequence.start_value}" />
	"""
	root = etree.Element('createSequence')
	root.set('incrementBy', '${liquibase.sequence.increment_by}')
	root.set('maxValue', '${liquibase.sequence.max_value}')
	root.set('minValue', '${liquibase.sequence.min_value}')
	root.set('schemaName', 'public')
	root.set('sequenceName', get_secuence_name(change))
	root.set('startValue', '${liquibase.sequence.start_value}')
	change.changes.insert(0, root)
	return change

def joinfile(filename) :
	sarray = []
	with open(filename) as fd :
		for line in fd :
			sarray.append(line.rstrip('\n'))
	return ''.join(sarray)

def get_table_name(tag):
	table = tag.get('tableName')
	if table:
		return table
	if tag.tag == 'addForeignKeyConstraint':
		return tag.get('baseTableName')

print '''
	Para que este script funcione, el archivo XML de entrada
	debe estar bien formado.
	
	Ante mal funcionamiento consultar con: avolpe@pol.una.py

	Este scprit comsidera lo siguiente:
	1. 	Un ChangeSet tiene uno, y solo un cambio. Para agrupar
		varios cambios cree varios changeset de la misma tabla.

	Este script no sirve para generar cambios muy grandes, solo
	es un punto de partida.
	'''

XML_PREFIX = '''<?xml version='1.0' encoding='UTF-8'?>
<databaseChangeLog xmlns='http://www.liquibase.org/xml/ns/dbchangelog'
	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
	xsi:schemaLocation='http://www.liquibase.org/xml/ns/dbchangelog
	 http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd'>
			'''

XML_SUFFIX = '''
</databaseChangeLog>
	'''

changes = {}

param_number = 8

if len(sys.argv) != param_number:
	print 'Requere {1} parametros, pasados {0}'.format(len(sys.argv), param_number)
	print 'Uso:'
	print '[1]	Archivo a partir'
	print '[2]	Carpeta destino'
	print '[3]	Prefijo'
	print '[4]	Autor'
	print '[5]	Id inicial (## se reemplaza por un numero)'
	print '[6]	Numero inicial'
	print '[7]	Agregar secuencia (0 = NO)'
	sys.exit(9)

source = joinfile(sys.argv[1])
target_folder = sys.argv[2]
prefix = sys.argv[3]
author = sys.argv[4]
id_string = sys.argv[5]
id_start = sys.argv[6]
val = 0
add_seq = sys.argv[7] != '0'


if not target_folder.endswith('/'):
	target_folder += '/'
if not prefix.endswith('/'):
	prefix += '/'

changes_xml = source.split('</changeSet>')

for change_xml in changes_xml:

	'''
	<changeSet author='avolpe@pol.una.py' id='1'>
		<createTable tableName='zona'>
			<column name='id' type='BIGINT' defaultValueNumeric='nextval('zona_id_seq'::regclass)'>
				<constraints nullable='false'/>
			</column>
			<column name='descripcion' type='VARCHAR(15)'/>
		</createTable>
	</changeSet>
	'''
	if not change_xml:
		continue
	t = etree.fromstring(change_xml + '</changeSet>')
	tag = t[0]
	table = get_table_name(tag)
	
	if table in changes:
		change = changes[table]
		
	else:
		change = Change(table, tag.tag)
		change.action = tag.tag.upper()
		changes[table] = change
	fix_tag(tag)
	change.changes.append(tag)

for table, change in changes.iteritems():
	configure_change(change)
	change.to_file()