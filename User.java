package broadcast;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class User extends Thread {

	// The user socket
	private static Socket userSocket = null;
	// The output stream
	private static PrintStream output_stream = null;
	// The input stream
	private static BufferedReader input_stream = null;

	private static BufferedReader inputLine = null;
	private static boolean closed = false;
	private static String userName = null;
	

	public static void main(String[] args) {

		// The default port.
		int portNumber = 8000;
		// The default host.
		String host = "localhost";

		if (args.length < 2) {
			System.out
			.println("Usage: java User <host> <portNumber>\n"
					+ "Now using host=" + host + ", portNumber=" + portNumber);
		} else {
			host = args[0];
			portNumber = Integer.valueOf(args[1]).intValue();
		}

		/*
		 * Open a socket on a given host and port. Open input and output streams.
		 */
		try {
			userSocket = new Socket(host, portNumber);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			output_stream = new PrintStream(userSocket.getOutputStream());
			input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to the host "
					+ host);
		}

		/*
		 * If everything has been initialized then we want to write some data to the
		 * socket we have opened a connection to on port portNumber.
		 */
		if (userSocket != null && output_stream != null && input_stream != null) {
			try {                
				/* Create a thread to read from the server. */
				new Thread(new User()).start();

				// Get user name and join the social net
				System.out.println("Enter your name");
				userName = inputLine.readLine().trim();
				String joinStatus = "#join <" + userName + ">"; 
				output_stream.println(joinStatus);
				
				
				
				


				while (!closed) {
				
					String userMessage = new String();
					String userInput = inputLine.readLine().trim();
					if(userInput.equals("Exit"))
					{
						output_stream.println("#Bye");
						break;
					}

					output_stream.println("#status " + userInput);
					
					// Read user input and send protocol message to server

				}
				/*
				 * Close the output stream, close the input stream, close the socket.
				 */
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}

	/*
	 * Create a thread to read from the server.
	 */
	public void run() {
		/*
		 * Keep on reading from the socket till we receive a Bye from the
		 * server. Once we received that then we want to break.
		 */

		
		String responseLine;
		
		try {
			while ((responseLine = input_stream.readLine()) != null) {

				if(responseLine.startsWith("#welcome"))
				{
					System.out.println("Welcome " + userName + " to our Social Media App.");
					System.out.println("To leave enter Exit on a new line");
				}
				else if(responseLine.startsWith("#statusPosted"))
				{
					System.out.println("Your status was posted succesfully!");
				}
				else if(responseLine.startsWith("#newStatus "))
				{
					System.out.println("Somebody just posted something");
				}
				else if(responseLine.startsWith("#newuser"))
				{
					System.out.println("New user has joined");
				}
				else if(responseLine.startsWith("#Bye"))
				{
					System.out.println("You have left the chat");
					break;
				}
				else if(responseLine.startsWith("#Busy"))
				{
					System.out.println("The server is busy");
					break;
				}
				else if(responseLine.startsWith("#Leave"))
				{
				

					System.out.println("Someone has left the chat");
				}
				else
				{
					System.out.println("Didn't know what to do " + responseLine);
				}

				System.out.println("Msg received is: " + responseLine);

				// Display on console based on what protocol message we get from server.
				


			}
			closed = true;
			output_stream.close();
			input_stream.close();
			userSocket.close();
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}



