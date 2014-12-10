/*
 * Anthony Raehn and Corey Cleric
 * CS1555
 * Wild Cats
 * BetterFuture
 */

import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
//needed by java for database connection and manipulation

public class BetterFuture
{
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
	
	
	public BetterFuture()
	{
		/*
		 * Making a connection to a DB causes certain exceptions. In order to
		 * handle these, you either put the DB stuff in a try block or have your
		 * function throw the Execptions and handle them later. For this demo I
		 * will use the try blocks
		 */
		pitt_username = "atr13"; // This is your username in oracle
		pitt_password = "3612340"; // This is your password in oracle
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

			connection = DriverManager.getConnection(url, pitt_username, pitt_password);
			// create a connection to DB on class3.cs.pitt.edu
		}
		catch (Exception Ex) // What to do with any exceptions
		{
			System.out.println("Error connecting to database.  Machine Error: "
					+ Ex.toString());
			Ex.printStackTrace();
		}
		//connected to database, do login stuff now:
		
		try
		{
			statement = connection.createStatement(); //create an instance
			
			input = new Scanner(System.in);
			
			/*** Welcome Message ***/
			System.out.println("Welcome to Better Future's Financial System!\n");
			
			/*** User/Admin Login ***/
			boolean loginSuccess = false;
			boolean isAdmin = false;
			
			System.out.println("To get started, please login to the system (admin or user).");
			// loop until a user or admin logs in to the system
			while (!loginSuccess)
			{
				// get username
				System.out.print("Username: ");
				username = input.nextLine();
				
				//get password
				System.out.print("Password: ");
				password = input.nextLine();
				
				int loginInfo = attemptLogin(username, password);
				
				// login failed
				if (loginInfo == 0)
				{
					System.out.println("\nERROR: Login unsuccesful. Please try again.\n");
					loginSuccess = false;
				}
				
				// user logged in
				else if (loginInfo == 1)
				{
					loginSuccess = true;
					isAdmin = false;
				}
				
				// admin logged in
				else if (loginInfo == 2)
				{
					loginSuccess = true;
					isAdmin = true;
				}
			}
			
			// if they get to this point, successfully logged in!
			
			char action;
			/*** if the user is an ADMIN ***/
			if (isAdmin == true)
			{
				String str;
				java.sql.Date date;
				float fl;
			    PreparedStatement updateStatement;
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yy");
				boolean done = false;
				while(!done){
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
					
					switch (action)
					{
						case 'a':
							//add new mutual fund
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
							System.out.print("Enter a mutual fund date (format: dd-MMM-yy): ");
							str = input.nextLine();
						    date = new java.sql.Date (df.parse(str).getTime());
						    updateStatement.setDate(5, date);
							
						    updateStatement.executeUpdate();
							
							//check if INSERT applied
							System.out.println("\nNew table result:");
							resultSet = mutualFund();
							while(resultSet.next()){
								System.out.println(""+
										resultSet.getString(1) + ", " +
										resultSet.getString(2) + ", " +
										resultSet.getString(3) + ", " +
										resultSet.getString(4) + ", " +
										resultSet.getDate(5));
							}
							break;
						case 'n':
							//new customer registration
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
							
							//check if INSERT applied
							System.out.println("\nNew table result:");
							resultSet = customer();
							while(resultSet.next()){
								System.out.println(""+
										resultSet.getString(1) + ", " +
										resultSet.getString(2) + ", " +
										resultSet.getString(3) + ", " +
										resultSet.getString(4) + ", " +
										resultSet.getString(5) + ", " +
										resultSet.getFloat(6));
							}
							break;
						case 's':
							//update share quotes
							break;
						case 't':
							//statistics
							break;
						case 'd':
							//update time and date
							break;
						case 'q':
							done = true;
							break;
					}
				}
			}
			
			/*** if the user is a CUSTOMER ***/
			if (isAdmin == false)
			{
				System.out.println("\na: Change Allocation Preferrence");
				System.out.println("b: Buy Shares");
				System.out.println("f: Browse Mutual Funds");
				System.out.println("i: Investing");
				System.out.println("m: Search Mutual Fund by Text");
				System.out.println("p: Check Portfolio");
				System.out.println("s: Sell Shares");
				System.out.print("Please select one of the above actions: ");
				
				// get first letter of input in lower case
				action = input.nextLine().toLowerCase().charAt(0);
				
				switch (action)
				{
					case 'a':
						//allocation preferences
						break;
					case 'b':
						//buy shares
						break;
					case 'f':
						//browse mutual funds
						break;
					case 'i':
						//investing
						break;
					case 'm':
						//search mutual funds by text
						break;
					case 'p':
						//check portfolio
						break;
					case 's':
						//sell shares
						break;
				}
			}
			//done, close the connection
			connection.close();
			System.out.println("Connection closed.");
		}
		catch (Exception Ex)
		{
			System.out.println("Machine Error: " +
		            Ex.toString());
		}
	}

	public static void main(String args[])
	{
		BetterFuture better_future_session = new BetterFuture();
	}

	public ResultSet mutualFund() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM MUTUALFUND");
	}
	public ResultSet closingPrice() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM CLOSINGPRICE");
	}
	public ResultSet customer() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM CUSTOMER");
	}
	public ResultSet administrator() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM ADMINISTRATOR");
	}
	public ResultSet allocation() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM ALLOCATION");
	}
	public ResultSet prefers() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM PREFERS");
	}
	public ResultSet trxlog() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM TRXLOG");
	}
	public ResultSet owns() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM OWNS");
	}
	public ResultSet mutualDate() throws Exception{
		statement = connection.createStatement(); //create an instance
		return statement.executeQuery("SELECT * FROM MUTUALDATE");
	}
	public int attemptLogin(String user, String pass)
	{
		try
		{
			// query CUSTOMER table
			query = "SELECT * FROM CUSTOMER WHERE login = '" + user + "' AND password = '" + pass + "'";
			
			resultSet = statement.executeQuery(query);
			
			// user is a CUSTOMER, successful login
			if (resultSet.next())
			{
				// Welcome back message
				System.out.println("\nWecome back, user " + resultSet.getString(2) + "!\n");
				return 1;
			}
			else
			{
				query = "SELECT * FROM ADMINISTRATOR WHERE login = '" + user + "' AND password = '" + pass + "'";
				
				resultSet = statement.executeQuery(query);
				
				// user is an ADMINISTRATOR, successful login
				if (resultSet.next())
				{
					// Welcome back message
					System.out.println("\nWecome back, admin " + resultSet.getString(2) + "!\n");
					return 2;
				}
				
				// failed login
				else
				{
					return 0;
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("\nException error! : " + ex.toString() + "\n");
		}
		
		// failed login
		return 0;
	}
	
}