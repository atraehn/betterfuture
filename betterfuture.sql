/*
	Wild Cats:
	Anthony Raehn
	Corey Cleric
	CS1555 Final Project: Better Future
*/

--purge recycle bin to ensure space for drops
purge recyclebin;

--drop old tables to create new ones
drop table MUTUALFUND cascade constraints;
drop table CLOSINGPRICE cascade constraints;
drop table CUSTOMER cascade constraints;
drop table ADMINISTRATOR cascade constraints;
drop table ALLOCATION cascade constraints;
drop table PREFERS cascade constraints;
drop table TRXLOG cascade constraints;
drop table OWNS cascade constraints;
drop table MUTUALDATE cascade constraints;

--create new tables
create table MUTUALFUND(
symbol 	varchar2(20)	not null,
name	varchar2(30)	not null,
description	varchar2(100) 	not null,
category	varchar2(10) 	not null,
c_date		date		not null
);

create table CLOSINGPRICE(
symbol	varchar2(20) 	not null,
price	float 		not null,
p_date	date		not null
);

create table CUSTOMER(
login	varchar2(10) 	not null,
name	varchar2(20)	not null,
email	varchar2(20)	not null,
address	varchar2(30)	not null,
password	varchar2(10) 	not null,
balance float not null
);

create table ADMINISTRATOR(
login 	varchar2(10) 	not null,
name 	varchar2(20)	not null,
email	varchar2(20)	not null,
address	varchar2(30)	not null,
password	varchar2(10)	not null
);

create table ALLOCATION(
allocation_no	int	not null,
login	varchar2(10)	not null,
p_date	date	not null
);

create table PREFERS(
allocation_no	int	not null,
symbol	varchar2(20)	not null,
percentage	float	not null
);

create table TRXLOG(
trans_id	int	not null,
login	varchar2(10)	not null,
symbol	varchar2(20)	not null,
t_date	date	not null,
action	varchar2(10)	not null,
num_shares	int	not null,
price	float	not null,
amount	float not null
);

create table OWNS(
login	varchar2(10)	not null,
symbol	varchar2(20)	not null,
shares	int	not null
);

create table MUTUALDATE(
c_date	date 	not null
);
