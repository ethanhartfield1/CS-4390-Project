import java.io.*;
import java.net.*;
class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        String sentence;
        String modifiedSentence;
        boolean clientConnection;
        
        System.out.println("Client is running: " );
        System.out.print("Enter a string to capitalize or type 'exit' to close the connection: ");

        Socket clientSocket = new Socket("127.0.0.1", 6789);
        clientConnection = true; // allow for client to keep connection with server

      // create necessary objects for connection
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        //Loop requests till user types "exit"
        while (clientConnection) {

            sentence = inFromUser.readLine();

            if (sentence.equalsIgnoreCase("exit")) {
                clientSocket.close();
                clientConnection = false;
            }
            else {
                System.out.println("sent...");

                outToServer.flush(); // was running into buffer issues
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();

                System.out.println("FROM SERVER: " + modifiedSentence);
                System.out.print("Enter next string or type 'exit' to close connection: ");
            }

            System.out.println("Connection Terminated");
        }

    }
}


