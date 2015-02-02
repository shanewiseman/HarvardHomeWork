package cscie55.hw6.bank;

public class InsufficientFundsException extends BankException
{
    public InsufficientFundsException(Account account, long withdrawal)
    {
        super(String.format("ERROR: Attempt to withdraw %d from %s", withdrawal, account));
    }
}
