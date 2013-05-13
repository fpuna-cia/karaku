import os
import argparse
import re
import json
import shutil as sh
import sys

#sys.path.append("/home/arturo/develop/facultad/workspace/metapersona/base/utilities/liquibase")
sys.path.append(sys.path[0] + "/../liquibase")
from conventions import liquibase_file_name_ex, liquibase_file_name_re
from utils import log, enable_log
from lxml import etree
xml_parser = etree.XMLParser(remove_blank_text=True)

NS = 'http://www.liquibase.org/xml/ns/dbchangelog'
#Matchea DATE-TABLA-ACCION-PARAMETROS, solo TABLA IMPORTA
#database_file_name = "(\d+)-(\w+)-(\w+)-?(\w*)"
#regex_table_name = re.compile(database_file_name)
program_description = 	'''
	Script de sincronizacion automatica para tablas compartidas

	Tener en cuenta:
	1	La estructura debe estar creada antes de correr este script
	'''
program_epilog = '''
	Karaku Framework(C) 2013.
	'''

program_resume = '''
	Realizando sincronizacion para sistema: {0}.
	Situado en la carpeta: {1},
	Detectados {2} sistemas
	'''

system_resume = '''
Procesando sistema:	{0}
Ubicacion:		{1}
Paquete:		{2}
Numero de tablas:	{3}
	'''

excludes = [ 'seed']

def joinfile(filename) :
	sarray = []
	with open(filename) as fd :
		for line in fd :
			sarray.append(line.rstrip('\n'))
	return ''.join(sarray)

def config_path(path):
	#print path
	if not path.startswith('/'):
		if path.startswith('.'):
		#print "ENTREE"
			path = os.getcwd() + "/" + path
		else:
			path = os.getcwd() + path
	if path.endswith('/'):
		return path
	else:
		return path + '/'


def get_table_name(table) :
	table = table.replace('-', '_')
	if (table.endswith("_AUD")):
		return table[:-4]
	else:
		return table

def is_part_of_system(system_tables, table_name):
	name = get_table_name(table_name).lower()
	#print name
	#print "....."
	for table in system_tables:
		#print table["name"].lower()
		
		if table["name"].lower() == name:
			return True
	#print "....."
	return False

def apply_changes(source, target):
	log('Moviendo de: {0} a {1}'.format(source, target))
	if args.s:
		pass#log('Movido de: {0} a {1}'.format(source, target))
	else:
		sh.copyfile(source, target)
	#print target


def process_change_log(source, target, system_master, system_slave, master_tables):
	#print system_slave
	#print system_master
	#print source
	#source_xml = joinfile(source)
	target_f = file(target, "w")
	tree = etree.parse(source, xml_parser)
	root = get_root_changelog()
	includes = tree.xpath('//ns:include', namespaces={'ns':NS})
	for include in includes :
		filename = include.get('file')
		continuar = True
		for exclude in excludes:
			if exclude in filename:
				continuar = False
				break
		if not continuar:
			continue

		#print filename
		#print etree.tostring(include, pretty_print=True)
		#si es un db-changelog, y ademas es un include de un archivo del sistema
		if "db-changelog.xml" in filename:
			#print "-	" + filename
			if system_slave.lower() in filename.lower():
				root.append(include)
			continue
		#si es un include normalito
		tablename = liquibase_file_name_re.search(filename).group(4)
		
		if is_part_of_system(master_tables, tablename):
			root.append(include)
	to_print = etree.tostring(root,
			pretty_print=True,
			xml_declaration=True,
			encoding='iso-8859-1')

	if args.v :
		log("Escribiendo db-changelog filtrado: {0}".format(target))
		if not args.s:
			target_f.write(to_print)
		else:
			log(to_print, level='warning')
			
def get_root_changelog():
	xml = '''<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd">
	</databaseChangeLog>'''
	#root = etree.Element('databaseChangeLog')
	#root.nsmap('xmlns', 'http://www.liquibase.org/xml/ns/dbchangelog')
	#root.nsmap('xmlns:xsi', 'http://www.w3.org/2001/XMLSchema-instance')
	#root.set('xsi:schemaLocation=', 'http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-2.0.xsd)')

	root = etree.fromstring(xml, xml_parser)
	return root


def process_database(system_name, system_path):
	db_replication_target = args.database.replace('###', system_name.lower())
	db_replication_source = os.path.join(config_path(system_path), args.database.replace('###', system_name.lower()))
	log("-------------------")
	log("Target: " + db_replication_target)
	log("Source: " + db_replication_source)
	log("-------------------")

	for root, dirs, files in os.walk(db_replication_source):

		for fil in files:

			source = os.path.join(root, fil)
			target = root.replace(system_path + "/", "")
			target = os.path.join(target, fil)
			continuar = True
			for exclude in excludes:
				if exclude in source:
					continuar = False
					break
			if not continuar:
				continue
			#print fil
			if fil == "db-changelog.xml":
				process_change_log(source, target, data["system"], system_name, system_tables)
				continue
				
			#match = re.match(database_file_name, fil, re.IGNORECASE)
			#print fil
			#print liquibase_file_name_ex
			match = liquibase_file_name_re.match(fil)
			#print match
			if not match:
				continue
			table = get_table_name(match.group(4))

			#print table
			if is_part_of_system(system_tables, table):
				log("Tabla a sincronizar encontrada: {0}".format(table))
				apply_changes(source, target)


#http://stackoverflow.com/questions/567879/how-can-i-process-command-line-arguments-in-python
parser = argparse.ArgumentParser(description=program_description, epilog=program_epilog)
parser.add_argument('--source-folder', dest='source', help='Carpeta de origen del sistema')
parser.add_argument('--config-file', dest='config_file', help='Archivo que contiene el json de configuracion, por defecto sync.json', default='sync.json',
    type=file)
parser.add_argument('--database-folder', dest='database', help='''
	Ubicacion de la carpeta donde se encuentra la base de datos, 
	### se reemplaza por el sistem.
	Ejemplo: ./###/src/main/database''', default='./###/src/main/database')
parser.add_argument('-s', '--simulation', dest='s', help='Simula todos los cambios',
	default=False, action='store_const', const=True)
parser.add_argument('-v', '--verbose', dest='v', help='Imprime todas las operaciones', default=None, action='store_const', const=True)

current_dir = os.getcwd()

args = parser.parse_args()

if args.v:
	enable_log(True)
else:
	enable_log(False)

print program_description
try:
	data = json.load(args.config_file)
except BaseException, e:
	print "Imposible abrir el archivo '{0}', por favor asegurese de que el path sea correcto".format(args.config_file)
	exit()
args.config_file.close()

if args.source is None:
	source = data["target_folder"]
else:
	source = args.source

source = config_path(source)

print program_resume.format(data["system"], source, len(data["replication"]))

db_target = args.database.replace('###', data["system"].lower())

for system in data["replication"]:
	system_name = system["system"]
	system_path = system["path"]
	system_package = system["base_package"]
	system_tables = system["tables"]
	print system_resume.format(system_name, system_path, system_package, len(system_tables))

	#Sincronizamos todos logs de bases de datos
	process_database(system_name, system_path)

	#Sincronizamos todos los archivos .java
	#process_java(system_name, system_path)

	#Sincronizamos todos los archivos .xhtml
	#process_xhtml(system_name, system_path)



print program_epilog