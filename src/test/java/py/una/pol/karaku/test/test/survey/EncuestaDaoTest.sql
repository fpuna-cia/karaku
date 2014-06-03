--Tabla: encuesta_plantilla
INSERT INTO encuesta_plantilla(id, fecha_creacion, usuario_id, activo)VALUES (500000, '2014-05-06 00:00:00-04', 'ROOT', 'SI');

--Tabla: tipo_bloque
INSERT INTO tipo_bloque(id, descripcion)VALUES (1, 'SIMPLE');
INSERT INTO tipo_bloque(id, descripcion)VALUES (2, 'GRILLA');


--Tabla: encuesta_plantilla_bloque
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id)VALUES (500000, 500000, 'DATOS PERSONALES', 1, 1);
INSERT INTO encuesta_plantilla_bloque(id, encuesta_plantilla_id, titulo, orden, tipo_bloque_id)VALUES (500001, 500000, 'DIRECCIONES', 2, 2);

--Tabla: tipo_objeto"TEXTO"
INSERT INTO tipo_objeto(id, nombre)VALUES (1, 'TEXTO');
INSERT INTO tipo_objeto(id, nombre)VALUES (2, 'RADIO');
INSERT INTO tipo_objeto(id, nombre)VALUES (3, 'CHECK');
INSERT INTO tipo_objeto(id, nombre)VALUES (4, 'COMBO');
INSERT INTO tipo_objeto(id, nombre)VALUES (5, 'TEXTO_FECHA');
INSERT INTO tipo_objeto(id, nombre)VALUES (6, 'TEXTO_AREA');

--Tabla: encuesta_plantilla_pregunta
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable)VALUES (500000, 500000, 1, 'NOMBRE', 1, 'SI', 'nombre', 300, 'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable)VALUES (500001, 500000, 1, 'APELLIDO', 2, 'NO', 'apellido', 300, 'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable)VALUES (500002, 500000, 2, 'SEXO', 3, 'NO', 'sexo', 300, 'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable)VALUES (500003, 500001, 1, 'CALLE', 1, 'SI', 'direccion_calle', 300, 'SI');
INSERT INTO encuesta_plantilla_pregunta(id, encuesta_plantilla_bloque_id, tipo_objeto_id, descripcion, orden, obligatoria, tag, longitud_respuesta, editable)VALUES (500004, 500001, 1, 'NUMERO', 2, 'SI', 'direccion_numero', 300, 'SI');

--Tabla: opcion_respuesta
INSERT INTO opcion_respuesta(id, descripcion, orden, completar, encuesta_plantilla_pregunta_id)VALUES (500000, 'FEMENINO', 1, 'NO', 500001);
INSERT INTO opcion_respuesta(id, descripcion, orden, completar, encuesta_plantilla_pregunta_id)VALUES (500001, 'MASCULINO', 2, 'NO', 500001);

--Tabla: encuesta
INSERT INTO encuesta(id, fecha_realizacion, encuesta_plantilla_id)VALUES (500000, '2014-05-06 00:00:00-04', 500000);

--Tabla: encuesta_detalle
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500000, 500000, 500000, 'GABRIEL', NULL);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500001, 500000, 500001, 'OCAMPOS', NULL);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500002, 500000, 500002, '', NULL);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500003, 500000, 500003, 'DR ABELINO MARTINEZ', 1);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500004, 500000, 500004, '1523', 1);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500005, 500000, 500003, 'DR EZEQUIEL GONZALEZ', 2);
INSERT INTO encuesta_detalle(id, encuesta_id, encuesta_plantilla_pregunta_id, respuesta, nro_fila)VALUES (500006, 500000, 500004, '6859', 2);

--Tabla: encuesta
INSERT INTO encuesta_detalle_opcion_respuesta(id, encuesta_detalle_id, opcion_respuesta_id)VALUES (500000, 500002, 500000);



