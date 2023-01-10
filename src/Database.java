import java.sql.*;
import java.util.Locale;

public class Database {
	Connection connection = null;
	
	public Connection connect(String database, String username, String password) {
		try {
			Class.forName("org.postgresql.Driver");
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
		return connection;
	}
	
	public void getAllInstruments(String type) {
		if(type.isEmpty()) {
			try {
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM rentable_instruments");
				printInstruments(result);
				connection.commit();
			} catch(SQLException error) {
				System.out.println(error);
			}
		} else {
			try {
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(String.format("SELECT * FROM rentable_instruments WHERE instrument='%s'", type.toLowerCase(Locale.ROOT)));
				printInstruments(result);
				connection.commit();
			} catch(SQLException error) {
				System.out.println(error);
			}
		}

	}
	
	private void printInstruments(ResultSet result) throws SQLException {
		while(result.next()) {
			System.out.print("ID: " + result.getString("id") + "\t");
			System.out.print("Instrument: " + result.getString("instrument") + "\t");
			System.out.print("Brand: " + result.getString("brand_name") + "\t");
			System.out.print("Price: " + result.getString("cost") + "\t");
			System.out.println();
		}
	}
	
	public void rentInstrument(String studentId, String instrumentId) {
		try {
			Statement statement = connection.createStatement();
			//Lock the instrument until rent is committed
			statement.executeQuery(String.format("SELECT * FROM rentable_instruments WHERE id='%s' FOR UPDATE", instrumentId.toLowerCase(Locale.ROOT)));
			//Rent the instrument
			int updated = statement.executeUpdate(String.format("INSERT INTO rental (start_date, end_date, student_id, physical_instrument_id) VALUES (CURRENT_DATE, CURRENT_DATE + INTERVAL '1 YEAR', '%s', '%s')", studentId, instrumentId));
			if(updated != 1) {
				connection.rollback();
				System.out.println("Error while renting the instrument. Please try again.");
				return;
			}
			connection.commit();
			System.out.println("Successfully rented instrument " + instrumentId + " for student " + studentId + ".");
		} catch(SQLException error) {
			System.out.println(error);
		}
	}
	
	public int getStudentRentals(String studentId) {
		int count = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT COUNT(*) FROM rental WHERE end_date>CURRENT_DATE and student_id=%s GROUP BY student_id", studentId));
			if(result.next())
				count = result.getInt("count");
			connection.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public void getOngoingRentals() {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM RENTAL WHERE end_date>CURRENT_DATE ");
			while(result.next()) {
				System.out.print("Rental ID: " + result.getString("rental_id") + "\t");
				System.out.print("Start Date: " + result.getString("start_date") + "\t");
				System.out.print("End Date: " + result.getString("end_date") + "\t");
				System.out.print("Student ID: " + result.getString("student_id") + "\t");
				System.out.print("Instrument ID: " + result.getString("physical_instrument_id") + "\t");
				System.out.println();
			}
			connection.commit();
		} catch(SQLException error) {
			System.out.println(error);
		}
	}
	
}
