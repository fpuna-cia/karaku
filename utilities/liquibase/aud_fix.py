import os
import sys
import re
import datetime
import io
import time
from lxml import etree


PK_CONVENTION = "{0}_fk"
PK_COLUMNS = "( {0}, rev )"
NS = 'http://www.liquibase.org/xml/ns/dbchangelog'

def joinfile(filename) :
	sarray = []
	with open(filename) as fd :
		for line in fd :
			sarray.append(line.rstrip('\n'))
	return ''.join(sarray)

def find_child(tag, child_name):
	for child in tag:
		print child.tag
		if child.tag == child_name:
			return child
	return None

def add_fk(tag, table_name, column_key):
	'''
	<addPrimaryKey tableName="sexo" constraintName="sexo_pkey"
			columnNames="id" />
	'''
	root = etree.Element('addPrimaryKey')
	root.set('tableName', '${liquibase.sequence.increment_by}')
	root.set('constraintName', FK_CONVENTION.format(table_name))
	root.set('columnNames', column_key)
	tag.append(root)
	return change


print '''
	Script de mantenimiento para tablas de auditoria


	Para que este script funcione, el archivo XML de entrada
	debe estar bien formado.
	
	Ante mal funcionamiento consultar con: avolpe@pol.una.py

	Este scprit comsidera lo siguiente:
	1. 	Se le pasa una carpeta, el creara en cada archivo de
		esta carpeta un nuevo primary key, y una referencia
		a la tabla rev

	'''

skip = ['init.xml', 'db-changelog.xml']

print 'Omitiendo: {0}'.format(skip)

param_number = 2

if len(sys.argv) != param_number:
	print 'Requiere {1} parametros, pasados {0}'.format(len(sys.argv), param_number)
	print 'Uso:'
	print '[1]	Carpeta'
	sys.exit(9)

folder = sys.argv[1]

if not os.path.isdir(folder):
	print '[1] debe ser una carpeta'
	sys.exit(9)

dirList = os.listdir(folder)
for change_file in dirList:
	if change_file in skip:
		continue
	change_text = joinfile(folder + change_file)
	t = etree.fromstring(change_text)
	#change_set = find_child(t, '{http://www.liquibase.org/xml/ns/dbchangelog}changeSet')
	#change_set = t.find('changeSet', namespaces={'ns':NS})
	#change_set = t.xpath('//ns:changeSet', namespaces={'ns':NS})[0]
	columns = t.xpath('//ns:column', namespaces={'ns':NS})
	for column in columns:
		#print column.get('name')
		#print column.get('name') == 'rev'
		if column.get('name') == 'id' or column.get('name') == 'rev':
			
			for child in column:
				#print child.tag
				if child.tag == '{http://www.liquibase.org/xml/ns/dbchangelog}constraints':
					child.set('primaryKey', "true")
	#print etree.tostring(t)
	file_name = "{0}{1}".format(folder, change_file)
	print "Eliminando {0}".format(file_name)
	os.remove(file_name)
	to_write = file(file_name, 'w')
	#to_write.write("<?xml version='1.0' encoding='UTF-8'?>")
	to_write.write(etree.tostring(t, pretty_print=True, xml_declaration=True))
	#print etree.tostring(change_set[0])
	#create_table = change_set.xpath('//ns:createTable', namespaces={'ns':NS})[0]
	#table_name = create_table.get('tableName')
	#add_fk(change_set, table_name, 'id')
	#print len(change_sets)
	#create_table = change_set.find('createTable')
	#print create_table.get('tableName')
	#for child in change_set:
	#	print child.tag
	print "-------"
	#sys.exit(9)
