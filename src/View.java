import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class View {
	private static final Scanner read = new Scanner(System.in);
	private static Controller controller;
	private static boolean loggedIn = false;
	
	public static void login() {
		System.out.println("Welcome to Sound Good Music School Database Interface.");
		System.out.print("Do you want to log into the database? (Y/n) ");
		
		//Return if the user does not want to connect
		if(!read.next().equalsIgnoreCase("y"))
			return;
		
		//Create a new controller instance
		controller = new Controller();
		//Connect and login to the controller with default credentials
		controller.login("sgms", "postgres", "pass");
		loggedIn = true;
		
		//Give user the input prompt while logged in
		while(loggedIn) {
			try {
				input();
			} catch(SQLException error) {
				System.out.println(error);
			}
		}
	}
	
	private static void input() throws SQLException {
		String option = read.next().toLowerCase();
		switch(option) {
			case "help" -> {
				//All available commands
				System.out.println("LIST - List all available instrument.");
				System.out.println("RENTALS - Get all the ongoing rentals.");
				System.out.println("RENT - Rent an instrument.");
				System.out.println("CANCEL - Terminate an ongoing rental.");
				System.out.println("LOGOUT - Exit the database.");
				System.out.println("HELP - Show this message.");
			}
			case "list" -> {
				System.out.print("Write specific instrument to search for or just write ALL. ");
				//Get the instruments from the controller
				//Read the preferred instrument type from user
				List<String> instruments = controller.getAllInstruments(read.next());
				//Print the output
				for(int i = 0; i < instruments.size(); i++)
					System.out.println(instruments.get(i));
			}
			case "rentals" -> {
				//Get all the ongoing rentals
				List<String> rentals = controller.getOngoingRentals();
				//Print the output
				for(int i = 0; i < rentals.size(); i++)
					System.out.println(rentals.get(i));
			}
			case "rent" -> {
				//Get user input for what to rent for who
				System.out.print("Which instrument do you want to rent? (Instrument ID) ");
				String instrument = read.next().toLowerCase();
				System.out.print("For which student should the instrument be rented? (Student ID) ");
				String student = read.next().toLowerCase();
				
				//Try to rent the instrument and print result
				System.out.println(controller.rentInstrument(student, instrument));
			}
			case "cancel" -> {
				System.out.print("Which ongoing rental do you want to terminate? (Rental ID) ");
				//Try to terminate rental and print result
				System.out.println(controller.terminateRental(read.next()));
			}
			case "logout" -> {
				//Remove the controller
				controller = null;
				//Disable the prompt loop
				loggedIn = false;
				System.out.println("Logged out from the database.");
			}
			default ->
					//Always print if unknown commands are used
					System.out.println("Unknown command, write HELP to see all commands.");
		}
	}
}
