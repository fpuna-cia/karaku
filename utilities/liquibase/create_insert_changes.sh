#!bin/bash

liquibase \
		--url=jdbc:postgresql://localhost:5432/configuracion \
		--driver=org.postgresql.Driver \
		--username=postgres \
		--password="postgres" \
		--changeLogFile=db.changelog-0.1.0.xml \
		--classpath=/home/arturo/develop/facultad/workspace/metaconfiguracion/postgresql-9.1-901.jdbc4.jar \
		--diffTypes="data" \
		generateChangeLog