package com.company;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import Logger.Logger;

public class Main {

    private static int countActions=0;
    private static BigDecimal minNum=null,maxNum=null,sum=BigDecimal.ZERO;
    private static final int PORT_NUMBER=1034;
    private static final String AUTHOR_INFORMATION="Author: Olexandr Yermilov Group: K-24 Variant: 9 - calculator";

    private static BigDecimal getMinBigDecimal(BigDecimal a, BigDecimal b){
        if(a.compareTo(b)==-1)return a;
        return b;
    }
    private static BigDecimal getMaxBigDecimal(BigDecimal a, BigDecimal b){
        if(a.compareTo(b)==-1)return b;
        return a;
    }

    private static Character getArithmeticOperation(String argument){
        if(argument.equals("/"))return '/';
        if(argument.equals("*"))return '*';
        if(argument.equals("-"))return '-';
        if(argument.equals("+"))return '+';
        return null;
    }
    private static BigDecimal getNumber(String argument){
        try{
            BigDecimal num = new BigDecimal(argument);
            return num;
        }catch (RuntimeException e){
            return null;
        }
    }
    private static void updateMinMaxSum(BigDecimal first, BigDecimal second){
        if(minNum==null){
            minNum=getMinBigDecimal(first,second);
        }
        else
        {
            minNum=getMinBigDecimal(minNum,getMinBigDecimal(first,second));
        }
        if(maxNum==null){
            maxNum=getMaxBigDecimal(first,second);
        }
        else
        {
            maxNum=getMaxBigDecimal(maxNum,getMaxBigDecimal(first,second));
        }
        sum=sum.add(first);
        sum=sum.add(second);
    }
    private static String calculateExpression(String line){
        String[] arg = line.split(" ");
        if(arg.length!=3)return "Wrong expression: should be {+,-,*,/} number number";
        Character operation = getArithmeticOperation(arg[0]);
        if(operation==null)return "Wrong expression: should be {+,-,*,/} number number";
        BigDecimal firstOperand = getNumber(arg[1]);
        if(firstOperand==null)return "Wrong expression: should be {+,-,*,/} number number";
        BigDecimal secondOperand = getNumber(arg[2]);
        if(secondOperand==null)return "Wrong expression: should be {+,-,*,/} number number";
        if(!operation.equals('/')||!secondOperand.equals(BigDecimal.ZERO)) {
            countActions++;
            updateMinMaxSum(firstOperand, secondOperand);
        }
        if(operation=='+'){
            return firstOperand.add(secondOperand).toString();
        }
        else{
            if(operation=='-'){
                return firstOperand.subtract(secondOperand).toString();
            }
            else{
                if(operation=='*'){
                    return firstOperand.multiply(secondOperand).toString();
                }
                else{
                    if(secondOperand.equals(BigDecimal.ZERO)){
                        return "Error: Division by zero";
                    }
                    else{
                        return firstOperand.divide(secondOperand,10, RoundingMode.UP).toString();
                    }
                }
            }
        }
    }
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
            String inputLine,outputLine;
            countActions = 0;
            while((inputLine=in.readLine())!=null){
                logger.logMessage("Received message: "+inputLine+" from "+clientSocket.toString());
                if(inputLine.equalsIgnoreCase("exit")){
                    out.println("During connection "+countActions+" times calculated result");
                    logger.logMessage("During connection "+countActions+" times calculated result");
                    if(countActions!=0){
                        out.println("Min is " + minNum+ ", max is "+maxNum+", average is "+sum.divide(new BigDecimal(2*countActions),10,BigDecimal.ROUND_UP));
                        logger.logMessage("Min is " + minNum+ ", max is "+maxNum+", average is "+sum.divide(new BigDecimal(2*countActions),10,BigDecimal.ROUND_UP));
                    }
                    out.println("Disconnecting");
                    logger.logMessage("Disconnecting");
                    clientSocket.close();
                    break;
                }
                else {
                    if (inputLine.equalsIgnoreCase("who")) {
                        out.println(AUTHOR_INFORMATION);
                        logger.logMessage("Sent message: " + AUTHOR_INFORMATION);
                    } else {
                        outputLine = calculateExpression(inputLine);
                        out.println(outputLine);
                        logger.logMessage("Tried to calculate expression: " + inputLine + ", result is: " + outputLine);
                    }
                }
            }

        }
        catch (SocketException e){
            logger.logMessage(e.getMessage());
        }
        catch (IOException e) {
            //e.printStackTrace();
            //System.exit(1);
        }

        finally{
            logger.logMessage("Shutting down server");
        }
    }
}
