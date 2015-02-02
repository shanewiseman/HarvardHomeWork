package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.InsufficientFundsException;

public class CommandTotalBalances extends Command
{
    @Override
    public String execute(Bank bank) throws InsufficientFundsException
    {
        simulateNetworkDelay();
        return Long.toString(bank.totalBalances());
    }

    @Override
    public String asString()
    {
       return NAME;
    }

    public static final String NAME = "TOTAL_BALANCES";
}
