package cscie55.hw6.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cscie55.hw6.bank.command.Command;

public class CommandExecutionThread extends Thread {
    
    private Bank bank;
    private BufferedReader reader;
    private PrintWriter   writer;
    private Socket        socket;
    private int commandsRun = 0;  
    
    //limiting connection idle times to 2000ms
    private static final int IDLE_LIMIT = 2000;
    
    
    public CommandExecutionThread(Bank bank, BufferedReader inputStream, PrintWriter outputStream, Socket socket){
        this.bank = bank;
        this.socket = socket;
        reader = inputStream;
        writer = outputStream;
    }
    
    
    /* keep processing commands on this connection while the connection is considered "good" */
    public void run(){
        
        Command command;
        while( socket.isConnected()){
            if(isSocketStateGood()){
            
                try {
                    command = getCmd();
                } catch(Exception e){
                    writer.println("ERROR: Command Unknown");
                    return;
                }    
                //System.out.println(this.getId());
                executeCmd(command);

            } else {
                System.out.println("Client Port " +socket.getPort() + " Ran " + commandsRun + " Commands");
                return;
            }
        }
    }
    /* 
     * This was not called for directions but.. function will monitor connection until it has something to work on.
     *  if the connection is idle for too long ( IDLE_LIMIT ) I'm going to cut the connection. Could be used to mitigate
     *  connection floods are hung connections on the client side
     */
    private boolean isSocketStateGood(){
        // using this to track idle time
        int idleTime = 0;
        
        try {
            // while the connection does not have data to read
            while( !reader.ready() ){
               
                // make sure the connection is not closed
                if( ! socket.isClosed() ){
                   try {
                       Thread.sleep(1);
                   } catch (InterruptedException socketEx) {
                       // TODO Auto-generated catch block
                       socketEx.printStackTrace();
                   }
                   //if we reach the limit, cut the server side processing
                   if(idleTime++ > IDLE_LIMIT){
                       return false;
                   }
                   
                   /*loop back around, socket is not ready to read from. We'll sit in the loops until the socket is ready or
                       the socket closes
                   */
                   continue;
               } else {
                   //connection is closed
                   return false;
               }
            }
           
        } catch (IOException sleepEx) {
            sleepEx.printStackTrace();
            return false;
        }
        
        return true;
    }
    private Command getCmd() throws IOException{
        
        return Command.parse(reader.readLine());
        
    }
    private void executeCmd(Command command) {
        try {
            commandsRun++;
            String output = command.execute(bank);
            if(output == null){
                writer.println("");
            } else {
                writer.println(output);
            }
            
        } catch (InsufficientFundsException | DuplicateAccountException e) {
            //e.printStackTrace();
            writer.println(e.getMessage());
        }
    }
    
}
