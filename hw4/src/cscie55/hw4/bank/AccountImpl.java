package cscie55.hw4.bank;

public class AccountImpl implements Account {

    
    private       long balance = 0;
    private final int id;     
    
    
    public AccountImpl(int id){
        this.id = id; 
    }
    
    
    @Override
    public int id() {
        return id;
    }

    @Override
    public long balance() {
        return balance;
    }

    @Override
    public void deposit(long amount) {
        if(amount <= 0){
            throw new IllegalArgumentException(
                    String.format("%s Is An Incorrect Value for A Deposit" +
                            "(0 or Negative Number)", amount) );
        }
        
        balance += amount;
        
        //long is signed, if the balance overflows we need to notify
        if(balance < 0){
            throw new RuntimeException("Overflow Detected on Account: " + this.id());
        }
        
    }

    @Override
    public void withdraw(long amount) throws InsufficientFundsException {
        if(amount <= 0){
            throw new IllegalArgumentException(
                    String.format("%s Is An Incorrect Value for A WithDraw" +
                            "(0 or Negative Number)", amount) );
        }
        
        
        if(balance - amount < 0 ){
            throw new InsufficientFundsException(this, amount);
        }
        
        balance -= amount;
        
    }

}
