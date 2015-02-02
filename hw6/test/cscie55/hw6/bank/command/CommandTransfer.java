package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.InsufficientFundsException;

public class CommandTransfer extends Command
{
    @Override
    public String execute(Bank bank) throws InsufficientFundsException
    {
        simulateNetworkDelay();
        bank.transfer(fromAccountId, toAccountId, amount);
        return null;
    }

    @Override
    public String asString()
    {
        return NAME +" "+ fromAccountId + " " + toAccountId + " " + amount;
    }

    public CommandTransfer(int fromAccountId, int toAccountId, long amount)
    {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public static final String NAME = "TRANSFER";

    private final int fromAccountId;
    private final int toAccountId;
    private final long amount;
}
