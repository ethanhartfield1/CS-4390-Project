import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        String string;
        String modifiedSentence;
        String userName;
        boolean clientConnection;

        System.out.println("Client is running");
        System.out.print("Enter a name to be used for the server:");

        Socket clientSocket = new Socket("127.0.0.1", 6789);
        clientConnection = true; // allow for client to keep connection with server

        // create necessary objects for connection
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        userName = inFromUser.readLine();

        outToServer.writeBytes(userName + '\n');
        outToServer.flush();
        int confirmation = inFromServer.read();
        while (confirmation != 1) {
            ;//Loop until gets a true
        }
        System.out.println("Acknowledgement from server received!");

        System.out.print("Enter any integer or double with four basic math operations (+, -, *, /) or type 'exit' to close the connection: ");

        while (clientConnection) {
            string = inFromUser.readLine();

            if (string.equalsIgnoreCase("exit")) {
                clientSocket.close();
                clientConnection = false;
            }
            else {
                System.out.println("sent...");

                outToServer.flush(); // was running into buffer issues
                outToServer.writeBytes(string + '\n');
                String currentNumber = inFromServer.readLine();
                System.out.println("FROM SERVER: " + currentNumber);
                System.out.print("Enter next string or type 'exit' to close connection: ");
            }
        }

        System.out.println("Connection Terminated");
    }
}
