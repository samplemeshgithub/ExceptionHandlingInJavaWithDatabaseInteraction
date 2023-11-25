package Demeo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

//Custom exception class for database operation errors
class DatabaseOperationException extends Exception {
 public DatabaseOperationException(String message, Throwable cause) {
     super(message, cause);
 }
}

//Custom exception class for data integrity violations
class DataIntegrityViolationException extends  Exception{
 public DataIntegrityViolationException(String message, Throwable cause) {
     super(message, cause);
 }
}
public class DatabaseConnectionWithJavaApp {
	
	 // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/minds";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
	
	 // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    private static final Logger logger = Logger.getLogger(DatabaseConnectionWithJavaApp.class.getName());

	public static void establishConnection() throws DatabaseConnectionException  {
        try {
           
            Class.forName("com.mysql.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
        } catch (ClassNotFoundException | SQLException e) {
           
        	logError("Error establishing database connection", e);
           throw new DatabaseConnectionException("Error establishing database connection", e);
        }
    }
	
	public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
              
                connection.close();
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
    }
	
	public static void insertData(String username,String emalil) throws DatabaseOperationException, DataIntegrityViolationException {
        try {
            // Check if the connection is established
            if (connection == null || connection.isClosed()) {
                throw new DatabaseOperationException("Database connection is not established", null);
            }

            // SQL query for data insertion
            String insertQuery = "INSERT INTO User (username,email) VALUES (?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters for the prepared statement
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, emalil);

                // Execute the update
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
        	 if (e.getErrorCode() == 1062) {
        		 logError("Data integrity violation: Duplicate entry", e);
                 // MySQL error code for duplicate entry (violates primary key or unique constraint)
                 throw new DataIntegrityViolationException("Data integrity violation: Duplicate entry", e);
             } else {
                 // Other database operation errors
            	 logError("Error inserting data into the database", e);
                 throw new DatabaseOperationException("Error inserting data into the database", e);
             }
        }
    }

	private static void logError(String message, Throwable throwable) {
       
        logger.log(Level.SEVERE, message, throwable);
    }
	public static void main(String[] args) throws DatabaseOperationException, DatabaseConnectionException {
		

		
		try {
            // Establish the database connection
            establishConnection();

           

            System.out.println("Database connection established successfully!");
            // Perform data insertion
            insertData("john_doe","jhon@example.com");

            System.out.println("Data inserted successfully!");
        } catch (DataIntegrityViolationException e) {
			
			e.printStackTrace();
		} finally {
            // Ensure the connection is closed in all cases
            closeConnection();
        }
    }
		
	}


