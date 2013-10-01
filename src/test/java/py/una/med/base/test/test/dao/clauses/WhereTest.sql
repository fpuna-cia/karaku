insert into test (id, description, costo, fecha, bigdecimal) values (1, 'COSTO', 133333, '2013-12-13',     1000);
insert into test (id, description, costo, fecha, bigdecimal) values (2, 'CASTO',   1245, '2013-12-14',    22222);
insert into test (id, description, costo, fecha, bigdecimal) values (3, 'TESTO',     15, '2013-12-15',   123131);
insert into test (id, description, costo, fecha, bigdecimal) values (4, 'TASTO',    145, '2013-12-16', 43585858);
insert into test (id, description, costo, fecha, bigdecimal) values (5, 'PERO',     225, '2013-12-17',   196565);
insert into test (id, description, costo, fecha, bigdecimal) values (6, 'PETOTE',   225, '2013-12-18',   659292);
insert into test (id, description, costo, fecha, bigdecimal) values (7, 'TOTE',     225, '2013-12-19',     1287);


insert into test_child (id, description, father_id, fecha, bigdecimal) values(1, 'COSTO_CHILD', 	3, '2013-12-13',     1000);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(2, 'COSTO2_CHILD', 	2, '2013-12-13',     8228);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(3, 'ASTEAS', 			5, '2013-12-13',     6879);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(4, 'TEASTO', 			6, '2013-12-13',     2357);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(5, 'TOASTE', 			7, '2013-12-13', 15798565);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(6, 'TO', 				4, '2013-12-13',   549865);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(7, 'COSTO_CHILD', 	4, '2013-12-13',    54896);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(8, 'TOTE', 			1, '2013-12-13',  8972145);
insert into test_child (id, description, father_id, fecha, bigdecimal) values(9, 'XXX', 			1, '2013-12-13', 54789218);



insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(1, 'COSTO_CHILD_CHILD', 	1, '2013-12-13', 1000);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(2, 'COSTO2_CHILD_CHILD2', 	1, '2013-12-13',  950);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(3, 'COSTO3_CHILD_CHILD3', 	1, '2013-12-13',   75);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(4, '3.1.XX', 				1, '2013-12-13',    8);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(5, '2.2.XX', 				2, '2013-12-13',   23);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(6, '5.3.XX', 				3, '2013-12-13',  542);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(7, '1.8.X2', 				8, '2013-12-13', 5420);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(8, '1.9.X2', 				9, '2013-12-13',  520);
insert into test_grand_child (id, description, father_id, fecha, bigdecimal) values(9, 'COSTO_CHILD_CHILD', 	2, '2013-12-13', 6840);
