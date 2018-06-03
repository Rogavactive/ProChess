drop database if exists accountsDB;
create database accountsDB;
USE accountsDB;

DROP TABLE IF EXISTS accounts;
-- remove table if it already exists and start from scratch

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
    pass_hash	nvarchar(128),
    email		nvarchar(64),
    code		nvarchar(64),
    primary key	(ID),
    unique key(username),
    unique key(email)
);

insert into accounts (username, pass_hash, email) values
    ("bejanadato", "c0f95fe8d96cb131b7d4d7b4a0cda0de", "dbezh16@freeuni.edu.ge");
    


-- select count(ID) as count_matches from accounts
-- 	where username = "aa" and pass_hash = "aa";

select *
from accounts;