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

DROP table if exists puzzles;

create table puzzles(
  ID int auto_increment,
  boardstate varchar(1000),
  computerMoves varchar(1000),
  correctMoves varchar(1000),

  primary key (ID)
);

insert into puzzles (boardstate, computerMoves, correctMoves) values
  ("wR00f wR05t wK06t wP11f wP12f wP15f wP16f wP17f wP20t wP32t wP34t wQ43t wN57t bB46t bP51t bP60f bB61t bP62f bP63f bP66f bP67f bQ73f bR74t bK77t",
  "7476", "4376 5765"),
  ("bB00t wR04t wK06t wP10f wP15f wP16f wP17f wB20t wP33t bP43t wN44t wQ52t bB54t bP60f bP62f bP64f bP65f bP66f bP67f bR71t bQ72t bK73t bR77f",
  "5465", "4465 2064"),
  ("wQ03t wR04t wK06t wP11f wP12f wP15f wP16f wP17f bP22t wB23t wN26t bP40t bQ42t bP54t wP55t wR61t bN63t bP65f bP66f bP67f bR70f bK74f bB75f",
  "6554 6756", "0454 2356 0363"),
  ("wK06t wP10f wP11f wB13t wP15f wP16f wP17f wN25t bP31t wR34t wQ36t bP40t bQ43t bN46t bP52t bP55t bB61t bK65t bP66f wR67t bR70f bR74t",
  "6575 4376 7434", "3647 6777 2546 4765"),
  ("wK06t wP10f wP11f wP12f wP15f wP17f wQ25t bP32t bP33t wP36t bB51t wR54t wN55t bQ57t bP60f bP61f bP62f bN64t bR66t bR70f bK73t",
  "7372 7271 6472", "2543 4363 6374 5563");

-- VARIABLES FOR TESTING (DELETE BEFORE FINAL RELEASE)

insert into accounts (username, pass_hash, email) values
  ("bejanadato", "c0f95fe8d96cb131b7d4d7b4a0cda0de", "dbezh16@freeuni.edu.ge");

-- select count(ID) as count_matches from accounts
-- 	where username = "aa" and pass_hash = "aa";

select *
from account_stats;