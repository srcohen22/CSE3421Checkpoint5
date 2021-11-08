package osu.cse3241;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

class App {
	
	// Static database connection
	public static CSE3241app db = new CSE3241app();
	
	// Constructor for App class
	public App () {
	}
	
	// Main method, does all the user input/output stuff
	public static void main(String[] args) {
		
		// While loop flag, true by default, set to false on user exit command
		boolean flag = true;
		
		// Input scanner
		Scanner in = new Scanner(System.in);
		
		// Create new App class
		App app = new App();
		
		// Initialize the database, get connection
		Connection conn = db.initializeDB("Library.db");
		
		// Initial main menu screen
		System.out.println("Welcome to the Library Application! \n"
						+ "Please select one of the following options:");
		
		while (flag) {
			System.out.println("a. Search");
			System.out.println("b. Add new records");
			System.out.println("c. Order/Activate items");
			System.out.println("d. Edit records");
			System.out.println("e. Useful Reports");
			System.out.println("f. Quit");
			
			// Get and handle user's choice
			String choice = in.nextLine();
			switch (choice) {
			case "a":
				app.search(in, conn);
				break;
			case "b":
				app.addRecord(in, conn);
				break;
			case "c":
				app.orderItem(in, conn);
				break;
			case "d":
				app.editRecord(in, conn);
				break;
			case "e":
				app.usefulReport(in, conn);
				break;
			case "f":
				System.out.println("Thank you for using the Library"
						+ " Mangement System");
				flag = false;
				break;
			default:
				System.out.println("Please choose either 'a', 'b', 'c', 'd', "
						+ "'e', " + "or 'f'!");
			}
			System.out.println("------------------------------------");
		}
		
		// Close the input stream
		in.close();
	}
	
	/**
	 * Prompts user for artist vs track search, returns info
	 * @param conn 
	 * 
	 * @param in: input scanner
	 */
	public void search(Scanner in, Connection conn) {
		System.out.println("What would you like to search for?");
		System.out.println("a. Artist");
		System.out.println("b. Tracks");
		
		String choice = in.nextLine();
		if (choice.equals("a")) {
			System.out.println("Artist name to search for: ");
			String name = in.nextLine();
			this.searchArtist(conn, name);
		} else if (choice.equals("b")) {
			System.out.println("Track name to search for: ");
			String name = in.nextLine();
			this.searchTrack(conn, name);
		} else {
			System.out.println("Not a valid choice...");
		}
	}
	
