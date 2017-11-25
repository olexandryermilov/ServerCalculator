package server;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;


import logger.Logger;

public class Main {

    private static final int PORT_NUMBER=1034;
    private static final String AUTHOR_INFORMATION="Author: Olexandr Yermilov Group: K-24 Variant: 9 - calculator";

    private static Logger logger = new Logger();
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            logger.logMessage("Server started. Listens on port "+PORT_NUMBER);
            Socket clientSocket =serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            logger.logMessage("Client connected on "+PORT_NUMBER+".");
            out.println("Successfully connected");
            Calculator calculator = new Calculator();
            String inputLine,outputLine;
            String messageToLog;
            while((inputLine=in.readLine())!=null){
                logger.logMessage("Received message: "+inputLine+" from "+clientSocket.toString());
                if(inputLine.equalsIgnoreCase("exit")){
                    messageToLog="During connection "+calculator.getCountActions()+" times calculated result";
                    out.println(messageToLog);
                    logger.logMessage(messageToLog);
                    if(calculator.getCountActions()!=0){
                        messageToLog="Min is " + calculator.getMinNum()+ ", max is "+calculator.getMaxNum()+
                                ", average is "+
                                calculator.
                                        getSum().
                                            divide(new BigDecimal(2*calculator.getCountOperands()),10,BigDecimal.ROUND_UP);
                        out.println(messageToLog);
                        logger.logMessage(messageToLog);
                    }
                    messageToLog="Disconnecting";
                    out.println(messageToLog);
                    logger.logMessage(messageToLog);
                    clientSocket.close();
                    break;
                }
                else {
                    if (inputLine.equalsIgnoreCase("who")) {
                        out.println(AUTHOR_INFORMATION);
                        logger.logMessage("Sent message: " + AUTHOR_INFORMATION);
                    } else {
                        outputLine = calculator.parse(inputLine);
                        out.println(outputLine);
                        logger.logMessage("Tried to calculate expression: " + inputLine + ", result is: " + outputLine);
                    }
                }
            }

        }
        catch (IOException e) {
            logger.logMessage(e.getMessage());
        }

        finally{
            logger.logMessage("Shutting down server");
        }
    }
}
