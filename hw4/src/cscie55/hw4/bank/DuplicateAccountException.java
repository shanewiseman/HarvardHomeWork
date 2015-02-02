package cscie55.hw4.bank;

public class DuplicateAccountException extends Exception
{
    public DuplicateAccountException(int accountId)
    {
        super(String.format("Attempt to add a second account with id %d", accountId));
    }
}
