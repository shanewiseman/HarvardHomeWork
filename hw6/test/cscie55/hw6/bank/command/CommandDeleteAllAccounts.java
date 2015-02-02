package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.DuplicateAccountException;

public class CommandDeleteAllAccounts extends Command
{
    @Override
    public String execute(Bank bank) throws DuplicateAccountException
    {
        simulateNetworkDelay();
        bank.deleteAllAccounts();
        return null;
    }

    @Override
    public String asString()
    {
        return NAME;
    }

    public CommandDeleteAllAccounts()
    {
    }

    public static final String NAME = "DELETE_ALL_ACCOUNTS";
}
