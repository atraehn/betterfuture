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

-----------------------------------------------------------------------
---------------------------TABLES BELOW--------------------------------
-----------------------------------------------------------------------
create table MUTUALFUND(
symbol 	varchar2(24)	not null,
name	varchar2(32)	not null,
description	varchar2(108) 	not null,
category	varchar2(16) 	not null,
c_date		date		not null,
CONSTRAINT MUTUALFUND_PK PRIMARY KEY (symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table CLOSINGPRICE(
symbol	varchar2(24) 	not null,
price	float 		not null,
p_date	date		not null,
CONSTRAINT CLOSINGPRICE_PK PRIMARY KEY (symbol,p_date) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT CLOSINGPRICE_FK FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table CUSTOMER(
login	varchar2(16) 	not null,
name	varchar2(24)	not null,
email	varchar2(24)	not null,
address	varchar2(32)	not null,
password	varchar2(16) 	not null,
balance float not null,
CONSTRAINT CUSTOMER_PK PRIMARY KEY (login) INITIALLY IMMEDIATE DEFERRABLE
);

create table ADMINISTRATOR(
login 	varchar2(16) 	not null,
name 	varchar2(24)	not null,
email	varchar2(24)	not null,
address	varchar2(32)	not null,
password	varchar2(16)	not null,
CONSTRAINT ADMINISTRATOR_PK PRIMARY KEY (login) INITIALLY IMMEDIATE DEFERRABLE
);

create table ALLOCATION(
allocation_no	int	not null,
login	varchar2(16)	not null,
p_date	date	not null,
CONSTRAINT ALLOCATION_PK PRIMARY KEY (allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT ALLOCATION_FK FOREIGN KEY (login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE
);

create table PREFERS(
allocation_no	int	not null,
symbol	varchar2(24)	not null,
percentage	float	not null,
CONSTRAINT PREFERS_PK PRIMARY KEY (allocation_no,symbol) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PREFERS_FK1 FOREIGN KEY (allocation_no) REFERENCES ALLOCATION(allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PREFERS_FK2 FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table TRXLOG(
trans_id	int	not null,
login	varchar2(16)	not null,
symbol	varchar2(24),
t_date	date	not null,
action	varchar2(16)	not null,
num_shares	int,
price	float,
amount	float not null,
CONSTRAINT TRXLOG_PK PRIMARY KEY (trans_id) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT TRXLOG_FK1 FOREIGN KEY (login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT TRXLOG_FK2 FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table OWNS(
login	varchar2(16)	not null,
symbol	varchar2(24)	not null,
shares	int	not null,
CONSTRAINT OWNS_PK PRIMARY KEY (login,symbol) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT OWNS_FK1 FOREIGN KEY (login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT OWNS_FK2 FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table MUTUALDATE(
c_date	date 	not null,
CONSTRAINT MUTUALDATE_PK PRIMARY KEY (c_date) INITIALLY IMMEDIATE DEFERRABLE
);
-----------------------------------------------------------------------
---------------------------TABLES ABOVE--------------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
---------------------------INSERTS BELOW-------------------------------
-----------------------------------------------------------------------
insert into MUTUALDATE(c_date) values('04-APR-14');

insert into CUSTOMER(LOGIN,NAME,EMAIL,ADDRESS,PASSWORD,BALANCE) values('mike','Mike','mike@betterfuture.com','1st street','pwd',750);
insert into CUSTOMER(LOGIN,NAME,EMAIL,ADDRESS,PASSWORD,BALANCE) values('mary','Mary','mary@betterfuture.com','2st street','pwd',0);

insert into Administrator(LOGIN,NAME,EMAIL,ADDRESS,PASSWORD) values('admin','Administrator','admin@betterfuture.com','5th Ave, Pitt','root');

insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('MM','money-market','money market, conservative','fixed','06-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('RE','real-estate','real estate','fixed','09-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('STB','short-term-bonds','short term bonds','bonds','10-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('LTB','long-term-bonds','long term bonds','bonds','11-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('BBS','balance-bonds-stocks','balance bonds and stocks','mixed','12-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('SRBS','social-respons-bonds-stocks','social responsibility bonds and stocks','mixed','12-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('GS','general-stocks','general stocks','stocks','16-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('AS','aggressive-stocks','aggressive stocks','stocks','23-JAN-14');
insert into MutualFund(SYMBOL,NAME,DESCRIPTION,CATEGORY,C_DATE) values('IMS','international-markets-stock','international markets stock, risky','stocks','30-JAN-14');

insert into Owns(LOGIN,SYMBOL,SHARES) values('mike','RE',50);

insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(0,'mike',NULL,'29-MAR-14','deposit',NULL,NULL,1000);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(1,'mike','MM','29-MAR-14','buy',50,10,500);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(2,'mike','RE','29-MAR-14','buy',50,10,500);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(3,'mike','MM','01-APR-14','sell',50,15,750);

insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(0,'mike','28-MAR-14');
insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(1,'mary','29-MAR-14');
insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(2,'mike','03-APR-14');

insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(0,'MM',.5);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(0,'RE',.5);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'STB',.2);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'LTB',.4);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'BBS',.4);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'GS',.3);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'AS',.3);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'IMS',.4);

insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',10,'28-MAR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',11,'29-MAR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',12,'30-MAR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',15,'31-MAR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',14,'01-APR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',15,'02-APR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('MM',16,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',12,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',15,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',14,'31-MAR-14');
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',16,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',17,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('RE',15,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',9,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',10,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',12,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',14,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',10,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('STB',12,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',12,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',13,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',15,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',12,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',9,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('LTB',10,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',11,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',14,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',18,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',13,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',15,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('BBS',16,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',12,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',12,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',14,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',17,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',20,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('SRBS',20,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',12,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',13,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',15,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',14,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',15,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('GS',12,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',15,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',14,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',16,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',14,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',17,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('AS',18,'03-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',10,'28-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',12,'29-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',12,'30-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',14,'31-MAR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',13,'01-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',12,'02-APR-14');                                       
insert into ClosingPrice(SYMBOL,PRICE,P_DATE) values('IMS',11,'03-APR-14');

commit;

-----------------------------------------------------------------------
---------------------------INSERTS ABOVE-------------------------------
-----------------------------------------------------------------------


