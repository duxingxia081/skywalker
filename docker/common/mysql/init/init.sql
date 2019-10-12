CREATE USER 'skywalker'@'%' IDENTIFIED WITH mysql_native_password BY 'Jade_19960902';
ALTER USER 'root'@'%' IDENTIFIED BY 'Jade_19960902'
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'skywalker'@'%';
