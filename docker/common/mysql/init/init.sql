alter user 'skywalker' identified with mysql_native_password by 'Jade_19960902';
set password for 'root'@'%'=password('Jade_19960902');
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'skywalker'@'%';