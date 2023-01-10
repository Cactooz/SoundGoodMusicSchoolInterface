import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	private final DAO db;
	
	public Controller() {
		//Create a new database connection (not logged in)
		db = new DAO();
	}
	
	//Log into the database
	public void login(String database, String username, String password) {
		db.connect(database, username, password);
	}
	
	//Get instruments available to rent
	public List<String> getAllInstruments(String type) throws SQLException {
		ResultSet result;
		if(type.equalsIgnoreCase("ALL"))
			//Get all available instruments
			result = db.readAllAvailableInstruments();
		else
			//Get all available instruments of a specific type
			result = db.readSpecificAvailableInstruments(type);
		
		List<String> output = new ArrayList<>();
		//Format the output for the view
		while(result.next()) {
			output.add("ID: " + result.getString("id") +
					"\tInstrument: " + result.getString("instrument") +
					"\tBrand: " + result.getString("brand_name") +
					"\tPrice: " + result.getString("cost"));
		}
		
		if(output.isEmpty())
			output.add("No instruments could be found.");
		
		return output;
	}
	
	//Rent a instrument for a student
	public String rentInstrument(String studentId, String instrumentId) {
		//Check if the student already have 2 rented instruments
		if(db.readStudentRental(studentId) >= 2)
			return "Error while renting the instrument. The student have already rented 2 instruments.";
		
		//Rent the instrument
		if(db.createRental(studentId, instrumentId))
			return "Successfully rented instrument " + instrumentId + " for student " + studentId + ".";
		
		return "Error while renting the instrument. Please try again.";
	}
	
	//Get all ongoing rentals
	public List<String> getOngoingRentals() throws SQLException {
		//Get the ongoing rentals from the database
		ResultSet result = db.readOngoingRentals();
		
		List<String> output = new ArrayList<>();
		//Format the output for the view
		while(result.next()) {
			output.add("ID: " + result.getString("rental_id") +
					"\tStart Date: " + result.getString("start_date") +
					"\tEnd Date: " + result.getString("end_date") +
					"\tStudent ID: " + result.getString("student_id") +
					"\tInstrument ID " + result.getString("physical_instrument_id"));
		}
		
		if(output.isEmpty())
			output.add("No ongoing rentals could be found.");
		
		return output;
	}
	
	//Terminate an ongoing rental
	public String terminateRental(String rentalId) {
		//Terminate the rental
		if(db.updateOngoingRental(rentalId))
			return "Successfully terminated instrument rental " + rentalId + ".";
		
		return "Error while terminating instrument rental " + rentalId + ". Please try again.";
	}
}
