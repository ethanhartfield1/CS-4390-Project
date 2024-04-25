import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String argv[]) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(6789);

        //Loop for new clients to create a thread for
        while (true) {
            System.out.println("Server is UP and running!");
            Socket connectionSocket = welcomeSocket.accept();

            // Create a new thread for continued communication with a specific client
            clientThread clientThread = new clientThread(connectionSocket);
            clientThread.start();
        }
    }

    public static class clientThread extends Thread {
        String clientSentence;
        String capitalizedSentence;

        private Socket connectionSocket;

        public clientThread(Socket connectionSocket) {this.connectionSocket = connectionSocket;}
        
        //Loop for the thread connection.
        public void run() {
            try {
                // Create communication streams (same as sample codes)
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // Process messages until client disconnects (by typing exit)
                while (true) {
                    clientSentence = inFromClient.readLine();
                    if (clientSentence == null) { // when user types exit the server gets an empty response (will happen as client disconnects from server)
                        break;
                    }
                    System.out.println("Server received message! (" + clientSentence + ")");
                    capitalizedSentence = clientSentence.toUpperCase() + '\n';// still capitalizes but goal is to calculate strings)
                    outToClient.writeBytes(capitalizedSentence);
                }
                // Close connection with individual client
                connectionSocket.close();
            } catch (IOException e) {
                // Handle any exceptions during communication
                e.printStackTrace();
            }
        }
    }
}
