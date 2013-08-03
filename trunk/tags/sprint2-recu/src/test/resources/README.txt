PARA RESTAURAR LA DB EN BASE A UN SQL:
Primero hay que crear primero la DB paw2 en el pgadmin:
Boton derecho sobre Database->New Database-> Name: paw2, Owner: paw, Collation: en_US

Para crear las tablas del sprint 1 e insertar datos:
./create.sh
./insert.sh

Para actualizar a las tablas del sprint 2 e insertar datos:
./update.sh
./insert2.sh

Para actualizar a las tablas del recuperatorio del sprint 2:
./update_rec.sh
