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
	
}
