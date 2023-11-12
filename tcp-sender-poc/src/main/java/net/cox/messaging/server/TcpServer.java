package net.cox.messaging.server;

import net.cox.messaging.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public static void main(String[] args) {
        int port = Constants.PORT; // Specify the port you want to use

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            // Listen for incoming connections
            while (true) {
                // Accept a client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                // Process the incoming data and send back to the client
                processIncomingData(clientSocket);

                // Close the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processIncomingData(Socket clientSocket) throws IOException {
        // Read data from the client
        InputStream is = clientSocket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = is.read(buffer);

        // Print the received data
        if (bytesRead > 0) {
            String receivedData = new String(buffer, 0, bytesRead);
            System.out.println("Received data: " + receivedData);

            // Send back the received data to the client
            sendResponseToClient(clientSocket, receivedData.getBytes());
        } else {
            System.out.println("No data received.");
        }
    }

    private static void sendResponseToClient(Socket clientSocket, byte[] responseData) throws IOException {
        // Get the output stream to send data back to the client
        OutputStream os = clientSocket.getOutputStream();

        // Send the response data back to the client
        os.write(responseData);
        os.flush();

        System.out.println("Sent response back to the client.");
    }
}
