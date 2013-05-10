import os
import argparse
import re
import json
import datetime
from lxml import etree

program_description = 	'''
	Script de particionamiento de changelogs generados por liquibase
	'''
program_epilog = '''

	Arturo Volpe (avolpe@pol.una.py)
	Karaku Framework(C) 2013.
	'''

NS = 'http://www.liquibase.org/xml/ns/dbchangelog'

#http://stackoverflow.com/questions/567879/how-can-i-process-command-line-arguments-in-python
parser = argparse.ArgumentParser(description=program_description, epilog=program_epilog, formatter_class=argparse.RawTextHelpFormatter)
parser.add_argument('--author', dest='author', help='Autor de los cambios', default='avolpe@pol.una.py')
parser.add_argument('--file-name', dest='file_name', help='''Nombre de los archivos.
- DDDD se cambia por la fecha
- TTTT se cambia por la tabla
default: DDDD_TTTT_ID''', default='DDDD_TTTT_ID')
parser.add_argument('--id', dest='id', help='''Identificador de cada changeset, 
TTTT por la tabla.
CCCC por la columna
default: conf_public_TTTT_id_##''', default='conf_public_TTTT_id_01')
parser.add_argument('-f', '--file-source', dest='source', type=file, 
	help='Archivo a partir', default='db-changelog.xml')
parser.add_argument('-e', '--encoding', dest='e', 
	help='Codificacion para escribir', default='iso-8859-1')
parser.add_argument('-v', '--verbose', dest='v', 
	help='Imprime todas las operaciones', default=None, action='store_const', const=True)
parser.add_argument('-p', '--prefix', dest='prefix', 
	help='Prefijo de para los include', default='')
args = parser.parse_args()

def get_root_changelog():
	xml = '''<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">


	</databaseChangeLog>'''

	root = etree.fromstring(xml)
	return root

def get_table_name(change_set):
	"""
	Se puede utilizar xpath, o expresiones regulares para 
	obtener el nombre de la tabla.
	"""
	
	for children in change_set.iter():
		if children.get('tableName'):
			return children.get('tableName')
	return None

ide = 0
def get_id():
	global ide
	ide += 1
	return '%03d' % ide 


def get_string_id(table_name):
	global args
	return (args.id.replace('TTTT', table_name))
time = datetime.datetime.now().strftime('%Y%m%d')
def save_change_set(change_set, table_name):
	global time
	file_name= (args.file_name
		.replace('DDDD', time)
		.replace('TTTT', table_name)
		.upper())
	file_name += '.xml'

	to_write = file(file_name, 'w')
	root = get_root_changelog();
	root.append(change_set)
	to_write.write(etree.tostring(root,
		pretty_print=True,
		xml_declaration=True,
		encoding='iso-8859-1'))
	print '<include file=\'{0}{1}\'/>'.format(args.prefix, file_name)
	
	

root = etree.parse(args.source)
change_sets = root.xpath('//ns:changeSet', namespaces={'ns':NS})
for change_set in change_sets:
	change_set.set('author', args.author)
	table_name = get_table_name(change_set)
	change_set.set('id', get_string_id(table_name))
	save_change_set(change_set, table_name)
	#print change_set.get('author')
	#print change_set.get('id')
	#print etree.tostring(change_set)
	#exit()

#print etree.tostring(root)

#print root
