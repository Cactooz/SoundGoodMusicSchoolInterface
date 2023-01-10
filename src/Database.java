import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	public Connection connect(String database, String username, String password) {
		Connection connection = null;
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
}
