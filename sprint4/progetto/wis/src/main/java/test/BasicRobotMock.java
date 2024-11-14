package main.java.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;


public class BasicRobotMock {
	public ServerSocket serverSocket;
	public BasicRobotMock() {
		CommUtils.outblue("BasicRobotMock listening on port " + 8020);

        // Open a ServerSocket to listen for incoming connections
        try {
        	this.serverSocket = new ServerSocket(8020);
            
            // Loop to accept connections continuously
            while (true) {
                try {
                    // Accept a new client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // Handle client messages
                    handleClient(clientSocket);
                } catch (Exception e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            CommUtils.outred("BasicRobotMock | Could not start server: " + e.getMessage());
        }
	}
	private static void handleClient(Socket clientSocket) {
        // Create input and output streams for communication
        try{
        	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;

            // Read messages from the client
            while ((message = in.readLine()) != null ) {
        		CommUtils.outblue("BasicRobotMock | Received: " + message);
            	if ( message.contains("engage")) {                    
                    // Optionally, send a response to the client
                    IApplMessage response = CommUtils.buildReply("basicrobot", "engagedone", "engagedone(0)", "oprobot");

                    // Stop if a certain keyword is received, e.g., "bye"
                    out.println(response.toString());
            	}
            	else if(message.contains("moverobot")){
            		// Optionally, send a response to the client
                    IApplMessage response = CommUtils.buildReply("basicrobot", "moverobotdone", "moverobotdone(0)", "oprobot");

                    // Stop if a certain keyword is received, e.g., "bye"
                    out.println(response.toString());
            	}
            }

        } catch (Exception e) {
            CommUtils.outred("BasicRobotMock | Error handling client messages: " + e.getMessage());
        } finally {
            // Close the client socket when done
            try {
            	CommUtils.delay(1000);
                clientSocket.close();
                CommUtils.outred("BasicRobotMock | Client connection closed.");
            } catch (Exception e) {
                CommUtils.outred("BasicRobotMock | Error closing client connection: " + e.getMessage());
            }
        }
	}
	public void shutdown() {
		try {
			if(serverSocket!= null)
				serverSocket.close();
		} catch (IOException e) {
			CommUtils.outred("BasicRobotMock | Error closing server socket");
		}
	}
}
