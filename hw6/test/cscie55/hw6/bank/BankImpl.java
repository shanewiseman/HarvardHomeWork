package cscie55.hw6.bank;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankImpl implements Bank {

    //Maps are atomic objects, no need to "sync" operations specific to Map
    Map<Integer,Account> existingAccounts = new ConcurrentHashMap<Integer,Account>();
    
    @Override
    public void addAccount(Account account) throws DuplicateAccountException {
        if(existingAccounts.containsKey(account.id())){
            throw new DuplicateAccountException(account.id());
        }
        
        existingAccounts.put(new Integer(account.id()), account);

    }

    @Override
    public long totalBalances() {

        long total = 0;
        
        synchronized(existingAccounts){
            for(Account account : existingAccounts.values()){
                total += account.balance();
            }
        }
        
        return total;    
    }

    @Override
    public void transfer(int fromAccountId, int toAccountId, long amount)
            throws InsufficientFundsException {
        
        Account fromAccount = existingAccounts.get(fromAccountId);
        Account toAccount   = existingAccounts.get(toAccountId);
        
        synchronized(fromAccount){
            fromAccount.withdraw(amount);
        }
        synchronized(toAccount){
            toAccount.deposit(amount);
        }
        
    }

    @Override
    public void deposit(int accountId, long amount) {
       
        Account account = existingAccounts.get(accountId);
        
        synchronized(account){
            account.deposit(amount);
        }
        
    }

    @Override
    public void deleteAllAccounts() {
        
        synchronized(existingAccounts){
            for(Integer account : existingAccounts.keySet() ){
                existingAccounts.remove(account);
            }
        }
        
    }

}
