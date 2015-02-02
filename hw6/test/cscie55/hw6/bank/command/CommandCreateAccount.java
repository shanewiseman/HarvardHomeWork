package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Account;
import cscie55.hw6.bank.AccountImpl;
import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.DuplicateAccountException;

public class CommandCreateAccount extends Command
{
    @Override
    public String execute(Bank bank) throws DuplicateAccountException
    {
        simulateNetworkDelay();
        Account account = new AccountImpl(accountId);
        bank.addAccount(account);
        return null;
    }

    @Override
    public String asString()
    {
        return NAME + " " + accountId;
    }

    public CommandCreateAccount(int accountId)
    {
        this.accountId = accountId;
    }

    public static final String NAME = "CREATE_ACCOUNT";

    private final int accountId;
}
