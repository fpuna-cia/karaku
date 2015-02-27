-- Tipo Dependencia
INSERT INTO tipo_dependencia(id, descripcion, fecha_registro, fecha_ult_modificacion, borrado_logico, uri) VALUES (1, 'SERVICIOS', '2013-07-18 19:10:26.991-04', '2013-07-18 19:10:26.991-04', 'false', 'sigh.med.una.py/conf/tipo_dependencia/2');

-- Dependencia
INSERT INTO dependencia(id, descripcion, borrado_logico, dependencia_padre_id, tipo_dependencia_id, uri) VALUES (1, 'CATEDRA DE CLINICA OFTALMOLOGICA', 'false', null, 1, 'sigh.med.una.py/conf/dependencia/2');

-- Pais
INSERT INTO pais(id, descripcion, borrado_logico, uri) VALUES(1,'Paraguay', 'false', 'sigh.med.una.py/asis/pais/1');

-- Universidad
INSERT INTO universidad(id, nombre, sigla, pais_id, uri, borrado_logico) VALUES (1,'Universidad Nacional de Asunción', 'UNA', 1, 'sigh.med.una.py/asis/universidad/1', 'false');