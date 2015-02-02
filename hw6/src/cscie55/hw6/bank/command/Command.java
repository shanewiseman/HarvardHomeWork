package cscie55.hw6.bank.command;

import cscie55.hw6.bank.Bank;
import cscie55.hw6.bank.DuplicateAccountException;
import cscie55.hw6.bank.InsufficientFundsException;

/**
 * Commands that can be executed by the bank. Each Command object has a string representation suitable for transmission
 * between client and server. Command.asString() yields the string representation. Command.parse(String) parses the
 * command string and returns the corresponding command.
 */

public abstract class Command
{
    /**
     * Parse the input string and return the described Command.
     * Formats:
     * - CREATE_ACCOUNT <ACCOUNT_ID>
     * - DELETE_ALL_ACCOUNTS
     * - DEPOSIT <ACCOUNT_ID> <AMOUNT>
     * - TRANSFER <FROM_ACCOUNT_ID> <TO_ACCOUNT_ID> <AMOUNT>
     * - TOTAL_BALANCES
     * @param input Description of command to be executed.
     * @return A Command that, when executed, will carry out the described operation.
     */
    public static Command parse(String input)
    {
        String[] commandData;

        try{
            // i decided against the tokenizer..
            commandData = input.split(" ");
        
            switch(commandData[0]){
                case "DEPOSIT" :
                    return deposit(
                            Integer.parseInt(commandData[1]),Integer.parseInt(commandData[2]));
                case "TRANSFER" :
                    return transfer(
                            Integer.parseInt(commandData[1]),Integer.parseInt(commandData[2]),Long.parseLong(commandData[3]));
                case "TOTAL_BALANCES" :
                    return totalBalances();
                case "CREATE_ACCOUNT":
                    return createAccount(Integer.parseInt(commandData[1] ));
                case "DELETE_ALL_ACCOUNTS" :
                    return deleteAllAccounts();
                default :
                    throw new RuntimeException();
            }
        } catch( Exception ex){
            System.out.println("FOOBAR");
            throw new RuntimeException();
        }
        
    }

    /**
     * Returns a String describing the Command.
     * @return a String describing the Command.
     */
    public abstract String asString();

    /**
     * Execute this Command on the given Bank.
     * @param bank The bank whose Accounts will be operated on by this Command.
     * @return Result to be sent back to Command requester. May be null.
     * @throws cscie55.hw6.bank.InsufficientFundsException
     */
    public abstract String execute(Bank bank) throws InsufficientFundsException, DuplicateAccountException;

    /**
     * Returns a DELETE_ALL_ACCOUNTS command.
     * @return a DELETE_ALL_ACCOUNTS Command.
     */
    public static Command deleteAllAccounts()
    {
        return new CommandDeleteAllAccounts();
    }

    /**
     * Returns a CREATE_ACCOUNT command.
     * @param accountId Account identifier, unique within the bank.
     * @return a CREATE_ACCOUNT Command.
     */
    public static Command createAccount(int accountId)
    {
        return new CommandCreateAccount(accountId);
    }

    /**
     * Returns a TRANSFER Command.
     * @param fromAccountId The identifier of the Account from which funds will be withdrawn.
     * @param toAccountId The identifer of the Account to which funds will be deposited.
     * @param amount The amount to be transferred.
     * @return a TRANSFER Command.
     */
    public static Command transfer(int fromAccountId, int toAccountId, long amount)
    {
        return new CommandTransfer(fromAccountId, toAccountId, amount);
    }


    /**
     * Returns a DEPOSIT Command.
     * @param accountId Identifies the Account to which funds will be deposited.
     * @param amount The amount to be deposited.
     * Returns a DEPOSIT Command.
     */
    public static Command deposit(int accountId, long amount)
    {
        return new CommandDeposit(accountId, amount);
    }

    /**
     * Returns a TOTAL_BALANCES Command.
     * @return a TOTAL_BALANCES Command.
     */
    public static Command totalBalances()
    {
        return TOTAL_BALANCES;
    }

    protected void simulateNetworkDelay()
    {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // Should not happen
            e.printStackTrace();
        }
    }

    private static final Command TOTAL_BALANCES = new CommandTotalBalances();
}