	/**
	 * Searches for artist given by name from database through conn
	 * @param conn
	 * @param name
	 */
	public void searchArtist(Connection conn, String name) {
		// Make a prepared sql string for the query
		String sql = "SELECT name, numb_grammys FROM Artist WHERE name=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			db.sqlQuery(conn, ps);
		} catch (SQLException e) {
			System.err.println("Error in searching for artist!");
			System.exit(0);
		}
	}
	
	/**
	 * Searches for track given by name from database through conn
	 * @param conn
	 * @param name
	 */
	public void searchTrack(Connection conn, String name) {
		// Make a prepared sql string for the query
		// Make a prepared sql string for the query
		String sql = "SELECT album_name, name, genre, year, length FROM "
				+ "Track WHERE name=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			db.sqlQuery(conn, ps);
		} catch (SQLException e) {
			System.err.println("Error in searching for track!");
			System.exit(0);
		}
	}
	
	/**
	 * Prompts user for adding either artist or track info, and adds it to App
	 * @param conn 
	 * 
	 * @param in: input scanner
	 */
	public void addRecord(Scanner in, Connection conn) {
		
		System.out.println("What would you like to add?");
		System.out.println("a. Artist");
		System.out.println("b. Track");
		
		String choice = in.nextLine();
		
		if (choice.equals("a")) {
			this.addArtist(in, conn);
		} else if (choice.equals("b")) {
			// this.tracks.add(addTrack(in));
		} else {
			System.out.println("Not a valid choice...");
		}
	}
	
	/**
	 * Prompts the user for new artist info, and returns a new Artist object
	 * 
	 * @param in: input scanner
	 * @return new Artist the user wants to add
	 */
	private void addArtist(Scanner in, Connection conn) {
		System.out.println("Please enter the Artist name:");
		String name = in.nextLine();
		System.out.println("Please enter the # of Grammys the artist has won:");
		int grammys = in.nextInt();
		
		String sql = "INSERT INTO ARTIST VALUES (name=?, numb_grammys=?);";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, grammys);
			db.sqlQuery(conn, ps);
		} catch (SQLException e) {
			System.err.println("Error adding artist to database!");
			System.exit(0);
		}
	}
	
	/**
	 * Asks user to enter new movie order info, adds it to app
	 * @param conn 
	 * 
	 * @param in: input scanner
	 */
	public void orderItem(Scanner in, Connection conn) {
		System.out.println("Please enter the movie name:");
		String name = in.nextLine();
		System.out.println("# of copies to purchase:");
		int num = in.nextInt(); in.nextLine(); // Consume \n token
		System.out.println("Price of the movie:");
		double price = in.nextDouble(); in.nextLine(); // Consume \n token
		System.out.println("Estimated arrival date:");
		String date = in.nextLine();
		
		// this.movies.add(new Movie(name, num, price, date));
	}
	
	/**
	 * Allows user to edit an artist's info
	 * @param conn 
	 * 
	 * @param in: input scanner
	 */
	public void editRecord(Scanner in, Connection conn) {
		System.out.println("Please search for an artist to edit!");
		String name = in.nextLine();
		
		// Make a prepared statement for the searching artist query
		
		this.searchArtist(conn, name);
		
		System.out.println("Is this the artist you'd like to edit? (y/n)");
		String choice = in.nextLine();
		
		// Changing the artist info
		if (choice.equals("y")) {
			// Prompt user for changes
			System.out.println("Would you like to edit the name? "
					+ "(y/n)");
			if (in.nextLine().equals("y")) {
				System.out.println("New name: ");
				String newName = in.nextLine();
				
				// Run the update query
				String sql = "UPDATE Artist SET name=? WHERE name=?";
				try {
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, newName);
					ps.setString(2, name);
					db.sqlQuery(conn, ps);
				} catch (SQLException e) {
					System.err.println("Error editing the database!");
					System.exit(0);
				}
				
			}
			
			System.out.println("Would you like to edit the # of "
					+ "grammys won? (y/n)");
			if (in.nextLine().equals("y")) {
				System.out.println("New # Grammys Won: ");
				int newGrammy = in.nextInt(); in.nextLine();
				
				// Do update query for grammys
				String sql = "UPDATE Artist SET numb_grammys=? WHERE name=?";
				try {
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, newGrammy);
					ps.setString(2, name);
					db.sqlQuery(conn, ps);
				} catch (SQLException e) {
					System.err.println("Error editing the database!");
					System.exit(0);
				}
			}
		} else {
			System.out.println("Returning to main menu...");
		}
		
	}
	
	/**
	 * Not implemented yet, just prints prompts
	 * @param conn 
	 * 
	 * @param in: input scanner
	 */
	public void usefulReport(Scanner in, Connection conn) {
		System.out.println("Select a Report! (Not implemented yet!)");
		System.out.println("a. Tracks by ARTIST released before YEAR");
		System.out.println("b. Number of albums checked out by a "
				+ "single patron");
		System.out.println("c. Most popular actor in the database");
		System.out.println("d. Most listened to artist in the database");
		System.out.println("e. Patron who has checked out the most videos");
		
		String choice = in.nextLine();
		switch (choice) {
		case "a":
			// Ask for artist and year
			System.out.println("Please enter Artist name: ");
			String artist_name = in.nextLine();
			System.out.println("What year? ");
			int year = in.nextInt(); in.nextLine();
			
			// Build query
			String sql = "SELECT T.Name, T.Album_Name, T.Year, A.Artist_Name, A.Name "
					+ "FROM Track AS T, ALBUM as A "
					+ "WHERE A.Artist =? AND A.Name = T.Album_Name AND T.Year<? "
					+ "GROUP BY T.Name";
			
			// Build PreparedStatement
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, artist_name);
				ps.setInt(2, year);
				db.sqlQuery(conn, ps);
			} catch (SQLException e) {
				System.err.println("Error searching the database!");
				System.exit(0);
			}
			
			break;
		case "b":
			// Ask user for patron
			System.out.println("Which patron would you like to search for? Provide their email: ");
			String patron = in.nextLine();
			
			// Query for num albums checked out by a single patron
			String sql2 = "SELECT C.Patron_Email, C.Media_ID, M.Media_ID, M.Name, M.Type "
					+ "FROM CHECKS_OUT AS C, MEDIA AS M "
					+ "WHERE C.Patron_Email = ? AND C.Media_ID = M.Media_ID AND M.Type = “Movies” "
					+ "GROUP BY M.Name";
			
			// Build PreparedStatement
			try {
				PreparedStatement ps = conn.prepareStatement(sql2);
				ps.setString(1, patron);
				db.sqlQuery(conn, ps);
			} catch (SQLException e) {
				System.err.println("Error searching the database!");
				System.exit(0);
			}
			
			break;
		case "c":
			// Query for most popular actor in database
			
			break;
		case "d":
			// Query for most listened to artist
			break;
		case "e":
			// Query for patron who's checked out the most videos
			String sql5 = "SELECT C.Patron_Email, C.Media_ID, M.Media_ID, COUNT(M.Name) AS count, M.Type "
					+ "	FROM CHECKS_OUT AS C, MEDIA AS M "
					+ "	WHERE M.Type = “MOVIES” AND C.Media_ID = M.Media_ID AND M.Type = “MOVIES” AND MAX(count) "
					+ "	ORDER BY C.Patron_Email";
			
			// Build PreparedStatement
			try {
				PreparedStatement ps = conn.prepareStatement(sql5);
				db.sqlQuery(conn, ps);
			} catch (SQLException e) {
				System.err.println("Error searching the database!");
				System.exit(0);
			}
			
			break;
		default:
			System.out.println("Invalid choice... returning to menu");
		}
	}
	
	/**
	 * Prompts user for nrew track info, returns new Track object
	 * 
	 * @param in: input scanner
	 * @return new Track user wants to add
	 */
	private void addTrack(Scanner in, Connection conn) {
		System.out.println("Please enter the track name:");
		String name = in.nextLine();
		System.out.println("Please enter the album name:");
		String album_name = in.nextLine();
		System.out.println("Please enter the track length (minutes):");
		String length = in.nextLine();
		System.out.println("Please enter the track year:");
		int year = in.nextInt();
		in.nextLine(); // Consume the \n token
		System.out.println("Please enter the track genre:");
		String genre = in.nextLine();
		
		String sql = "INSERT INTO TRACK VALUES (album_name=?, name=?, genre=?, year=?, time=?);";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, album_name);
			ps.setString(2, name);
			ps.setString(3, genre);
			ps.setInt(4, year);
			ps.setInt(5, Integer.parseInt(length));
			db.sqlQuery(conn, ps);
		} catch (SQLException e) {
			System.err.println("Error adding artist to database!");
			System.exit(0);
		}
	}

}
