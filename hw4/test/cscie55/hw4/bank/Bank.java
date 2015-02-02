package cscie55.hw4.bank;

public interface Bank
{
    void addAccount(Account account) throws DuplicateAccountException;
    void transferWithoutLocking(int fromId, int toId, long amount) throws InsufficientFundsException;
    void transferLockingBank(int fromId, int toId, long amount) throws InsufficientFundsException;
    void transferLockingAccounts(int fromId, int toId, long amount) throws InsufficientFundsException;
    long totalBalances();
    int numberOfAccounts();
}
