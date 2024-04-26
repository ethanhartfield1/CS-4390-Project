import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

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
        String userName;
        String timeJoined;
        private Socket connectionSocket;
        public clientThread(Socket connectionSocket) {this.connectionSocket = connectionSocket;}


        //Loop for the thread connection.
        public void run() {

            try {
                // Create communication streams (same as sample codes)
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                userName = inFromClient.readLine();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime timeJoined= LocalDateTime.now();
                long startTime = System.currentTimeMillis();
                System.out.println(dtf.format(timeJoined));
                System.out.println("Received username from client: " + userName);
                outToClient.write(1);
                // Process messages until client disconnects (by typing exit)
                String op = "";
                double number = 0;
                while (true) {

                    String string = inFromClient.readLine();
                    if (string.equals("+")||string.equals("-")||string.equals("*")||string.equals("/")){
                        op = string;
                    } else if (op.equals("")) number = Double.parseDouble(string);
                    else{
                        if (op.equals("+")) number += Double.parseDouble(string);
                        else if (op.equals("-")) number -= Double.parseDouble(string);
                        else if (op.equals("*")) number *= Double.parseDouble(string);
                        else if (op.equals("/")) number /= Double.parseDouble(string);
                        op = "";
                    }

                    if (string == null) { // when user types exit the server gets an empty response (will happen as client disconnects from server)
                        break;
                    }
                    System.out.println("Server received a message from "+ userName + "! (" + number + op + ")");

                    outToClient.writeBytes(String.valueOf(number) + op + "\n");
                }
                // Close connection with individual client
                connectionSocket.close();
                long endTime = System.currentTimeMillis();
                String timeElapsed = formatDuration(endTime-startTime);
                System.out.println(userName + " disconnected with " + timeElapsed + " time elapsed.");
            } catch (IOException e) {
                // Handle any exceptions during communication
                e.printStackTrace();
            }
        }
        private String formatDuration(long millis){
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            seconds = seconds%60;
                    return String.format("%d minute(s) %d second(s)", minutes, seconds);
        }
    }
}
