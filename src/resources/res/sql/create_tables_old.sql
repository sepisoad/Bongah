CREATE TABLE DOCUMENT(
  ID INTEGER PRIMARY KEY AUTOINCREMENT,
  HASREFERRER BOOLEAN,
  REFERRERID INTEGER,
  DATE TEXT
);

CREATE TABLE COLLEAGUE(
  ID INTEGER PRIMARY KEY AUTOINCREMENT,
  NAME TEXT NOT NULL,
  PHONENUMBERS TEXT,
  ADDRESS TEXT,
  DESCRIPTION TEXT
);

CREATE TABLE CUSTOMER(
  ID INTEGER PRIMARY KEY AUTOINCREMENT,
  NAME TEXT,
  ADDRESS TEXT,
  PHONENUMBERS TEXT
);

CREATE TABLE LAND(
  ID INTEGER PRIMARY KEY AUTOINCREMENT,
  DOCID INTEGER,
  APPLICATIONTYPE TEXT,
  TERRAINTYPE TEXT,
  ISHOUSEINCLUDED BOOLEAN,
  ISLANDACRES BOOLEAN
);

CREATE TABLE PROPERTY(
  ID INTEGER PRIMARY KEY AUTOINCREMENT,
  DOCID INTEGER,
  ADDRESS TEXT,
  AREA REAL,
  OWNERNAME TEXT,
  OWNERPHONENUMBERS TEXT,
  SINDHTYPE TEXT,
  PRICE REAL,
  ISSWAPPABLE BOOLEAN,
  PHONENUMBERS TEXT,
  HASPHONE BOOLEAN,
  HASWATER BOOLEAN,
  HASELECTRICITY BOOLEAN,
  HASGAS BOOLEAN,
  LATITUDE REAL,
  LONGITUDE REAL,
  PICTURES TEXT
);