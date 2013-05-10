import os
import argparse
import re
import json
import datetime
from lxml import etree
from utils import BColors, log, enable_log, from_classpath_to_filesystem
import utils
from conventions import liquibase_file_name_re, liquibase_file_name
import conventions
from utils import DateUtil
from conventions import get_conv_key, get_conv
import datetime
import shutil as sh

program_description = 	'''
	Script que se encarga de arreglar las convenciones utilizadas 
	en las creacion de archivos y seteo de ID liquibase.

	Funcionamiento:
		- Abre el db-changelog principal
		- Abre todos sus inclues, recursivamente
		- Por cada changelog que no sea de inclues arregla el nombre
		- Por cada changeset arregla el id
		- Omite las fechas, se suponen que siempre estan bien
		- Sincroniza todos los archivos.
	'''
program_epilog = '''

	Arturo Volpe (avolpe@pol.una.py)
	Karaku Framework(C) 2013.
	'''

NS = 'http://www.liquibase.org/xml/ns/dbchangelog'
NSE =('{{http://www.liquibase.org/xml/ns/dbchangelog}}') + '{0}'
NSE_re = re.compile("{http://www.liquibase.org/xml/ns/dbchangelog}(\w+)")
xml_parser = etree.XMLParser(remove_blank_text=True)
parser = argparse.ArgumentParser(description=program_description, 
	epilog=program_epilog, 
	formatter_class=argparse.RawTextHelpFormatter)
parser.add_argument('-f','-source-file', 
	dest='file', help='Ubicacion del archivo de cambios principal', 
	default='./configuracion/src/main/database/db-changelog.xml')
parser.add_argument('-v', '--verbose', dest='v', help='Imprime todas las operaciones', 
	default=None, action='store_const', const=True)
parser.add_argument('-c', '--no-color', dest='c', help='Imprime sin colores',
	default=True, action='store_const', const=False)
parser.add_argument('-x', '--fix', dest='x', help='Arregla los errores',
	default=True, action='store_const', const=False)
parser.add_argument('-s', '--simulation', dest='s', help='Simula todos los cambios',
	default=False, action='store_const', const=True)

sequence_numbers = {}


args = parser.parse_args()
enable_log(args.v, args.c)


log(program_description, 'ALL')

if args.s:
	log("Simulando", 'ALL')

class LiquibaseFile:
	year = None
	month = None
	day = None
	table = None
	schema = None
	action = None
	params = None
	system = None
	def __init__(self, path, root=None, file_name=None):
		"""
		Construye un objeto con una ruta y su respectivo xml cargado
		"""
		self.path = path
		self.root = root
		self.name = file_name
	def auto_fix(self):
		if not self.year:
			self.year = DateUtil.get_year()
		if not self.month:
			self.month = DateUtil.get_month()
		if not self.day:
			self.day = DateUtil.get_day()

	def full_fix(self, full_path=None):
		if not full_path:
			full_path = self.path

		#print full_path
		#print conventions.liquibase_file_location_filesystem_ex
		result = conventions.liquibase_file_location_filesystem_re.match(full_path)
		self.system = result.group(1)
		self.schema = result.group(2)
		if self.system == self.schema or self.schema == 'seed':
			self.schema = 'public'
		file_name = result.group(3)
		result = conventions.liquibase_file_name_re.match(file_name)
		self.year = result.group(1)
		self.month = result.group(2)
		self.day = result.group(3)
		self.table = result.group(4)
		self.action = result.group(5)
		try :
			self.params = result.group(6)
		except:
			pass

	def get_date(self):
		return '{0}{1}{2}'.format(self.year, self.month, self.day)

	def __str__(self):
		return liquibase_file_name.format(year=self.year, month=self.month,
			day=self.day, table=self.table, action=self.action)



def fix_change_log(file_path, nivel=0):
	log("Processing changelog: {0}".format(file_path), nivel=nivel)
	root = etree.parse(file_path, xml_parser)

	#Bandera, si se fixea algo imprimir
	fixed = False
	for element in root.iter():
		#log(etree.tostring(element, pretty_print=True))
		if element.tag == NSE.format('include'):
			log('Found classpath: {0}, finding file...'.format(element.get('file')), nivel=nivel)
			found = get_real_path(element.get('file'))
			if 'db-changelog.xml' in element.get('file'):
				log('Found changelog file: {0}, processing'.format(found), nivel=nivel)
				fixed = fix_change_log(found, nivel + 1)
				if fixed == True:
					fixed = True


			else:
				log('Found file: {0}, verifying'.format(found), nivel=nivel)
				fixed = verify(found, element, nivel + 1)
				if fixed == True:
					fixed = True
				#element.set('file', '')

	if fix() and fixed:
		save_change_log(file_path, root)
			#exit()
	return fixed

			#exit()


	#print root
def save_change_log(file_path, root):
	
	content = etree.tostring(root,
		pretty_print=True,
		xml_declaration=True,
		encoding='iso-8859-1')
	if args.s:
		if 'insert' in content:
			log('Archivo de insert', 'warning', active=True)
			return
		log(content, 'warning', active=True)
	else:
		to_write = file(file_path, 'w')
		to_write.write(content)
		to_write.close()

def replace(old_file, new_file):
	
	if args.s:
		log('Moviendo de: {0} a {1}'.format(old_file, new_file), 'warning', active=True)
	else:
		log('Moviendo de: {0} a {1}'.format(old_file, new_file))
		sh.move(old_file, new_file)
		



