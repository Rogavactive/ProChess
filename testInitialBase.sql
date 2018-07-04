drop database if exists testProchessDB;
create database testProchessDB;
USE testProchessDB;

DROP TABLE IF EXISTS accounts;
-- remove table if it already exists and start from scratch

-- ACCOUNTS DDL INITIALIZATION

CREATE TABLE accounts (
	ID        INT auto_increment,
	username  nvarchar(64),
	pass_hash nvarchar(128),
	email     nvarchar(64),
	primary key (ID),
	unique key (username),
	unique key (email)
);

CREATE TABLE validations(
	ID	INT auto_increment,
    username	nvarchar(64),
    password	nvarchar(128),
    email		nvarchar(64),
    code		nvarchar(64),
    primary key	(ID)
);

-- GAME HISTORY DDL INITIALIZATION

DROP TABLE IF EXISTS games;

CREATE TABLE games (
  ID int auto_increment,
  player1ID int,
  player2ID int,
  colorOfPlayer1 boolean,
  colorOfPlayer2 boolean,

  primary key (ID),
  foreign key (player1ID) references accounts(ID),
  foreign key (player2ID) references accounts(ID)
);

DROP TABLE IF EXISTS moves;

CREATE TABLE moves(
  ID	INT auto_increment,
  gameID	int,
  srcRow	int,
  srcCol	int,
  dstRow	int,
  dstCol  int,
  piece   int,

  primary key	(ID),
  foreign key (gameID) references games(ID)
);

drop table if exists search_queue;

CREATE TABLE search_queue(
  username_ID   INT,
  timePrimary   nvarchar(64),
  timeBonus     nvarchar(64),
  foreign key	(username_ID) references accounts(ID),
  primary key	(username_ID),
  unique key (username_ID)
);