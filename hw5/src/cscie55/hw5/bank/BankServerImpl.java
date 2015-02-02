package cscie55.hw5.bank;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cscie55.hw5.bank.command.Command;
import cscie55.hw5.bank.command.CommandStop;

public class BankServerImpl implements BankServer {

    private final int threadCount;
    
    private Queue<Command> commandQueue = new LinkedList<Command>();
    private ArrayList<CommandExecutionThread> workerThreads = new ArrayList<CommandExecutionThread>();
    private Bank bank;
    
    
    
    public BankServerImpl(Bank bank, int threadCount, boolean isBlockedExecuted){
        
        this.bank = bank;
        this.threadCount = threadCount;

        // init thy threads
        for(int i = 0; i < threadCount; i++){
            workerThreads.add( new CommandExecutionThread(bank, commandQueue, isBlockedExecuted ) );
        }
        
        // start your engines
        for(CommandExecutionThread thread : workerThreads){
            thread.start();
        }
        
    }
    
    @Override
    public void execute(Command command) {
        //This simply adds the given Command to the queue of commands
        
        synchronized(commandQueue){
            commandQueue.add(command);
            commandQueue.notifyAll();
        }
    }

    @Override
    public void stop() throws InterruptedException {
       
        synchronized(commandQueue){
            for(int i = 0; i < threadCount; i++){
               commandQueue.add(new CommandStop());
            }
            commandQueue.notifyAll();
        }
        
        for(CommandExecutionThread thread : workerThreads){
            thread.join();
        }
        
    }

    @Override
    public long totalBalances() {
        // invoking Bank.totalBalances()
        return bank.totalBalances();
    }

}
