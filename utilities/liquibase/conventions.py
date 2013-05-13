import re

"""
Expresion regular para los archivos dentro de la carpeta
Grupo 1: sistema
Grupo 2: esquema
Grupo 3: nombre del archivo
Ejemplo: configuracion/src/main/database/public/FILENAME
"""
liquibase_file_location_filesystem_ex = '([\w_-]+)/src/main/database/([\w_-]+)/([\w_-]+)'
liquibase_file_location_filesystem_re = re.compile(liquibase_file_location_filesystem_ex,re.IGNORECASE)
liquibase_file_location_filesystem = '{system}/src/main/database/{schema}/{filename}'

"""
Expresion regular para los archivos dentro de la carpeta
Grupo 1: sistema
Grupo 2: esquema
Grupo 3: nombre del archivo
Ejemplo: database/base/audit/20130424-REVISION_ENTITY-CREATE_TABLE.xml
"""
liquibase_file_location_classpath_ex = 'database/([\w_-]+)/([\w_-]+)/?([\w_-]*)'
liquibase_file_location_classpath_re = re.compile(liquibase_file_location_classpath_ex,re.IGNORECASE)
liquibase_file_location_classpath = 'database/{system}/{schema}/{filename}'


"""
Convenciones de nombres de archivos
ref: http://appcia.cnc.una.py/wf/index.php/Liquibase_conventions#Nombre_de_los_archivos

{ANHO}{MES}{DIA}_{TABLA}_{ACCION}_{PARAMETROS}*

Grupo 1: ANHO
Grupo 2: MES
Grupo 3: DIA
Grupo 4: TABLA
Grupo 5: ACCION
Grupo 6: PARAMETROS

"""
liquibase_file_name_ex = '(\d{4})(\d{2})(\d{2})_([\w_-]+)_([\w_-]*)_?([\w_-]+)?'
liquibase_file_name_re = re.compile(liquibase_file_name_ex,re.IGNORECASE)
liquibase_file_name = '{year}{month}{day}_{table}_{action}'
liquibase_file_name_w_p = '{year}{month}{day}_{table}_{action}_{params}'




"""
Claves para acciones de liquibase
ref: http://appcia.cnc.una.py/wf/index.php?title=Liquibase_id_conventions
"""


liquibase_id_name='{system}_{schema}_{table}_{action}_##'
liquibase_id_name_w_p='{system}_{schema}_{table}_{action}_{params}_##'

conv = {}
conv["ID"] = "insert"
conv["CT"] = "createTable"
conv["DUC"] = 'dropUniqueConstraint'
conv["AUC"] = 'addUniqueConstraint'
conv["CS"] = "createSequence"
conv['DNNC'] = 'dropNotNullConstraint'
conv['DI'] = 'dropIndex'


def get_conv_key(value):
    for key, val in conv.iteritems():
        if val == value:
            return key
    return None

def get_conv(key):
	return conv[key]

"""
Convenciones para el nombre de los sistemas
"""
sys = {}
sys['configuracion'] = 'conf'
sys['identificacion'] = 'idp'
sys['farmacia'] = 'saf'
sys['stock'] = 'sas'
sys['base'] = 'base'