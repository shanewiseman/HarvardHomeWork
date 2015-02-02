package cscie55.hw5.bank;

import java.util.Queue;

import cscie55.hw5.bank.command.Command;

public class CommandExecutionThread extends Thread {

    private Bank bank;
    private Queue<Command> commandQueue;
    private boolean executeCommandInsideMonitor;
    
    public CommandExecutionThread(Bank bank, Queue<Command> commandQueue, boolean executeCommandInsideMonitor){
        
        this.bank = bank;
        this.commandQueue = commandQueue;
        this.executeCommandInsideMonitor = executeCommandInsideMonitor;
        
    }
    
    public void run(){
        
        // continue to loop until thread receives stop command
        while( true ){
            Command currentCommand;
             
           do {
               synchronized(commandQueue){
                   
                   currentCommand = commandQueue.poll();
                   if( currentCommand == null){    
                       try {
                        commandQueue.wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                   
               }    
           } while(currentCommand == null);
           
           // break if we get a stop command
           if(currentCommand.isStop())
               break;

           
           try {
               if(executeCommandInsideMonitor){
                   synchronized(commandQueue){
                       currentCommand.execute(bank);
                   }
               } else {
                   currentCommand.execute(bank);
               }
           } catch (InsufficientFundsException e) {
               //Do Nothing if you're broke
           }
                        
        }
        
        
        
    }
    
    
}
