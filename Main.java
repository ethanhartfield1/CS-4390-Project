import java.io.*;
import java.net.*;

class TCPClient1 {

    public static void main(String argv[]) throws Exception
    {
        String operation;
        String userName;
        String currentNumber; // Operation that the server is currently holding
        boolean clientConnection; // allows for client to keep connection with server

        System.out.println("Client is running");
        System.out.print("Enter a name to be used for the server: ");

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        // create necessary objects for connection
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        userName = inFromUser.readLine();

        //Send the username to the server and await acknowledgement
        outToServer.writeBytes(userName + '\n');
        outToServer.flush();

        int confirmation = inFromServer.read();
        while (confirmation != 1) {}       //Loop until acknowledgment is received from server (as the integer 1)
        clientConnection = true;
        System.out.println("Acknowledgement from server received! Connection Established.");

        System.out.println("Server can solve math questions with four basic math operations (+, -, *, /) for as many equations as needed \n" +
                "When finished type 'exit' to close the connection. \n");
        System.out.print("Enter a single integer or double: or type 'exit' to close the connection: ");

        // loop for each request the client has
        while (clientConnection) {
            operation = inFromUser.readLine();

            if (operation.equalsIgnoreCase("exit")) {
                outToServer.writeBytes(operation + '\n');
                clientSocket.close();
                clientConnection = false;
            }
            else {
                System.out.println("sent...");

                outToServer.flush();
                outToServer.writeBytes(operation + '\n');
                currentNumber = inFromServer.readLine();
                System.out.println("Current equation in the server: " + currentNumber);
                System.out.print("Enter next operation/ integer or type 'exit' to close connection: ");
            }
        }

        System.out.println("Connection with mathServer closed, have a good day!");
    }
}
