PARA RESTAURAR LA DB EN BASE A UN SQL:
Primero hay que crear primero la DB paw2 en el pgadmin:
Boton derecho sobre Database->New Database-> Name: paw2, Owner: paw, Collation: en_US

psql -d paw2 -f create.sql --username="paw"
psql -d paw2 -f insert.sql --username="paw"
