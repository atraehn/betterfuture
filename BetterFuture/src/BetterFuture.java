/*
 * Anthony Raehn and Corey Cleric
 * CS1555
 * Wild Cats
 * BetterFuture
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
//needed by java for database connection and manipulation

public class BetterFuture {
	private Connection connection; // used to hold the jdbc connection to the DB
	private Statement statement; // used to create an instance of the connection
	private ResultSet resultSet; // used to hold the result of your query (if
									// one exists)
	private String query; // this will hold the query we are using
	private String pitt_username, pitt_password;

	private Scanner input;

	/*** Better Future variables ***/
	private String username;
	private String password;
	
	private java.sql.Date mutualDate;
	
	SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

	public BetterFuture() {
		/*
		 * Making a connection to a DB causes certain exceptions. In order to
		 * handle these, you either put the DB stuff in a try block or have your
		 * function throw the Execptions and handle them later. For this demo I
		 * will use the try blocks
		 */

		pitt_username = "cmc143"; // This is your username in oracle
		pitt_password = "3560867"; // This is your password in oracle

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

			connection = DriverManager.getConnection(url, pitt_username,
					pitt_password);
			// create a connection to DB on class3.cs.pitt.edu
		} catch (Exception Ex) // What to do with any exceptions
		{
			System.out.println("Error connecting to database.  Machine Error: "
					+ Ex.toString());
			Ex.printStackTrace();
		}

		// connected to database, do login stuff now:

		try {
			statement = connection.createStatement(); // create an instance
		} catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
		input = new Scanner(System.in);

		/*** Welcome Message ***/
		System.out.println("Welcome to Better Future's Financial System!\n");

		/*** User/Admin Login ***/
		boolean loginSuccess = false;
		boolean isAdmin = false;
		try {
			
			// get MutualDate
			query = "SELECT MAX(c_date) FROM MUTUALDATE";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				mutualDate = resultSet.getDate(1);
			}
			
			System.out.println("To get started, please login to the system (admin or user).");
			// loop until a user or admin logs in to the system
			while (!loginSuccess) {
				// get username
				System.out.print("Username: ");
				username = input.nextLine();

				// get password
				System.out.print("Password: ");
				password = input.nextLine();

				int loginInfo = attemptLogin(username, password);

				// login failed
				if (loginInfo == 0) {
					System.out
							.println("\nERROR: Login unsuccesful. Please try again.\n");
					loginSuccess = false;
				}

				// user logged in
				else if (loginInfo == 1) {
					loginSuccess = true;
					isAdmin = false;
				}

				// admin logged in
				else if (loginInfo == 2) {
					loginSuccess = true;
					isAdmin = true;
				}
			}
		} catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
		// if they get to this point, successfully logged in!

		char action;
		/*** if the user is an ADMIN ***/
		if (isAdmin == true) {
			String str;
			java.sql.Date date;
			float fl;
			PreparedStatement updateStatement;
			boolean done = false;
			while (!done) {
				System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("a: Add New Mutual Fund");
				System.out.println("n: New Customer Registration");
				System.out.println("s: Update Share Quotes");
				System.out.println("t: Statistics");
				System.out.println("d: Update Time and Date");
				System.out.println("q: Quit");
				System.out.print("Please select one of the above actions: ");

				// get first letter of input in lower case
				action = input.nextLine().toLowerCase().charAt(0);

				try {
					switch (action) {
						case 'a':
						{
							// add new mutual fund
							query = "INSERT INTO MUTUALFUND VALUES (?,?,?,?,?)";
							updateStatement = connection.prepareStatement(query);
	
							System.out.print("Enter a mutual fund symbol: ");
							str = input.nextLine().toUpperCase();
							updateStatement.setString(1, str);
							System.out.print("Enter a mutual fund name: ");
							str = input.nextLine();
							updateStatement.setString(2, str);
							System.out.print("Enter a mutual fund description: ");
							str = input.nextLine();
							updateStatement.setString(3, str);
							System.out.print("Enter a mutual fund category: ");
							str = input.nextLine();
							updateStatement.setString(4, str);
							System.out
									.print("Enter a mutual fund date (format: dd-MMM-yy): ");
							str = input.nextLine();
							date = new java.sql.Date(df.parse(str).getTime());
							updateStatement.setDate(5, date);
	
							updateStatement.executeUpdate();
	
							// check if INSERT applied
							System.out.println("\nNew table result:");
							resultSet = mutualFund();
							while (resultSet.next()) {
								System.out.println("" + resultSet.getString(1)
										+ ", " + resultSet.getString(2) + ", "
										+ resultSet.getString(3) + ", "
										+ resultSet.getString(4) + ", "
										+ resultSet.getDate(5));
							}
							break;
						}
						case 'n':
						{
							// new customer registration
							query = "INSERT INTO CUSTOMER VALUES (?,?,?,?,?,?)";
							updateStatement = connection.prepareStatement(query);
	
							System.out.print("Enter a customer login: ");
							str = input.nextLine();
							updateStatement.setString(1, str);
							System.out.print("Enter a customer name: ");
							str = input.nextLine();
							updateStatement.setString(2, str);
							System.out.print("Enter a customer email: ");
							str = input.nextLine();
							updateStatement.setString(3, str);
							System.out.print("Enter a customer address: ");
							str = input.nextLine();
							updateStatement.setString(4, str);
							System.out.print("Enter a customer password: ");
							str = input.nextLine();
							updateStatement.setString(5, str);
							System.out.print("Enter a customer balance: ");
							str = input.nextLine();
							fl = Float.parseFloat(str);
							updateStatement.setFloat(6, fl);
	
							updateStatement.executeUpdate();
	
							// check if INSERT applied
							System.out.println("\nNew table result:");
							resultSet = customer();
							while (resultSet.next()) {
								System.out.println("" + resultSet.getString(1)
										+ ", " + resultSet.getString(2) + ", "
										+ resultSet.getString(3) + ", "
										+ resultSet.getString(4) + ", "
										+ resultSet.getString(5) + ", "
										+ resultSet.getFloat(6));
							}
							break;
						}
						case 's':
						{
							// update share quotes
							break;
						}
						case 't':
						{
							// statistics
							break;
						}
						case 'd':
						{
							// update time and date
							break;
						}
						case 'q':
						{
							done = true;
							break;
						}
					}
				} catch (java.sql.SQLIntegrityConstraintViolationException ic) {
					System.out.println("" + ic.getMessage());
					action = 'x';
				} catch (Exception Ex) {
					System.out.println("Machine Error: " + Ex.toString());
				}
			}
		}

		/*** if the user is a CUSTOMER ***/
		if (isAdmin == false) {
			String str;
			java.sql.Date date;
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yy");
			boolean done = false;
			while (!done)
			{
				System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("a: Change Allocation Preferrence");
				System.out.println("b: Buy Shares");
				System.out.println("f: Browse Mutual Funds");
				System.out.println("i: Investing");
				System.out.println("m: Search Mutual Fund by Text");
				System.out.println("p: Check Portfolio");
				System.out.println("s: Sell Shares");
				System.out.println("q: Quit");
				System.out.print("Please select one of the above actions: ");

				// get first letter of input in lower case
				action = input.nextLine().toLowerCase().charAt(0);
				
				switch (action) {
					case 'a':
					{
						// allocation preferences
						
						allocationChange();
						
						break;
					}
					case 'b':
					{
						// buy shares
						
						buyShares();
						
						break;
					}
					case 'f':
					{
						// browse mutual funds
						
						browseMutualFunds();
						
						break;
					}
					case 'i':
					{
						// investing
						
						investDeposit();
						
						break;
					}
					case 'm':
					{
						// search mutual funds by text
						
						searchMutualFunds();
						
						break;
					}
					case 'p':
					{
						// check portfolio
						
						customerPortfolio();
						
						break;
					}
					case 's':
					{
						// sell shares
						
						sellShares();
						
						break;
					}
					case 'q':
					{
						done = true;
						break;
					}
				}
			}
		}

		try {
			// done, close the connection
			connection.close();
			System.out.println("Connection closed.");
		} catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}

	public static void main(String args[]) {
		BetterFuture better_future_session = new BetterFuture();
	}

	public ResultSet mutualFund() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM MUTUALFUND");
	}

	public ResultSet closingPrice() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM CLOSINGPRICE");
	}

	public ResultSet customer() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM CUSTOMER");
	}

	public ResultSet administrator() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM ADMINISTRATOR");
	}

	public ResultSet allocation() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM ALLOCATION");
	}

	public ResultSet prefers() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM PREFERS");
	}

	public ResultSet trxlog() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM TRXLOG");
	}

	public ResultSet owns() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM OWNS");
	}

	public ResultSet mutualDate() throws Exception {
		statement = connection.createStatement(); // create an instance
		return statement.executeQuery("SELECT * FROM MUTUALDATE");
	}

	public int attemptLogin(String user, String pass) {
		try {
			// query CUSTOMER table
			query = "SELECT * FROM CUSTOMER WHERE login = '" + user
					+ "' AND password = '" + pass + "'";

			resultSet = statement.executeQuery(query);

			// user is a CUSTOMER, successful login
			if (resultSet.next()) {
				// Welcome back message
				System.out.println("\nWecome back, user "
						+ resultSet.getString(2) + "!\n");
				return 1;
			} else {
				query = "SELECT * FROM ADMINISTRATOR WHERE login = '" + user
						+ "' AND password = '" + pass + "'";

				resultSet = statement.executeQuery(query);

				// user is an ADMINISTRATOR, successful login
				if (resultSet.next()) {
					// Welcome back message
					System.out.println("\nWecome back, admin "
							+ resultSet.getString(2) + "!\n");
					return 2;
				}

				// failed login
				else {
					return 0;
				}
			}
		} catch (Exception ex) {
			System.out.println("\nException error! : " + ex.toString() + "\n");
		}

		// failed login
		return 0;
	}
	
	public void browseMutualFunds()
	{
		try
		{
			PreparedStatement updateStatement;
			
			// get category
			System.out.println("\nHow would you like to browse the mutual funds?\nBy:");
			System.out.println("\t(a) All");
			System.out.println("\t(b) Bonds");
			System.out.println("\t(f) Fixed");
			System.out.println("\t(m) Mixed");
			System.out.println("\t(s) Stocks");
			System.out.println("\t(Anything else to quit.):");
			
			char category = input.nextLine().toLowerCase().charAt(0);
			
			// they picked their category
			if (category == 'a' || category == 'b' || category == 'f' || category == 'm' || category == 's')
			{
				// get sorting type (force them)
				char sortType;
				do
				{
					System.out.print("\nSort mutual funds by (a) alphabet or (p) price? ");
					sortType = input.nextLine().toLowerCase().charAt(0);
				} while (!(sortType == 'a' || sortType == 'p'));
				
				// build query for alphabetical sorting
				if (sortType == 'a')
				{
					query = "SELECT M.symbol, M.name, M.category, C.p_date, C.price, M.description "
							+ "FROM MUTUALFUND M JOIN CLOSINGPRICE C ON M.symbol = C.symbol "
							+ "WHERE C.p_date = (SELECT MAX(p_date) FROM CLOSINGPRICE) ";
					
					switch (category)
					{
						case 'a':
							// all, do nothing
							break;
						case 'b':
							query += "AND M.category LIKE 'bonds' ";
							break;
						case 'f':
							query += "AND M.category LIKE 'fixed' ";
							break;
						case 'm':
							query += "AND M.category LIKE 'mixed' ";
							break;
						case 's':
							query += "AND M.category LIKE 'stocks' ";
							break;
					}
					
					query += "ORDER BY M.symbol ASC";
					
				}
				
				// build query for price sorting
				else if (sortType == 'p')
				{
					System.out.print("\nEnter a date to sort stock price by ('DD-MMM-YY'): ");
					String sortDate = input.nextLine().toUpperCase();
					
					// check for invalid format for date
					if (!sortDate.matches("\\d{2}-\\w{3}-\\d{2}"))
					{
						System.out.println("\nERROR! The date you entered was invalid format.\n");
						return;
					}
					
					query = "SELECT p_date FROM CLOSINGPRICE WHERE p_date = '" + sortDate + "'";
					resultSet = statement.executeQuery(query);
					
					// quick check of if date is in database
					if (!resultSet.next())
					{
						System.out.println("\nERROR! The date you entered did not exist in database.\n");
						return;
					}
					
					query = "SELECT M.symbol, M.name, M.category, C.p_date, C.price, M.description "
							+ "FROM MUTUALFUND M JOIN CLOSINGPRICE C ON M.symbol = C.symbol "
							+ "WHERE C.p_date = '" + sortDate + "' ";
					
					switch (category)
					{
						case 'a':
							// all, do nothing
							break;
						case 'b':
							query += "AND M.category LIKE 'bonds' ";
							break;
						case 'f':
							query += "AND M.category LIKE 'fixed' ";
							break;
						case 'm':
							query += "AND M.category LIKE 'mixed' ";
							break;
						case 's':
							query += "AND M.category LIKE 'stocks' ";
							break;
					}
					
					query += "ORDER BY C.price DESC";
				}
				
				resultSet = statement.executeQuery(query);
				
				// Display results of query
				int i = 1;
				System.out.println("\n");
				while(resultSet.next())
				{
					System.out.println(i + ". " + resultSet.getString(1) + " - " + resultSet.getString(2));
					System.out.println("\tCategory: " + resultSet.getString(3));
					System.out.println("\tDate of stock price: " + df.format(resultSet.getDate(4)));
					System.out.println("\tPrice of stock: $" + resultSet.getFloat(5));
					System.out.println("\tDescription: " + resultSet.getString(6) + "\n");
					i++;
				}
			}
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	public void searchMutualFunds()
	{
		try
		{
			PreparedStatement updateStatement;
			
			System.out.print("Enter (up to 2) keywords to search mutual funds by.\n"
					+ "Deliminate keywords by a space: ");
			String phrase = input.nextLine();
			
			if (phrase.trim().equals(""))
			{
				System.out.println("\nERROR! Please enter at least one keyword.\n");
				return;
			}
			
			String[] keywords = phrase.split(" ");
			
			// check if they entered too many keywords
			if (keywords.length > 2)
			{
				System.out.println("\nERROR! Please only enter two keywords.\n");
				return;
			}

			// one keyword
			if (keywords.length == 1)
			{
				query = "SELECT M.symbol, M.name, M.category, C.p_date, C.price, M.description "
						+ "FROM MUTUALFUND M JOIN CLOSINGPRICE C ON M.symbol = C.symbol "
						+ "WHERE M.description LIKE '%" + keywords[0] + "%' AND "
						+ "C.p_date = (SELECT MAX(p_date) FROM CLOSINGPRICE)";
			}
			// two keywords
			else
			{
				query = "SELECT M.symbol, M.name, M.category, C.p_date, C.price, M.description "
						+ "FROM MUTUALFUND M JOIN CLOSINGPRICE C ON M.symbol = C.symbol "
						+ "WHERE M.description LIKE '%" + keywords[0] + "%' AND "
						+ "M.description LIKE '%" + keywords[1] + "%' AND "
						+ "C.p_date = (SELECT MAX(p_date) FROM CLOSINGPRICE)";
			}
			
			resultSet = statement.executeQuery(query);
			
			// Display results of query
			int i = 1;
			System.out.println("\n");
			while(resultSet.next())
			{
				System.out.println(i + ". " + resultSet.getString(1) + " - " + resultSet.getString(2));
				System.out.println("\tCategory: " + resultSet.getString(3));
				System.out.println("\tDate of stock price: " + df.format(resultSet.getDate(4)));
				System.out.println("\tPrice of stock: $" + resultSet.getFloat(5));
				System.out.println("\tDescription: " + resultSet.getString(6) + "\n");
				i++;
			}
			
			if (i == 1)
			{
				System.out.println("\nSorry no mutual funds found with those keyword(s).\n");
			}
			
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	public void customerPortfolio()
	{
		try
		{
			PreparedStatement updateStatement;
			
			System.out.print("\nEnter a date to create portfolio against ('DD-MMM-YY'): ");
			String currDate = input.nextLine().toUpperCase();
			
			// check for invalid format for date
			if (!currDate.matches("\\d{2}-\\w{3}-\\d{2}"))
			{
				System.out.println("\nERROR! The date you entered was invalid format.\n");
				return;
			}
			
			query = "SELECT p_date FROM CLOSINGPRICE WHERE p_date = '" + currDate + "'";
			resultSet = statement.executeQuery(query);
			
			// quick check of if date is in database
			if (!resultSet.next())
			{
				System.out.println("\nERROR! The date you entered did not exist in database.\n");
				return;
			}
			
			query = "SELECT O.symbols, C.prices, O.shares FROM CLOSINGPRICE WHERE p_date = '" + currDate + "'";
			
			//For a specific date, the report will list the mutual funds the customer owns shares of:
			//their symbols, their prices, the number of shares, their current values on the specific
			//date, the cost value and the yield as well as the total value of the portfolio.
			
			ArrayList <String> ownedSymbols = new ArrayList<String>();
			ArrayList <Float> ownedPrices = new ArrayList<Float>();
			ArrayList <Integer> ownedShares = new ArrayList<Integer>();
			ArrayList <Float> currentValues = new ArrayList<Float>();
			ArrayList <Float> costValues = new ArrayList<Float>();
			ArrayList <Float> adjustedCosts = new ArrayList<Float>();
			ArrayList <Float> yields = new ArrayList<Float>();
			float totalValue = 0.0f;
			
			
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	public void investDeposit()
	{
		try
		{
			PreparedStatement updateStatement;
			
			System.out.print("\nPlease enter an amount to deposit for investment (-1 to cancel): ");
			float depositAmount = Float.parseFloat(input.nextLine());
			
			// check for cancel
			if (depositAmount == -1f)
			{
				return;
			}
			
			// check for a positive deposit amount
			if (depositAmount <= 0)
			{
				System.out.println("\nERROR! Please enter a postive deposit amount.\n");
				return;
			}
			
			// get the newest trans_id from TRXLOG
			int newTransID = 0;
			query = "SELECT MAX(trans_id) FROM TRXLOG";
			resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				newTransID = resultSet.getInt(1) + 1;
			}
			
			// INSERT deposit to TRXLOG
			query = "INSERT INTO TRXLOG VALUES (?,?,?,?,?,?,?,?)";
			updateStatement = connection.prepareStatement(query);
			
			updateStatement.setInt(1, newTransID);
			updateStatement.setString(2, username);
			updateStatement.setString(3, null);
			updateStatement.setDate(4, mutualDate);
			updateStatement.setString(5, "deposit");
			updateStatement.setString(6, null);
			updateStatement.setString(7, null);
			updateStatement.setFloat(8, depositAmount);
			
			updateStatement.executeUpdate();
			
			// trigger takes care of rest!
			
			System.out.println("\nAn amount of $" + depositAmount + " was invested into your preferences.");
			System.out.println("If you did not deposit enough to invest, the deposit was simply added to your account.\n");
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	public void buyShares()
	{
		try
		{
			PreparedStatement updateStatement;
			
			float sharePrice = 0.0f;
			float userBalance = 0.0f;
			
			System.out.print("\nEnter a stock symbol to buy: ('-' to quit): ");
			String symbolToBuy = input.nextLine().toUpperCase();
			
			if (symbolToBuy == "-")
			{
				return;
			}
			
			// get the stock's closing price				
			query = "SELECT price FROM "
			+ "MUTUALFUND M JOIN CLOSINGPRICE C ON M.symbol = C.symbol "
			+ "WHERE C.p_date = (SELECT MAX(p_date) FROM CLOSINGPRICE "
			+ "WHERE symbol = '" + symbolToBuy + "') AND C.symbol = '" + symbolToBuy + "'";
			
			resultSet = statement.executeQuery(query);
			
			// put price into variable
			if (resultSet.next())
			{
				sharePrice = resultSet.getFloat(1);
			}
			
			// symbol not found
			if (sharePrice == 0.0f)
			{
				System.out.println("\nERROR! That was not a valid symbol\n");
				return;
			}
			
			// get the stock's current preferences				
			query = "SELECT balance FROM CUSTOMER WHERE login = '" + username + "'";
			resultSet = statement.executeQuery(query);
			
			// put balance into variable
			if (resultSet.next())
			{
				userBalance = resultSet.getFloat(1);
			}
			
			// check if they even have enough for 1 share
			if (userBalance < sharePrice)
			{
				System.out.println("\nERROR! Your balance was not enough for even 1 share :(.\n");
				return;
			}
			
			System.out.println("\nThe symbol " + symbolToBuy + " is at $" + sharePrice
					+ "/share. Your current balance is $" + userBalance + ".\n");
			System.out.print("Would you like to buy shares by:"
					+ "\n(s) shares or (b) balance? (anything else to quit): ");
			char buyType = input.nextLine().toLowerCase().charAt(0);
			
			// buy by stock price ( number of shares)
			if (buyType == 's')
			{
				
				int maxShares = (int)(userBalance/sharePrice);
				
				System.out.print("\nHow many shares of " + symbolToBuy + " would you like?\n"
						+ "You can buy a maximum of " + maxShares + " shares: ");
				int numToBuy = Integer.parseInt(input.nextLine());
				
				// check if they can buy that many shares
				if (numToBuy > maxShares)
				{
					System.out.println("\nERROR! You do not have a high enough balance to buy that many.\n");
					return;
				}
				
				// check if they want to buy any shares
				if (numToBuy <= 0)
				{
					System.out.println("\nSorry you didn't want to buy any shares.\n");
					return;
				}
				
				// get the newest trans_id from TRXLOG
				int newTransID = 0;
				query = "SELECT MAX(trans_id) FROM TRXLOG";
				resultSet = statement.executeQuery(query);
				if (resultSet.next())
				{
					newTransID = resultSet.getInt(1) + 1;
				}
				
				// INSERT buy to TRXLOG
				query = "INSERT INTO TRXLOG VALUES (?,?,?,?,?,?,?,?)";
				updateStatement = connection.prepareStatement(query);
				
				updateStatement.setInt(1, newTransID);
				updateStatement.setString(2, username);
				updateStatement.setString(3, symbolToBuy);
				updateStatement.setDate(4, mutualDate);
				updateStatement.setString(5, "buy");
				updateStatement.setInt(6, numToBuy);
				updateStatement.setFloat(7, sharePrice);
				updateStatement.setFloat(8, (numToBuy*sharePrice));
				
				updateStatement.executeUpdate();
				
				// add shares to OWNS
				query = "SELECT * FROM OWNS WHERE login = '" + username
						+ "' AND symbol = '" + symbolToBuy + "'";
				resultSet = statement.executeQuery(query);
				
				// user does not own any of these yet, INSERT
				if (!resultSet.next())
				{
					query = "INSERT INTO OWNS VALUES (?,?,?)";
					updateStatement = connection.prepareStatement(query);
					
					updateStatement.setString(1, username);
					updateStatement.setString(2, symbolToBuy);
					updateStatement.setInt(3, numToBuy);
					
					updateStatement.executeUpdate();
				}
				// user already owns some shares, UPDATE
				else
				{
					query = "UPDATE OWNS SET shares = shares + ? "
							+ " WHERE login = ? AND SYMBOL = ?";
					updateStatement = connection.prepareStatement(query);
					
					updateStatement.setInt(1, numToBuy);
					updateStatement.setString(2, username);
					updateStatement.setString(3, symbolToBuy);
					
					updateStatement.executeUpdate();
				}
				
				//subtract transaction from customer balance
				query = "UPDATE CUSTOMER SET balance = balance - ? "
						+ " WHERE login = ?";
				updateStatement = connection.prepareStatement(query);
				
				updateStatement.setFloat(1, (numToBuy*sharePrice));
				updateStatement.setString(2, username);
				
				updateStatement.executeUpdate();
				
			}
			
			// buy by balance
			else if (buyType == 'b')
			{
				System.out.print("\nHow much of your balance would you like to spend?\n"
						+ "Reminder your balance is at $" + userBalance + ": ");
				float balanceToSpend = Float.parseFloat(input.nextLine());
				
				// check if they have enough balance
				if (balanceToSpend > userBalance)
				{
					System.out.println("\nERROR! You do not have that much balance.\n");
					return;
				}
				
				// they didn't want to spend money
				if (balanceToSpend <= 0.0f)
				{
					System.out.println("\nSorry you didn't want to buy any shares.\n");
					return;
				}
				
				int numToBuy = (int)(balanceToSpend/sharePrice);
				
				System.out.println("\nYou bought " + numToBuy + " shares with that much money!\n");
				
				// get the newest trans_id from TRXLOG
				int newTransID = 0;
				query = "SELECT MAX(trans_id) FROM TRXLOG";
				resultSet = statement.executeQuery(query);
				if (resultSet.next())
				{
					newTransID = resultSet.getInt(1) + 1;
				}
				
				// INSERT buy to TRXLOG
				query = "INSERT INTO TRXLOG VALUES (?,?,?,?,?,?,?,?)";
				updateStatement = connection.prepareStatement(query);
				
				updateStatement.setInt(1, newTransID);
				updateStatement.setString(2, username);
				updateStatement.setString(3, symbolToBuy);
				updateStatement.setDate(4, mutualDate);
				updateStatement.setString(5, "buy");
				updateStatement.setInt(6, numToBuy);
				updateStatement.setFloat(7, sharePrice);
				updateStatement.setFloat(8, (numToBuy*sharePrice));
				
				updateStatement.executeUpdate();
				
				// add shares to OWNS
				query = "SELECT * FROM OWNS WHERE login = '" + username
						+ "' AND symbol = '" + symbolToBuy + "'";
				resultSet = statement.executeQuery(query);
				
				// user does not own any of these yet, INSERT
				if (!resultSet.next())
				{
					query = "INSERT INTO OWNS VALUES (?,?,?)";
					updateStatement = connection.prepareStatement(query);
					
					updateStatement.setString(1, username);
					updateStatement.setString(2, symbolToBuy);
					updateStatement.setInt(3, numToBuy);
					
					updateStatement.executeUpdate();
				}
				// user already owns some shares, UPDATE
				else
				{
					query = "UPDATE OWNS SET shares = shares + ? "
							+ " WHERE login = ? AND SYMBOL = ?";
					updateStatement = connection.prepareStatement(query);
					
					updateStatement.setInt(1, numToBuy);
					updateStatement.setString(2, username);
					updateStatement.setString(3, symbolToBuy);
					
					updateStatement.executeUpdate();
				}
				
				//update customer balance
				query = "UPDATE CUSTOMER SET balance = balance - ? "
						+ " WHERE login = ?";
				updateStatement = connection.prepareStatement(query);
				
				updateStatement.setFloat(1, (numToBuy*sharePrice));
				updateStatement.setString(2, username);
				
				updateStatement.executeUpdate();
			}
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	public void sellShares()
	{
		try
		{
			PreparedStatement updateStatement;
			ArrayList<String> symbolsOwned = new ArrayList<String>();
			String symbolToSell;
			
			// get the shares they currently own				
			query = "SELECT symbol FROM OWNS WHERE login = '" + username + "'";
			resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				symbolsOwned.add(resultSet.getString(1));
			}
			
			// check if they even own any symbols
			if (symbolsOwned.size() == 0)
			{
				System.out.println("\nERROR! You do not have any shares to sell!\n");
				return;
			}
			
			// get symbol user wants to sell
			System.out.print("\nYou currently own shares of symbols:  ");
			for (String sym : symbolsOwned)
			{
				System.out.print(sym + "  ");
			}
			System.out.print("\nWhich symbol would you like to sell? ");
			symbolToSell = input.nextLine().toUpperCase();
			
			// check if they own symbol they want to sell
			if (!symbolsOwned.contains(symbolToSell))
			{
				System.out.println("\nERROR! You do not own any shares of that symbol!\n");
				return;
			}
			
			int numSharesOwned = 0;
			// get the number of shares they own of symbol
			query = "SELECT shares FROM OWNS WHERE login = '" + username
					+ "' AND symbol = '" + symbolToSell + "'";
			resultSet = statement.executeQuery(query);
			if(resultSet.next())
			{
				numSharesOwned = resultSet.getInt(1);
			}
			
			System.out.print("\nYou currently own " + numSharesOwned + " shares of symbol "
					+ symbolToSell + ".\nHow many would you like to sell? ");
			int numToSell = Integer.parseInt(input.nextLine());
			
			// check if they have the amount of shares they want to sell
			if (numToSell > numSharesOwned)
			{
				System.out.println("\nERROR! You do not own that many shares of that symbol!\n");
				return;
			}
			
			// check if they want to sell any shares
			if (numToSell <= 0)
			{
				System.out.println("\nSorry you didn't want to sell any shares.\n");
				return;
			}
			
			// good to sell
			
			// get the stock's closing price				
			query = "SELECT price FROM CLOSINGPRICE WHERE symbol = '" + symbolToSell
					+ "' AND p_date = (SELECT MAX(p_date) FROM CLOSINGPRICE "
					+ "WHERE symbol = '" + symbolToSell + "')";
			
			resultSet = statement.executeQuery(query);
			
			float sharePrice = 0.0f;
			// put price into variable
			if (resultSet.next())
			{
				sharePrice = resultSet.getFloat(1);
			}
			
			// get the newest trans_id from TRXLOG
			int newTransID = 0;
			query = "SELECT MAX(trans_id) FROM TRXLOG";
			resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				newTransID = resultSet.getInt(1) + 1;
			}
			
			// INSERT buy to TRXLOG
			query = "INSERT INTO TRXLOG VALUES (?,?,?,?,?,?,?,?)";
			updateStatement = connection.prepareStatement(query);
			
			updateStatement.setInt(1, newTransID);
			updateStatement.setString(2, username);
			updateStatement.setString(3, symbolToSell);
			updateStatement.setDate(4, mutualDate);
			updateStatement.setString(5, "sell");
			updateStatement.setInt(6, numToSell);
			updateStatement.setFloat(7, sharePrice);
			updateStatement.setFloat(8, (numToSell*sharePrice));
			
			updateStatement.executeUpdate();
			
			// trigger takes care of rest!
			
			System.out.println("\nYou have sold " + numToSell + " shares of "
					+ symbolToSell + " at $" + sharePrice + "/share, for a total of $"
							+ (numToSell*sharePrice) + "!\n");
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}
	
	
	public void allocationChange()
	{
		try
		{
			PreparedStatement updateStatement;
			
			// get the user's current preferences						
			query = "SELECT symbol, percentage, p_date FROM "
			+ "PREFERS P JOIN ALLOCATION A ON P.allocation_no = A.allocation_no "
			+ "WHERE P.allocation_no = (SELECT MAX(allocation_no) FROM ALLOCATION "
			+ "WHERE login = '" + username + "')";
			
			resultSet = statement.executeQuery(query);
			
			java.sql.Date lastUpdate = null;
			
			// output current preferences
			System.out.println("\nYour current allocation preferences:\n");
			System.out.format("%20s %20s\n", "Symbols", "Percentage:");
			while (resultSet.next()) {
				System.out.format("%20s %20s\n",resultSet.getString(1), resultSet.getString(2));
				lastUpdate = resultSet.getDate(3);
			}
			
			// check lastUpdate vs currentDate
			
			System.out.print("\nWould you like to update these preferences? (y/n): ");
			char yOrN = input.nextLine().toLowerCase().charAt(0);
			
			// if user wants to update preferences
			if (yOrN == 'y')
			{
				ArrayList<String> newSymbols = new ArrayList<String>();
				ArrayList<Float> newPercentages = new ArrayList<Float>();
				
				String tempSymbol = "";
				float tempFloat = 0.0f;
				
				boolean successfulInput = false;
				
				// loop until the user is at 100% or cancels
				while (!tempSymbol.equals("-"))
				{
					System.out.print("\nEnter the symbol of one of your preferences ('-' to quit): ");
					tempSymbol = input.nextLine().toUpperCase();
					if (newSymbols.contains(tempSymbol))
					{
						System.out.println("\nYou have already entered that symbol!");
						continue;
					}
					else if (tempSymbol.equals("-"))
					{
						break;
					}
					else
					{
						newSymbols.add(tempSymbol);
						
						float sum = 0.0f;
						for(Float f : newPercentages)
						    sum += f;
						
						System.out.format("Enter a percentage for this symbol (-1 to quit):\n"
								+ "(all percentages must sum to 1.00, currently at %.2f out of 1.00): ", sum);
						tempFloat = Float.parseFloat(input.nextLine());
						
						if (tempFloat == -1.0)
						{
							break;
						}
						
						if (sum + tempFloat > 1.0)
						{
							System.out.println("\n\nERROR! You have exceed the maximum of 100 percent!\n");
							break;
						}
						
						newPercentages.add(tempFloat);
						
						if (sum + tempFloat == 1.0)
						{
							successfulInput = true;
							break;
						}
					}
				}							
				
				// attempt to add to database
				if (successfulInput == true)
				{
					
					// get the new max alloc num
					int allocNum = 0;
					query = "SELECT MAX(allocation_no) FROM ALLOCATION";
					resultSet = statement.executeQuery(query);
					if (resultSet.next())
					{
						allocNum = resultSet.getInt(1) + 1;
					}
					
					// add new alloc_num to ALLOCATION
					query = "INSERT INTO ALLOCATION VALUES (?,?,?)";
					updateStatement = connection.prepareStatement(query);
					
					updateStatement.setInt(1, allocNum);
					updateStatement.setString(2, username);
					updateStatement.setDate(3, mutualDate);
					
					updateStatement.executeUpdate();
					
					query = "INSERT INTO PREFERS VALUES (?,?,?)";
					updateStatement = connection.prepareStatement(query);
					
					// loop through and add new symbols to PREFERS
					for (int i = 0; i < newSymbols.size(); i++)
					{
						updateStatement.setInt(1, allocNum);
						updateStatement.setString(2, newSymbols.get(i));
						updateStatement.setFloat(3, newPercentages.get(i));
						
						updateStatement.executeUpdate();
					}
					
				}
				
			}
		}
		catch (java.sql.SQLIntegrityConstraintViolationException ic)
		{
			System.out.println("\n\nOops! There was an integrity contraint violated!\n");
			//System.out.println("" + ic.getMessage());
		}
		catch (java.lang.NumberFormatException num)
		{
			System.out.println("\n\nOops! That was the wrong number format!\n");
		}
		catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
	}

}