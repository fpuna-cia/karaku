from conventions import liquibase_file_location_filesystem_re
from conventions import liquibase_file_location_filesystem
from conventions import liquibase_file_location_filesystem_ex

from conventions import liquibase_file_location_classpath_ex
from conventions import liquibase_file_location_classpath
from conventions import liquibase_file_location_classpath_re

import datetime

class NotRegExFound(Exception):
    def __init__(self, expresion, string):
        message = "Expresion: {0} no coincide con la expresion regular {1}".format(
                string, 
                expresion)
        Exception.__init__(self, message)


class BColors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'

def disable():
    BColors.HEADER = ''
    BColors.OKBLUE = ''
    BColors.OKGREEN = ''
    BColors.WARNING = ''
    BColors.FAIL = ''
    BColors.ENDC = ''

args = False
def enable_log(value, color = True):
    global args
    args = value
    if not color:
        disable()

def log(string, level='debug', nivel=0, active=None):
    if not active:
        active = args
    if active :
        if level == 'debug':
            imprimir(BColors.OKBLUE, string, nivel)
        if level == 'info':
            imprimir(BColors.WARNING, string, nivel)
        if level == 'warning':
            imprimir(BColors.HEADER, string, nivel)
        if level == 'error':
            imprimir(BColors.FAIL, string, nivel)
    if level == 'ALL':
        imprimir(BColors.OKGREEN, string, nivel)

def imprimir(color, texto, nivel):
    try:
        print (nivel * '\t') + color + texto + BColors.ENDC
    except :
        print BColors.ENDC

def from_filesystem_to_classpath(file_system_path):
    """
    Metodo que convierte (siguiendo las convenciones) un cadena en del 
    filesystem a su ubicacion en tiempo de ejecucion (classpath).
    """
    result = liquibase_file_location_filesystem_re.search(file_system_path)
    if not result:
        raise NotRegExFound(liquibase_file_location_filesystem_ex, file_system_path)
    system = result.group(1)
    schema = result.group(2)
    filename = result.group(3)
    return fix_fil(liquibase_file_location_classpath.format(
        system=system,
        schema=schema,
        filename=filename))

def from_classpath_to_filesystem(classpath):
    """
    Metodo que convierte (siguiendo las convenciones) un cadena en del 
    classpath a su ubicacion real en el sistema de archivos.
    """
    result = liquibase_file_location_classpath_re.search(classpath)
    if not result:
        raise NotRegExFound(liquibase_file_location_classpath_ex, classpath)
    system = result.group(1)
    schema = result.group(2)
    filename = result.group(3)
    return fix_fil(liquibase_file_location_filesystem.format(
        system=system,
        schema=schema,
        filename=filename))


def fix_fil(fil):
    if (fil.endswith('/')):
        fil = fil[:-1]
    if not fil.endswith('.xml'):
        fil = fil + ".xml"
    return fil

class DateUtil:
    @classmethod
    def get_year(cls):
        return datetime.datetime.now().strftime('%Y')

    @classmethod
    def get_month(cls):
        return datetime.datetime.now().strftime('%m')

    @classmethod
    def get_day(cls):
        return datetime.datetime.now().strftime('%d')

def convert_table_name(table):
    """
    Convierte el nombre de la tabla a la Convenciones
    Por ejemplo: audit_trail pasa a AUDIT-TRAIL
    """
    return table.replace('_', '-').upper()
    