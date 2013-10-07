-- La necesidad de un archivo especial para Las DateClauses, es para
-- abstraer de problemas relacionados con joins  y otros comandos.
insert into test (id, description, costo, fecha, bigdecimal) values (1, 'COSTO', 133333, '2013-12-13',     1000);
insert into test (id, description, costo, fecha, bigdecimal) values (2, 'CASTO',   1245, '2013-12-14',    22222);
insert into test (id, description, costo, fecha, bigdecimal) values (3, 'TESTO',     15, '2013-12-15',   123131);
insert into test (id, description, costo, fecha, bigdecimal) values (4, 'TASTO',    145, '2013-12-16', 43585858);
insert into test (id, description, costo, fecha, bigdecimal) values (5, 'PERO',     225, '2013-12-17',   196565);
insert into test (id, description, costo, fecha, bigdecimal) values (6, 'PETOTE',   225, '2013-12-18',   659292);
insert into test (id, description, costo, fecha, bigdecimal) values (7, 'TOTE',     225, '2013-12-19',     1287);

--Relleno para que los hijos sean 1-1 //?????\\
insert into test (id, description, costo, fecha, bigdecimal) values (8, 'TOTE',     225, '2025-12-19',     1287);
insert into test (id, description, costo, fecha, bigdecimal) values (9, 'TOTE',     225, '2025-12-19',     1287);


insert into test_child (id, description, father_id, fecha, bigdecimal) values(1, 'COSTO_CHILD', 	1, '1970-01-01 08:00:00.0',     1000);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(2, 'COSTO2_CHILD', 	2, '1970-01-01 08:30:00.0',     8228);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(3, 'ASTEAS', 			3, '1970-01-01 09:00:00.0',     6879);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(4, 'TEASTO', 			4, '1970-01-01 09:30:00.0',     2357);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(5, 'TOASTE', 			5, '1970-01-01 14:00:00.0', 15798565);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(6, 'TO', 				6, '1970-01-01 14:30:00.0',   549865);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(7, 'COSTO_CHILD', 	7, '1970-01-01 18:30:00.0',    54896);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(8, 'TOTE', 			8, '1970-01-01 22:59:00.0',  8972145);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(9, 'XXX', 			9, '1970-01-01 23:59:00.0', 54789218);



insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(1, 'COSTO_CHILD_CHILD', 	1, '2013-12-10 08:00:00.0', 1000);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(2, 'COSTO2_CHILD_CHILD2', 	2, '2013-12-11 08:30:00.0',  950);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(3, 'COSTO3_CHILD_CHILD3', 	3, '2013-12-12 09:00:00.0',   75);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(4, '3.1.XX', 				4, '2013-12-13 09:30:00.0',    8);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(5, '2.2.XX', 				5, '2013-12-14 14:00:00.0',   23);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(6, '5.3.XX', 				6, '2013-12-15 14:30:00.0',  542);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(7, '1.8.X2', 				7, '2013-12-16 18:30:00.0', 5420);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(8, '1.9.X2', 				8, '2013-12-17 22:59:00.0',  520);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(9, 'COSTO_CHILD_CHILD', 	9, '2013-12-18 23:59:00.0', 6840);
