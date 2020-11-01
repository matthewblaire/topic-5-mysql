package employeesDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;
public class App {
	private static Connection conn;
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		final String connectionStr = "jdbc:mysql://localhost:3306/foods";
		
		try { 
			System.out.println("connecting to db");
			conn = DriverManager.getConnection(connectionStr, "root", "34923628!Ad"); // connect to database. connectionStr, username, password
			System.out.println("Successfully connected to DB\n");
			
			//create the table
			createFoodsTable();
			// start the menu
			startMenu();
			
		} catch (SQLException e) { // this happens if the connection fails for any reason
			System.out.println("unable to connect to db");
			e.printStackTrace();
		} finally { //close the connection after everything is done
			if (conn != null) {
				System.out.println("closing db connection");
				conn.close();
				System.out.println("successfully closed connection");
			}	
		}
		

	}
	
	public static void startMenu() {
		
		boolean working = true; // used to keep app running until user exits
		
		while (working) {
			System.out.println("\nWelcome to the foods table interaction application.");
			System.out.println("Please pick and enter one option. (1-4)");
			System.out.println("1. Show all foods");
			System.out.println("2. Insert new food");
			System.out.println("3. Find food by name");
			System.out.println("4. Update calories by food name");
			System.out.println("5. Delete food by name");
			System.out.println("\n6. Exit\n");
			
			Scanner input = new Scanner(System.in); //scanner to read user input
			int userChoice = input.nextInt(); // get the integer input from user
			
			
			switch (userChoice) { // logic to run the part of the app the user requested
			case 1: // Show all foods
				showAllFoods();
				break;
			case 2: // Insert new food
				insertNewFood();
				break;
			case 3: // Find food by name
				findFoodByName();
				break;
			case 4: // Update calories by food name
				updateCaloriesByFoodName();
				break;
			case 5: // Delete food by name
				deleteFoodByName();
				break;
			case 6: // Exit
				working = false;
				break;
			}
			
		}
	}
	
	public static void createFoodsTable() { // This method creates a table of foods in the mysql database with two columns, food_name and calories
		
		System.out.println("Creating foods table...");
		final String query = "CREATE TABLE IF NOT EXISTS foods ( food_name varchar(20) not null, calories int not null, primary key (food_name) )"; // query to create the table
		
		try {
			Statement ps = conn.createStatement(); //prepare the statement for update
			
			ps.executeUpdate(query); // execute the update
			
		} catch (SQLException e) {	
			System.out.println("Error occurred in createFoodsTable()");
			e.printStackTrace();
		}
		System.out.println("foods table created successfully");
	}

	public static void showAllFoods() { // This method shows all foods in the table using a SELECT query
		
		System.out.println("\nShowing all foods...\n");
		final String query = "select * from foods limit 100"; // SELECT query for showing all foods in table
		
		try {
			Statement s = conn.createStatement(); // create statement. Don't need a prepared statement 
			
			ResultSet rs = s.executeQuery(query); // execute the query
			
			System.out.println("Food Name | Calorie Count");
			while (rs.next()) { //loop through all of the results from the query
				System.out.println(rs.getString("food_name") + " | " + rs.getInt("calories")); 
			}
			
		} catch (SQLException e) {	
			System.out.println("Error occurred in showAllFoods()");
			e.printStackTrace();
		}
		System.out.println("\nRetrieved all foods\n");
	}
	
	public static void insertNewFood() { // This method lets the user insert a new food into the table
		Scanner input = new Scanner(System.in); // scanner for getting input from user
		
		System.out.println("\nINSERT NEW FOOD\n");
		System.out.println("Please enter the name of your new food:");
		String food_name = input.nextLine();	// Get food name from user
		System.out.println("Please enter the calorie count of your new food:");
		int calories = input.nextInt(); // Get calorie count from user
		
		final String query = "INSERT IGNORE INTO foods VALUES(?, ?)"; // query to insert new food, using wild cards instead of concatenation 
																		// using INSERT IGNORE because I read online it will stop MySQL from throwing an error
																		// when a primary key already exists with a certain value
		try {
			PreparedStatement ps = conn.prepareStatement(query); //prepare the statement for update
			ps.setString(1, food_name);
			ps.setInt(2, calories);
			ps.executeUpdate(); // execute the update
			System.out.println("\nFood inserted successfully.\n");
		} catch (SQLException e) {	
			System.out.println("Error occurred in insertNewFood()");
			e.printStackTrace();
		}
		
		
	}
	
	public static void findFoodByName() {
		Scanner input = new Scanner(System.in); // Create scanner object used to get input from user
		final String query = "select * from foods where food_name = ?"; // prepare select query
		
		System.out.println("\nFIND FOOD BY NAME\n");
		System.out.println("Please enter the name of the food you wish to search for:");
		String food_name = input.nextLine();
		
		System.out.println("Food Name | Calorie Count");
	
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, food_name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("food_name") + " | " + rs.getInt("calories"));
			}
			
		} catch (SQLException e) {
			System.out.println("Error occurred in findFoodByName()");
			e.printStackTrace();
		}
		
		
	}
	
	public static void updateCaloriesByFoodName() { // This method lets the user insert a new food into the table
		Scanner input = new Scanner(System.in); // scanner for getting input from user
		
		System.out.println("\nUPDATE FOOD BY NAME\n");
		System.out.println("Please enter the name of the food you wish to update:");
		String food_name = input.nextLine();	// Get food name from user
		System.out.println("Please enter the new calorie count of your food:");
		int calories = input.nextInt(); // Get calorie count from user
		
		final String query = "update foods set calories = ? where food_name = ?"; // query to update calories by food name
		try {
			PreparedStatement ps = conn.prepareStatement(query); //prepare the statement for update
			ps.setInt(1, calories);
			ps.setString(2, food_name);
			ps.executeUpdate(); // execute the update
			System.out.println("\nFood updated successfully.\n");
		} catch (SQLException e) {	
			System.out.println("Error occurred in insertNewFood()");
			e.printStackTrace();
		}
		
		
	}	
	
	public static void deleteFoodByName() {
		Scanner input = new Scanner(System.in); // scanner for getting input from user
		
		System.out.println("\nDELETE FOOD BY NAME\n");
		System.out.println("Please enter the name of the food you wish to delete:");
		String food_name = input.nextLine();	// Get food name from user

		
		final String query = "delete from foods where food_name = ?"; // query to delete by food name
		try {
			PreparedStatement ps = conn.prepareStatement(query); //prepare the statement for table update
			ps.setString(1, food_name);
			ps.executeUpdate(); // execute the table update (in this case, delete)
			System.out.println("\nFood deleted successfully.\n");
		} catch (SQLException e) {	
			System.out.println("Error occurred in insertNewFood()");
			e.printStackTrace();
		}
	}
	
	
}
