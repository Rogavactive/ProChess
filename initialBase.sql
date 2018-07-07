drop database if exists prochessDB;
create database prochessDB;
USE prochessDB;

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

drop TABLE IF EXISTS validations;

CREATE TABLE validations(
	ID	INT auto_increment,
    username	nvarchar(64),
    password	nvarchar(128),
    email		nvarchar(64),
    code		nvarchar(64),
    primary key	(ID)
);

-- GAMES FOR ACCOUNTS

drop TABLE IF EXISTS account_stats;

CREATE TABLE account_stats(
  acc_ID INT,
  bulletRank INT,
  bulletGames INT,
  blitzRank INT,
  blitzGames INT,
  classicalRank INT,
  classicalGames INT,
  UNIQUE (acc_ID),
  FOREIGN KEY (acc_ID) REFERENCES accounts(ID) on DELETE CASCADE
);

ALTER TABLE account_stats
  ADD CONSTRAINT acc_id_fk
FOREIGN KEY (acc_ID) REFERENCES accounts(ID) on DELETE CASCADE;

-- GAME HISTORY DDL INITIALIZATION

DROP TABLE IF EXISTS games;

CREATE TABLE games (
  ID int auto_increment,
  player1ID int,
  player2ID int,
  colorOfPlayer1 boolean,
  colorOfPlayer2 boolean,
  winner int,
  time datetime,

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
  pieceType   int,
  pieceColor boolean,
  numberOfMove int,

  primary key	(ID),
  foreign key (gameID) references games(ID)
);

-- GAME SEARCH MANAGMENT DDL

 drop table if exists ongoing_games_ids;

 CREATE TABLE ongoing_games_ids(
   ID	INT auto_increment,
   temp_id	nvarchar(128),
   primary key	(ID),
   unique key (temp_id)
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

-- VARIABLES FOR TESTING (DELETE BEFORE FINAL RELEASE)

insert into accounts (username, pass_hash, email) values
  ("bejanadato", "c0f95fe8d96cb131b7d4d7b4a0cda0de", "dbezh16@freeuni.edu.ge");

-- select count(ID) as count_matches from accounts
-- 	where username = "aa" and pass_hash = "aa";

select *
from account_stats;