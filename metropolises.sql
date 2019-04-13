DROP DATABASE IF EXISTS hw3db;
CREATE DATABASE hw3db;
USE hw3db;

DROP TABLE IF EXISTS metropolises;
 -- remove table if it already exists and start from scratch

CREATE TABLE metropolises (
    metropolis CHAR(64),
    continent CHAR(64),
    population BIGINT
);

INSERT INTO metropolises VALUES
	("Mumbai","Asia",20400000),
    ("New York","North America",21295000),
	("San Francisco","North America",5780000),
	("London","Europe",8580000),
	("Rome","Europe",2715000),
	("Melbourne","Australia",3900000),
	("San Jose","North America",7354555),
	("Rostov-on-Don","Europe",1052000);
    
SELECT *
FROM hw3db.metropolises m;

COMMIT;
ROLLBACK;