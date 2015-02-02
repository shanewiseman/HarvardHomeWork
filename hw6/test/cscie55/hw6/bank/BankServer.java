package cscie55.hw6.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankServer {
    
    private static Bank bank = null;
    private static Map<Integer,Thread> executionThreads = new ConcurrentHashMap<Integer,Thread>();
    private static ServerSocket serverSocket = null;
    private static final int SERVER_PORT_NUMBER = 9090;
    private static Thread cleanUpThread = null;
    
    public static void main(String [] args){
        init();
        run();
    }
    
    private static void init(){
        bank = new BankImpl();
        
        try {
            serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
     // clean up any done threads, is not really needed but...
        cleanUpThread = new Thread( new Runnable() {
            public void run(){
                while( true ){
                    for(Thread thread : executionThreads.values()){
                        if( !thread.isAlive() ){
                           try {
                            thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            
                            executionThreads.remove(thread.hashCode());
                        }
                    }
                    Thread.yield();
                }

            }
            
       });
       cleanUpThread.start();
    }
    
    private static void run(){
        
        /* should handle this differently, maybe catch a control-c or something?? i would like to clean up cleanly
         * would also like to use a pool, directions state to create a new instance each time, very wasteful */
        System.out.println("SYSTEM RUNNING");
        while( true ){
            try { 
                    
                Socket clientSocket = serverSocket.accept();
                    
                System.out.println("CONNECTION FROM " + clientSocket.getPort());
                    
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                    
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                    
                Thread newCommandThread = new CommandExecutionThread(bank,in,out, clientSocket);
                        newCommandThread.start();
                    
                //place the "executing" pool to keep them in scope and have ability to manage
                executionThreads.put(newCommandThread.hashCode(), newCommandThread);

            } catch(Exception ex){
                ex.printStackTrace();         
            }
        }
    }
}
