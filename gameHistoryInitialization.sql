drop database if exists gameHistory;
create schema gameHistory;
USE gameHistory;

DROP TABLE IF EXISTS games;

CREATE TABLE games (
	  ID int auto_increment,
    player1ID int,
    player2ID int,
    colorOfPlayer1 boolean,
    colorOfPlayer2 boolean,
    winner int,
    
	  primary key (ID),
    foreign key (player1ID) references prochessdb.accounts(ID),
    foreign key (player2ID) references prochessdb.accounts(ID)
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
    
    primary key	(ID),
    foreign key (gameID) references games(ID)
);
