/*
    Here is an example of connecting to a database using jdbc

    The table we will use in the example is
    Table Test(
       name     varchar(30),
       ssn      number(10),
       bday     date
    );
    
    For demostratration purpose, insert two records into this table:
    ( 'Mike', 123456789, '09/Nov/03' )
    ( 'Amy', 987654321, '10/Nov/03' )

    Written by: Jonathan Beaver, modified by Thao Pham
    Purpose: Demo JDBC for CS1555 Class

    IMPORTANT (otherwise, your code may not compile)	
    Same as using sqlplus, you need to set oracle environment variables by 
    sourcing bash.env or tcsh.env
 */

import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
//needed by java for database connection and manipulation

public class BetterFuture
{
	private Connection connection; // used to hold the jdbc connection to the DB
	private static Statement statement; // used to create an instance of the connection
	private static ResultSet resultSet; // used to hold the result of your query (if
									// one exists)
	private static String query; // this will hold the query we are using
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
			// Register the oracle driver. This needs the oracle files provided
			// in the oracle.zip file, unzipped into the local directory and
			// the class path set to include the local directory
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			// This is the location of the database. This is the database in
			// oracle
			// provided to the class
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
		
		input = new Scanner(System.in);
		
		try
		{
			statement = connection.createStatement(); //create an instance
		}
		catch (Exception Ex)
		{
			System.out.println("Error running the sample queries.  Machine Error: " +
		            Ex.toString());
		}
		
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
			System.out.println("\na: Add New Mutual Fund");
			System.out.println("n: New Customer Registration");
			System.out.println("q: Update Share Quotes");
			System.out.println("s: Statistics");
			System.out.println("u: Update Time and Date");
			System.out.print("Please select one of the above actions: ");
			
			// get first letter of input in lower case
			action = input.nextLine().toLowerCase().charAt(0);
			
			switch (action)
			{
				case 'a':
					break;
				case 'n':
					break;
				case 'q':
					break;
				case 's':
					break;
				case 'u':
					break;
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
					break;
				case 'b':
					break;
				case 'i':
					break;
				case 'm':
					break;
				case 'p':
					break;
				case 's':
					break;
			}
		}
		
		
		// close the connection
		try
		{
			connection.close();
		}
		catch (Exception Ex)
		{
			System.out.println("Error running the sample queries.  Machine Error: " +
		            Ex.toString());
		}
	}

	public static void main(String args[])
	{
		BetterFuture better_future_session = new BetterFuture();
	}
	
	public static int attemptLogin(String user, String pass)
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