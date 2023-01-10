import java.sql.*;

public class DAO {
	Connection connection = null;
	
	public void connect(String database, String username, String password) {
		try {
			Class.forName("org.postgresql.Driver");
			//Create the connection to the database
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, username, password);
			if(connection != null) {
				System.out.println("Connected to " + database + " database successfully!");
				//Disable autoCommit for the database connection
				connection.setAutoCommit(false);
			} else
				System.out.println("Connection to " + database + " database failed.");
		} catch(Exception error) {
			System.out.println(error);
		}
	}
	
	public boolean readStudentId(String studentId) {
		boolean exist = false;
		try {
			Statement statement = connection.createStatement();
			//Check if a student with the id exist
			ResultSet result = statement.executeQuery(String.format("SELECT COUNT(student_id) FROM student WHERE student_id='%s'", studentId));
			if(result.next())
				exist = result.getBoolean("count");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return exist;
	}
	
	public boolean readInstrumentId(String instrumentId) {
		boolean exist = false;
		try {
			Statement statement = connection.createStatement();
			//Check if a rentable instrument with the id exist
			ResultSet result = statement.executeQuery(String.format("SELECT COUNT(id) FROM rentable_instruments WHERE id='%s'", instrumentId));
			if(result.next())
				exist = result.getBoolean("count");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return exist;
	}
	
	public ResultSet readAllAvailableInstruments() {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			//Get the all rentable instruments from the rentable_instruments view
			result = statement.executeQuery("SELECT * FROM rentable_instruments");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return result;
	}
	
	public ResultSet readSpecificAvailableInstruments(String type) {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			//Get the specific type of rentable instruments from the rentable_instruments view
			result = statement.executeQuery(String.format("SELECT * FROM rentable_instruments WHERE instrument='%s'", type.toLowerCase()));
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return result;
	}
	
	public int readStudentRental(String studentId) {
		int count = 0;
		try {
			Statement statement = connection.createStatement();
			//Get the count of ongoing rentals for the defined student
			ResultSet result = statement.executeQuery(String.format("SELECT COUNT(*) FROM rental WHERE end_date>CURRENT_DATE and student_id=%s GROUP BY student_id", studentId));
			if(result.next())
				count = result.getInt("count");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return count;
	}
	
	public boolean createRental(String studentId, String instrumentId) {
		try {
			Statement statement = connection.createStatement();
			//Lock the instrument until rent is committed
			statement.executeQuery(String.format("SELECT * FROM rentable_instruments WHERE id='%s' FOR UPDATE", instrumentId));
			//Rent the instrument
			int updated = statement.executeUpdate(String.format("INSERT INTO rental (start_date, end_date, student_id, physical_instrument_id) VALUES (CURRENT_DATE, CURRENT_DATE + INTERVAL '1 YEAR', '%s', '%s')", studentId, instrumentId));
			//Rollback the change if one row is not updated
			if(updated != 1) {
				connection.rollback();
				return false;
			}
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return true;
	}
	
	public ResultSet readOngoingRentals() {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			//Get all the ongoing rentals
			result = statement.executeQuery("SELECT * FROM rental WHERE end_date>CURRENT_DATE");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return result;
	}
	
	public boolean readOngoingRentalId(String rentalId) {
		boolean exist = false;
		try {
			Statement statement = connection.createStatement();
			//Check if the rentalId is a valid ongoing rental
			ResultSet result = statement.executeQuery(String.format("SELECT COUNT(rental_id) FROM rental WHERE end_date>CURRENT_DATE AND rental_id='%s'", rentalId));
			if(result.next())
				exist = result.getBoolean("count");
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return exist;
	}
	
	public boolean updateOngoingRental(String rentalId) {
		try {
			Statement statement = connection.createStatement();
			//Lock the rental that should be updated until commit
			statement.executeQuery(String.format("SELECT * FROM rental WHERE rental_id=%s FOR UPDATE", rentalId));
			//Update the end date of the rental to today's date
			int updated = statement.executeUpdate(String.format("UPDATE rental SET end_date=CURRENT_DATE WHERE rental_id='%s'", rentalId));
			//Rollback the change if one row is not updated
			if(updated != 1) {
				connection.rollback();
				return false;
			}
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
		return true;
	}
}
