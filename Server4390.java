import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class TCPmathServer {

    public static void main(String argv[]) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server is UP and running!");

        //Loop for new clients to create a thread for
        while (true) {
            System.out.println("Client Search");
            Socket connectionSocket = welcomeSocket.accept();

            // Create a new thread for continued communication with a specific client
            clientThread clientThread = new clientThread(connectionSocket);
            clientThread.start();
        }
    }

    public static class clientThread extends Thread {
        String userName;
        private Socket connectionSocket;
        public clientThread(Socket connectionSocket) {this.connectionSocket = connectionSocket;}


        //Loop for the thread connection.
        public void run() {

            try {
                // Create communication streams (same as sample codes)
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // receive username and send acknowledgement
                userName = inFromClient.readLine();
                System.out.println("Received request from from client: " + userName);
                outToClient.write(1);

                //log date and time of connection
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime timeJoined= LocalDateTime.now();
                long startTime = System.currentTimeMillis();
                System.out.println(dtf.format(timeJoined));

                //variables for calculator
                String currentOpp = "";
                double number = 0;
                String operationS;

                // Process messages until client disconnects (by typing exit)
                while (true) {

                    operationS = inFromClient.readLine();

                    //Calculator function
                    if (operationS.equals("+")||operationS.equals("-")||operationS.equals("*")||operationS.equals("/")){
                        currentOpp = operationS;
                    }
                    else if (currentOpp.equals("")) number = Double.parseDouble(operationS);
                    else{
                        if (currentOpp.equals("+")) number += Double.parseDouble(operationS);
                        else if (currentOpp.equals("-")) number -= Double.parseDouble(operationS);
                        else if (currentOpp.equals("*")) number *= Double.parseDouble(operationS);
                        else if (currentOpp.equals("/")) number /= Double.parseDouble(operationS);
                        currentOpp = "";
                    }

                    if (operationS.equalsIgnoreCase("exit")) { // when user types exit the server gets an empty response (will happen as client disconnects from server)
                        break;
                    }

                    System.out.println("Server received request from "+ userName + "! (" + number + " " + currentOpp + " )");
                    outToClient.writeBytes(String.valueOf(number) + currentOpp + "\n");
                }

                // Close connection with individual client by closing thread
                connectionSocket.close();

                //calculate time passed and log the info
                long endTime = System.currentTimeMillis();
                String timeElapsed = formatDuration(endTime-startTime);
                System.out.println(userName + " disconnected with " + timeElapsed + " time elapsed.");

            } catch (IOException e) {
                // Handle any exceptions during communication
                e.printStackTrace();
            }
        }

        // Formatting time be displayed
        private String formatDuration(long millis){
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            seconds = seconds%60;
            return String.format("%d minute(s) %d second(s)", minutes, seconds);
        }
    }
}
