package Demeo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

class ApiCallException extends Exception {
   

	public ApiCallException(String message, Throwable cause) {
        super(message, cause);
    }
}

//Custom exception class for API call timeout errors
class TimeoutException extends ApiCallException {
 public TimeoutException(String message, Throwable cause) {
     super(message, cause);
 }
}

class ApiResponseException extends ApiCallException {
    public ApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
//Custom exception class for network issues
class NetworkException extends ApiCallException {
 public NetworkException(String message, Throwable cause) {
     super(message, cause);
 }
}

public class ApiCaller {

    public static String makeApiCall(String apiUrl,int timeout) throws ApiCallException, SocketTimeoutException, UnknownHostException {
        try {
            // Create URL object
            URL url = new URL(apiUrl);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");
            
         // Set timeout
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            // Get response code
            int responseCode = connection.getResponseCode();

            // Check if the API call was successful (status code 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                return response.toString();
            } else {
                throw new ApiResponseException("API call failed. Response Code: " + responseCode, null);
            }
        } catch (Exception e) {
            // Custom exception handling for API call errors
            throw new ApiCallException("Error making API call to " + apiUrl, e);
        }
        
    }

	
	
	
	public static void main(String[] args) throws ApiResponseException, UnknownHostException, NetworkException {
		
		String apiUrl = "https://api.example.com/data";
		int timeout = 5000;  // Specify timeout in milliseconds
		
        try {
            // Make the API call
            String apiResponse = makeApiCall(apiUrl,timeout);

            
            System.out.println("API Response: " + apiResponse);
        } catch (ApiCallException e) {
            // Handle custom API call exception
            System.err.println("Error: " + e.getMessage());
        } catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
