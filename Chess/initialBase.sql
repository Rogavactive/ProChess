drop database if exists accountsDB;
create database accountsDB;
USE accountsDB;
  
DROP TABLE IF EXISTS accounts;
 -- remove table if it already exists and start from scratch

CREATE TABLE accounts (
	ID INT auto_increment,
    username nvarchar(64),
    pass_hash nvarchar(128),
    email nvarchar(64),
    primary key (ID),
    unique key (username),
    unique key (email)
);

select email from accounts where username="rogavactive";

insert into accounts(username,pass_hash,email) values
("rogavactive","D1m1tr110","droga16@freeuni.edu.ge");

-- select count(ID) as count_matches from accounts
-- 	where username = "aa" and pass_hash = "aa";

select * from accounts;