def get_real_path(classpath_path):
	"""
	Recibe un path en formato:
		database/SISTEMA/ESQUEMA/ARCHIVO
	Y retorna en forma:
		SISTEMA/src/main/database/ESQUEMA/ARCHIVO
	"""
	return from_classpath_to_filesystem(classpath_path)


def verify(fil, element, log_level):
	log('Verifing file: {0}'.format(fil), nivel=log_level)
	base = os.path.basename(fil)
	root = etree.parse(fil, xml_parser)
	lb_file = LiquibaseFile(fil, root, os.path.splitext(base)[0])
	
	#obtenemos el nuevo nombre
	new_name = check_file_name(lb_file, log_level + 1)
	if new_name and fix():
		old_name = lb_file.name
		lb_file.name = new_name
		new_full_path = fil.replace(old_name, new_name)
		#movemos el archivo
		replace(fil, new_full_path)
		#modificamos el element
		#obtenemos el nombre classpath:
		element.set('file', utils.from_filesystem_to_classpath(new_full_path))
		
		lb_file.path=new_full_path

	else:
		#raise Exception('Archivo bien echo encontrado, programar autoconfiguracion')
		pass


	#Bandera apra saber si se realizan cambis
	fixed = False
	
	#verificamos los ids
	#print etree.tostring(root)
	change_sets = root.xpath('//ns:changeSet', namespaces={'ns':NS})
	#print change_sets
	for change_set in change_sets:
		#print change_set.get('id')
		#print change_set.get('author')
		lb_file.full_fix()
		#print lb_file.system
		new_id = get_id(lb_file)
		#print new_id
		nex_val = 1
		if new_id in sequence_numbers:
			nex_val = sequence_numbers[new_id] + 1
		sequence_numbers[new_id] = nex_val

		new_id = new_id.replace('##', '%03d' % nex_val)
		
		if new_id != change_set.get('id'):
			#print new_id
			#print change_set.get('id')
			#print new_id == change_set.get('id')
			log('ID incorrecto, actual={0}, correcto={1}'.format(
				change_set.get('id'), new_id), 'error')
			if fix():
				change_set.set('id', new_id)
				fixed=True
	if fix() and fixed:
		save_change_log(fil, root)

	return fixed


def get_id(lb_file):
	return conventions.liquibase_id_name.format(
		system=conventions.sys[lb_file.system],
		schema=lb_file.schema,
		table=lb_file.table,
		action=lb_file.action).lower()

def fix():
	return args.x

def fix_file_name(lb_file, log_level):
	log('Fixing file name', nivel=log_level)
	new_file_name = get_file_name(lb_file)
	log('Name fixed:{0}'.format(new_file_name), nivel=log_level)
	return new_file_name


def check_file_name(lb_file, log_level):
	log('Verifing file_name', nivel=log_level)
	group = liquibase_file_name_re.match(lb_file.name)
	if not group:
		log("File: {0}, wrong name".format(lb_file.name), 'error', log_level)
		if fix():
			return fix_file_name(lb_file, log_level + 1)
		return None
	try:
		lb_file.year = group[1]
		lb_file.month = group[2]
		lb_file.day = group[3]
		lb_table.table = group[4]
		lb_table.action = group[5]
		lb_table.params = group[6]
	except:
		pass
	#print lb_file.name
	#Comparacion sin fecha, la fecha se supone que siempre esta bien
	if lb_file.name[8:] == get_real_name(lb_file)[8:]:
		log('Not problemns found', log_level)
	else:
		log("File: {0}, wrong name".format(lb_file.name), 'error', log_level)
		if fix():
			return fix_file_name(lb_file, log_level + 1)
		return None
	return None

def get_table_name(root):
	for child in root.iter():
		if child.get('tableName'):
			return utils.convert_table_name(child.get('tableName'))
		if child != root:
			val = get_table_name(child)
			if val:
				return val
	return None


best = None
def get_action(root):
	global best
	best = None
	"""
	The action is the most important operation in 
	a changeLog.
	If a insertData is found, return insertData
	If a createTable is found, return createTable

	"""
	for child in root.iter():
		if 'createTable' in child.tag:
			return get_conv_key('createTable')
		if 'insert' in child.tag:
			return get_conv_key('insert')
		#print child.tag
		best = get_conv_key(NSE_re.match(child.tag).group(1))
		#print best;
		if root != child:
			val = get_action(child)
			if val != best:
				return val

	return best

def get_file_name(lb_file):
	lb_file.auto_fix()
	if not lb_file.table :
		lb_file.table = get_table_name(lb_file.root)
	if not lb_file.action :
		lb_file.action = get_action(lb_file.root)
	return lb_file.__str__()


def get_real_name(lb_file):
	new_file = LiquibaseFile(lb_file.path, lb_file.root, lb_file.name)
	get_file_name(new_file)
	return new_file.__str__()
	


fix_change_log(args.file)
# for root, dirs, files in os.walk(args.file):
# 	#print root
# 	if '/.' in root:
# 		continue
# 	skip = False
# 	for folder in skip_folders:
# 		if folder in root:
# 			skip = True
# 			break
# 	if skip:
# 		continue
# 	for fil in files:
# 		if fil.startswith('.'):
# 			continue
# 		#print root
# 		#print fil
# 		if 'db-changelog.xml' == fil:
# 			fix_change_log(os.path.join(root, fil))
# 		#print fil
# 		#print os.path.join(root, fil)

log (program_epilog, 'ALL')