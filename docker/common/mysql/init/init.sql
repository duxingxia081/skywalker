GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '666666' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'skywalker'@'%' IDENTIFIED BY '666666' WITH GRANT OPTION;
alter user 'skywalker'@'localhost' identified with mysql_native_password by '666666';