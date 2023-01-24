CREATE USER 'daar'@'localhost';
GRANT ALL PRIVILEGES ON daar.* TO 'daar'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
CREATE DATABASE daar CHARACTER SET utf8;
USE daar;

