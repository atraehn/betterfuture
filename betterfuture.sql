/*
	Wild Cats:
	Anthony Raehn
	Corey Cleric
	CS1555 Final Project: Better Future
*/

--purge recycle bin to ensure space for drops
purge recyclebin;
alter session set recyclebin = off;

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
symbol		varchar2(24)	not null,
name		varchar2(32)	not null,
description	varchar2(108) 	not null,
category	varchar2(16) 	not null,
c_date		date			not null,
CONSTRAINT MUTUALFUND_PK PRIMARY KEY (symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table CLOSINGPRICE(
symbol	varchar2(24) 	not null,
price	float 			not null,
p_date	date			not null,
CONSTRAINT CLOSINGPRICE_PK PRIMARY KEY (symbol,p_date) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT CLOSINGPRICE_FK FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table CUSTOMER(
login		varchar2(16) 	not null,
name		varchar2(24)	not null,
email		varchar2(24)	not null,
address		varchar2(32)	not null,
password	varchar2(16) 	not null,
balance 	float 			not null,
CONSTRAINT CUSTOMER_PK PRIMARY KEY (login) INITIALLY IMMEDIATE DEFERRABLE
);

create table ADMINISTRATOR(
login 		varchar2(16) 	not null,
name 		varchar2(24)	not null,
email		varchar2(24)	not null,
address		varchar2(32)	not null,
password	varchar2(16)	not null,
CONSTRAINT ADMINISTRATOR_PK PRIMARY KEY (login) INITIALLY IMMEDIATE DEFERRABLE
);

create table ALLOCATION(
allocation_no	int				not null,
login			varchar2(16)	not null,
p_date			date			not null,
CONSTRAINT ALLOCATION_PK PRIMARY KEY (allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT ALLOCATION_FK FOREIGN KEY (login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE
);

create table PREFERS(
allocation_no	int				not null,
symbol			varchar2(24)	not null,
percentage		float			not null,
CONSTRAINT PREFERS_PK PRIMARY KEY (allocation_no,symbol) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PREFERS_FK1 FOREIGN KEY (allocation_no) REFERENCES ALLOCATION(allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PREFERS_FK2 FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE
);

create table TRXLOG(
trans_id	int	not null,
login		varchar2(16)	not null,
symbol		varchar2(24),
t_date		date			not null,
action		varchar2(16)	not null,
num_shares	int,
price		float,
amount		float 			not null,
CONSTRAINT TRXLOG_PK PRIMARY KEY (trans_id) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT TRXLOG_FK1 FOREIGN KEY (login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT TRXLOG_FK2 FOREIGN KEY (symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT ACTION_CHECK CHECK (action LIKE 'deposit' OR action LIKE 'sell' OR action LIKE 'buy') INITIALLY IMMEDIATE DEFERRABLE
);

create table OWNS(
login	varchar2(16)	not null,
symbol	varchar2(24)	not null,
shares	int				not null,
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

COMMIT;

insert into CUSTOMER(LOGIN,NAME,EMAIL,ADDRESS,PASSWORD,BALANCE) values('mike','Mike','mike@betterfuture.com','1st street','pwd',750);
insert into CUSTOMER(LOGIN,NAME,EMAIL,ADDRESS,PASSWORD,BALANCE) values('mary','Mary','mary@betterfuture.com','2st street','pwd',0);

COMMIT;

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

COMMIT;

insert into Owns(LOGIN,SYMBOL,SHARES) values('mike','RE',50);

COMMIT;

insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(0,'mike',NULL,'29-MAR-14','deposit',NULL,NULL,1000);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(1,'mike','MM','29-MAR-14','buy',50,10,500);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(2,'mike','RE','29-MAR-14','buy',50,10,500);
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT) values(3,'mike','MM','01-APR-14','sell',50,15,750);

COMMIT;

insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(0,'mike','28-MAR-14');
insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(1,'mary','29-MAR-14');
insert into Allocation(ALLOCATION_NO,LOGIN,P_DATE) values(2,'mike','03-APR-14');

COMMIT;

insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(0,'MM',.5);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(0,'RE',.5);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'STB',.2);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'LTB',.4);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(1,'BBS',.4);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'GS',.3);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'AS',.3);
insert into Prefers(ALLOCATION_NO,SYMBOL,PERCENTAGE) values(2,'IMS',.4);

COMMIT;

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

COMMIT;

-----------------------------------------------------------------------
---------------------------INSERTS ABOVE-------------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
---------------------FUNCTIONS/PROCEDURES BELOW------------------------
-----------------------------------------------------------------------

CREATE OR REPLACE FUNCTION GET_SHARE_PRICE (c_symbol IN VARCHAR2)
	RETURN FLOAT IS share_price FLOAT;
BEGIN
	SELECT price INTO share_price
	FROM CLOSINGPRICE
	WHERE (TO_CHAR(p_date, 'DD-Mon-YY') LIKE TO_CHAR((SELECT MAX(p_date) FROM CLOSINGPRICE WHERE CLOSINGPRICE.symbol LIKE c_symbol), 'DD-Mon-YY') AND symbol LIKE c_symbol);
	
	RETURN(share_price);
END;
/
SHOW ERRORS;

CREATE OR REPLACE FUNCTION GET_PERCENTAGE (c_login IN VARCHAR, c_symbol IN VARCHAR2)
	RETURN FLOAT IS percent FLOAT;
BEGIN
	SELECT percentage INTO percent
	FROM PREFERS
	WHERE (PREFERS.allocation_no = (SELECT MAX(ALLOCATION.allocation_no) FROM ALLOCATION WHERE ALLOCATION.login = c_login) AND PREFERS.symbol LIKE c_symbol);
	
	RETURN(percent);
END;
/
SHOW ERRORS;

CREATE OR REPLACE FUNCTION GET_NUMBER_OF_SHARES(c_login IN VARCHAR2, c_symbol IN VARCHAR2)
RETURN INT AS number_of_shares INT;
BEGIN
	SELECT shares INTO number_of_shares
	FROM OWNS
	WHERE OWNS.symbol LIKE c_symbol AND OWNS.login LIKE c_login;
	
	RETURN (number_of_shares);
END;
/
SHOW ERRORS;

CREATE OR REPLACE PROCEDURE GET_DEPOSIT_INFO (c_login IN VARCHAR2, c_symbol IN VARCHAR2, amount IN FLOAT, 
	percent OUT FLOAT, share_price OUT FLOAT, number_of_shares OUT INT)
AS
BEGIN
	-- get the percentage that customer wants to allocate for this particular stock
	percent := GET_PERCENTAGE(c_login, c_symbol);
	
	-- get the price for this particular stock
	share_price := GET_SHARE_PRICE(c_symbol);
	
	-- calculate the number of shares this customer wants based off of share price and the amount they want to spend on this stock
	number_of_shares := FLOOR((amount*percent)/share_price);
END GET_DEPOSIT_INFO;
/
SHOW ERRORS;

CREATE OR REPLACE PROCEDURE ADD_STOCK_TO_OWNS (c_login IN VARCHAR2, c_symbol IN VARCHAR2, number_of_shares IN INT)
AS
shares_test INT;
BEGIN
	BEGIN
		SELECT shares INTO shares_test
		FROM OWNS
		WHERE OWNS.symbol LIKE c_symbol AND OWNS.login = c_login;
	EXCEPTION 
		WHEN NO_DATA_FOUND THEN NULL;
	END;
	
	IF shares_test IS NOT NULL THEN
		UPDATE OWNS 
		SET shares = shares + number_of_shares
		WHERE OWNS.symbol LIKE c_symbol AND OWNS.login = c_login;
	ELSE
		INSERT INTO OWNS(login, symbol, shares) VALUES (c_login, c_symbol, number_of_shares);
	END IF;
	COMMIT;
END ADD_STOCK_TO_OWNS;
/
SHOW ERRORS;

-----------------------------------------------------------------------
---------------------FUNCTIONS/PROCEDURES ABOVE------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
---------------------------TRIGGERS BELOW------------------------------
-----------------------------------------------------------------------

CREATE OR REPLACE TRIGGER DEPOSIT_MADE
AFTER INSERT ON TRXLOG
FOR EACH ROW WHEN (new.action LIKE 'deposit')
DECLARE
	PRAGMA AUTONOMOUS_TRANSACTION;
	total_investment FLOAT := 0.0;
	percent FLOAT;
	share_price FLOAT;
	number_of_shares INT;
	i INT := 1;
	shares_test INT;
	enough_money BOOLEAN := TRUE;
	CURSOR preference_list IS SELECT symbol FROM PREFERS WHERE PREFERS.allocation_no = (SELECT MAX(ALLOCATION.allocation_no) FROM ALLOCATION WHERE ALLOCATION.login = :new.login);
BEGIN
	-- check if the customer has enough money to invest in all of their preferred stocks
	FOR sym in preference_list LOOP
	
		-- get the price for this particular stock
		share_price := GET_SHARE_PRICE(sym.symbol);
		
		-- get the percentage that customer wants to allocate for this particular stock
		percent := GET_PERCENTAGE(:new.login, sym.symbol);
		
		-- If the customer does not have enough money to buy at least one stock based off his preference
		IF (:new.amount*percent < share_price) THEN
			enough_money := FALSE;
		END IF;
		
	END LOOP;
	
	-- If the customer has enough money to go through with the transaction
	IF (enough_money = TRUE) THEN
	
		-- loop for all of the symbols or percentages in the PREFERS table
		FOR sym IN preference_list LOOP
			
			-- get stock info for this particular preference
			GET_DEPOSIT_INFO(:new.login, sym.symbol, :new.amount, percent, share_price, number_of_shares);
			
			-- sum up the total investment for this deposit to use later
			total_investment := total_investment + number_of_shares*share_price;
			
			-- Fire off the new buy transactions
			INSERT INTO TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT)
			VALUES((:new.trans_id + i), :new.login, sym.symbol, :new.t_date, 'buy', number_of_shares, share_price, number_of_shares*share_price);
			i:=i+1;
			COMMIT;
			
			-- Add newly bought stocks to OWNS table
			ADD_STOCK_TO_OWNS(:new.login, sym.symbol, number_of_shares);
			
		END LOOP;
	-- The customer did not have enough money to buy all of the stocks
	ELSE
		total_investment := 0;
	END IF;
	
	-- Update Customer's total balance, basically adding (amount_deposited - total_invested)
	UPDATE CUSTOMER
	SET balance = balance + :new.amount - total_investment
	WHERE CUSTOMER.login = :new.login;
	
	COMMIT;
END;
/
SHOW ERRORS;

CREATE OR REPLACE TRIGGER SHARES_SOLD
AFTER INSERT ON TRXLOG
FOR EACH ROW WHEN (new.action LIKE 'sell')
DECLARE
	number_of_shares INT;
BEGIN
	-- Update Customer's total balance by adding sold shares
	UPDATE CUSTOMER
	SET balance = balance + :new.amount
	WHERE CUSTOMER.login = :new.login;
	
	-- Assume Customer has the stock and subtract it from owns
	UPDATE OWNS 
	SET shares = shares - :new.num_shares
	WHERE OWNS.symbol LIKE :new.symbol AND OWNS.login = :new.login;
	
	number_of_shares := GET_NUMBER_OF_SHARES(:new.login, :new.symbol);
	
	-- if there are no more of the sold shares owned by the customer, remove that row from the table
	IF number_of_shares = 0 THEN
		DELETE FROM OWNS
		WHERE OWNS.symbol LIKE :new.symbol AND OWNS.login = :new.login;
	END IF;
END;
/
SHOW ERRORS;

-----------------------------------------------------------------------
---------------------------TRIGGERS ABOVE------------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
----------------WE NEED TO MAKE FUNCTION AND SHIT HERE-----------------
-----------------------------------------------------------------------

-- deposit 750 into mike's account
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT)
values(5, 'mike', NULL, '04-APR-14', 'deposit', NULL, NULL, 750);
COMMIT;

SELECT * FROM TRXLOG;

SELECT * FROM CUSTOMER;

-- sell 27 of mike's IMS stocks
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT)
values(9, 'mike', 'IMS', '04-APR-14', 'sell', 27, 11, 297);
COMMIT;

SELECT * FROM TRXLOG;

SELECT * FROM CUSTOMER;

-- test a deposit of  less than the needed amount of money to buy all of stock (for example AS is 18 per stock, and 50*.3=15, so money is simply deposited)
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT)
values(10, 'mike', NULL, '04-APR-14', 'deposit', NULL, NULL, 50);
COMMIT;

SELECT * FROM TRXLOG;

SELECT * FROM CUSTOMER;

-- test a deposit of just enough money to buy one stock of problematic stock above
insert into TRXLOG(TRANS_ID,LOGIN,SYMBOL,T_DATE,ACTION,NUM_SHARES,PRICE,AMOUNT)
values(11, 'mike', NULL, '04-APR-14', 'deposit', NULL, NULL, 60);
COMMIT;

SELECT * FROM TRXLOG;

SELECT * FROM CUSTOMER;

SELECT * FROM OWNS;
