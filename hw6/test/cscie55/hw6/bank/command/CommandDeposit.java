package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.InsufficientFundsException;

public class CommandDeposit extends Command
{
    @Override
    public String execute(Bank bank) throws InsufficientFundsException
    {
        simulateNetworkDelay();
        bank.deposit(accountId, amount);
        return null;
    }

    @Override
    public String asString()
    {
        return NAME +" " + accountId + " " + amount;
    }

    public CommandDeposit(int accountId, long amount)
    {
        this.accountId = accountId;
        this.amount = amount;
    }

    public static final String NAME = "DEPOSIT";

    private final int accountId;
    private final long amount;
}
