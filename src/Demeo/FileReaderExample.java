package Demeo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Custom exception class for file reading errors
class FileReadingException extends Exception {
 public FileReadingException(String message, Throwable cause) {
     super(message, cause);
 }
}

class InvalidFileFormatException extends FileReadingException {
    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class FileReaderExample {
    private static final Logger logger = Logger.getLogger(FileReaderExample.class.getName());

	    public static void readFile(String filePath) throws FileReadingException, FileNotFoundException {
	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                
	                System.out.println(line);
	                // Check for invalid file format 
	                if (!line.contains("filePath")) {
	                    throw new InvalidFileFormatException("Invalid file format: " + filePath, null);
	                }
	            }
	        }catch (java.io.FileNotFoundException e) {
	            // Custom exception handling for file not found errors
	        	 logError("File not found: " + filePath, e);
	            throw new FileNotFoundException("File not found: " + filePath);
	        }
	        catch (IOException  e) {
	            // Custom exception handling for file reading errors
	        	logError("Error reading the file: " + filePath, e);
	            throw new FileReadingException("Error reading the file: " + filePath, e);
	        }
	    }
	
	    private static void logError(String message, Throwable throwable) {
	        // Log the error 
	        logger.log(Level.SEVERE, message, throwable);
	    }
	    
	public static void main(String[] args) throws FileNotFoundException, InvalidFileFormatException {
		
		String filePath = "financial_data.csv";  

        try {
            // Read and process the contents of the file
            readFile(filePath);
        }catch (FileReadingException e) {
            // Handle custom file reading exception
            System.err.println("Error: " + e.getMessage());
        }
        
	}

}
