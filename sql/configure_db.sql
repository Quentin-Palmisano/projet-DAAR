CREATE USER 'daar'@'localhost';
GRANT ALL PRIVILEGES ON daar.* TO 'daar'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
CREATE DATABASE daar CHARACTER SET utf8;
USE daar;

CREATE TABLE Livre (
	Id BIGINT NOT NULL,
	Titre VARCHAR(300) NOT NULL,
	Author VARCHAR(300),
	Date VARCHAR(300),
	Language VARCHAR(300),
	Closeness FLOAT(50),
	PRIMARY KEY (Id)
);

CREATE TABLE Occurence (
	Id BIGINT NOT NULL,
	Mot VARCHAR(100) NOT NULL,
	Count BIGINT NOT NULL,
	PRIMARY KEY (Mot, Id)
);

CREATE TABLE Distance (
	Id1 BIGINT NOT NULL,
	Id2 BIGINT NOT NULL,
	Dist FLOAT(50) NOT NULL,
	PRIMARY KEY (Id1, Id2)
);