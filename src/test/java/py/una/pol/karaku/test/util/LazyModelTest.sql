-- plantilla
insert into encuesta_plantilla(id, fecha_creacion, usuario_id, activo, descripcion, key) values ('1','2013-05-27 16:00:00-04','SIGH_APP_USER','SI','COMPLETAR DESCRIPCION 1','KEY1');

-- tipo de bloque
INSERT INTO tipo_bloque(id, descripcion) VALUES (1, 'SIMPLE');

-- bloque plantilla
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (1, 1, 'Datos personales del/la usuario/a', 1, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (2, 1, 'Datos personales del/la usuario/a', 2, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (3, 1, 'Datos personales del/la usuario/a', 3, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (4, 1, 'Datos personales del/la usuario/a', 4, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (5, 1, 'Datos personales del/la usuario/a', 5, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (6, 1, 'Datos personales del/la usuario/a', 6, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (7, 1, 'Datos personales del/la usuario/a', 7, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (8, 1, 'Datos personales del/la usuario/a', 8, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (9, 1, 'Datos personales del/la usuario/a', 9, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (10, 1, 'Datos personales del/la usuario/a', 10, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id) VALUES (11, 1, 'Diagnóstico Social y Seguimiento', 11, 1);
-- tipo de objeto
INSERT INTO tipo_objeto(id, nombre) VALUES (1, 'TEXTO');
INSERT INTO tipo_objeto(id, nombre) VALUES (2, 'RADIO');
INSERT INTO tipo_objeto(id, nombre) VALUES (3, 'CHECK');
INSERT INTO tipo_objeto(id, nombre) VALUES (4, 'COMBO');
INSERT INTO tipo_objeto(id, nombre) VALUES (5, 'TEXTO_FECHA');
INSERT INTO tipo_objeto(id, nombre) VALUES (6, 'TEXTO_AREA');

-- preguntas
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (1,1,1,'Nombre y apellido',1,'SI','nombre_apellido',100,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (2,1,1,'C.I.',2,'NO','cedula_identidad',100,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (3,1,5,'Fecha de nacimiento',3,'NO','fecha_nacimiento',10,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (4,1,1,'Lugar de nacimiento:',4,'NO','lugar_nacimiento',15,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (5,1,1,'E. Civil',6,'SI','estado_civil',15,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (6,1,1,'Edad',7,'NO','edad',3,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (7,1,1,'Sexo',8,'SI','sexo',100,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (8,1,1,'Ciudad donde vive:',9,'NO','ciudad_vive',50,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (9,1,1,'Departamento de nacimiento:',5,'NO','departamento_nacimiento',50,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (10,1,1,'Teléfono',10,'NO','telefono',15,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (11,1,1,'Domicilio',11,'NO','domicilio',100,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (12,1,1,'Otras referencias',12,'NO','otras_referencias',100,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (13,1,1,'Profesión',13,'NO','profesion',50,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (14,1,1,'Ocupación',14,'NO','ocupacion',50,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (15,1,1,'Ingresos',15,'NO','ingresos',15,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (16,1,1,'Nivel de instrucción',16,'NO','nivel_instruccion',50,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (17,1,1,'Fecha de ingreso',17,'NO','fecha_ingreso',50,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (18,1,1,'C.Externo',18,'NO','fecha_ingreso',50,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (19,1,1,'Internación',19,'NO','fecha_ingreso',50,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (20,1,1,'Porqué prefirió venir al H.C.',20,'NO','porque_prefirio_hc',100,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (21,1,6,'Diagnóstico Médico',21,'SI','diagnostico_medico',500,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (22,1,1,'Responsable',22,'NO','responsable',50,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (23,1,1,'Vínculo',23,'NO','vinculo_responsable',30,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (24,1,1,'C.I.',24,'NO','ci_responsable',10,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (25,1,1,'Entrevistado/a',26,'SI','entrevistado',50,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (26,1,1,'Vínculo',27,'SI','vinculo_entrevistado',30,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (27,1,1,'C.I.',28,'NO','ci_entrevistado',10,'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (28,2,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (29,3,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (30,4,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (31,5,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (32,6,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (33,7,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (34,8,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (35,9,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (36,10,1,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable) values (37,11,6,'Diagnostico Social',1,'NO','diagnostico_social',500,'NO');
-- encuesta
INSERT INTO encuesta(id, fecha_realizacion,encuesta_plantilla_id) values (1,'2014-11-21 08:48:00-03',1);

-- respuestas
INSERT INTO encuesta_detalle(id, encuesta_id,encuesta_plantilla_pregunta_id , respuesta, nro_fila) values (529186,1,1,'ROBERTO BAEZ BAREIRO',null);
