
package cscie55.hw4;


import cscie55.hw4.bank.Account;
import cscie55.hw4.bank.AccountImpl;
import cscie55.hw4.bank.Bank;
import cscie55.hw4.bank.BankImpl;
import cscie55.hw4.bank.DuplicateAccountException;
import cscie55.hw4.bank.InsufficientFundsException;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/*
 * Unit tests for Banks and Accounts. All of the tests except for testPerformance test basic operations on
 * Bank and Account. Many of these tests expect an exception, so the test fails if control gets past the
 * tested operation. E.g. testZeroDeposit does the following:
 *
 *        Account account = new AccountImpl(0);
 *        try {
 *            account.deposit(0);
 *            fail();
 *        } catch (IllegalArgumentException e) {
 *            // Expected
 *        }
 *
 * Depositing 0 is not permitted, so IllegalArgumentException is expected. If that exception does not occur,
 * control continues to call fail() which causes the test to fail.
 *
 * testPerformance exersizes Bank and Account, trying different locking approaches, measuring time, and checking
 * correctness.
 */

public class Tester
{
    @Test
    public void testZeroDeposit()
    {
        Account account = new AccountImpl(0);
        try {
            account.deposit(0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeDeposit()
    {
        Account account = new AccountImpl(0);
        try {
            account.deposit(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPositiveDeposit()
    {
        Account account = new AccountImpl(0);
        try {
            account.deposit(1);
            assertEquals(1, account.balance());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testZeroWithdrawal()
    {
        Account account = new AccountImpl(0);
        try {
            account.withdraw(0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (InsufficientFundsException e) {
            fail();
        }
    }

    @Test
    public void testNegativeWithdrawal()
    {
        Account account = new AccountImpl(0);
        try {
            account.withdraw(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (InsufficientFundsException e) {
            fail();
        }
    }

    @Test
    public void testInsufficientFundsWithdrawal()
    {
        Account account = new AccountImpl(0);
        try {
            account.deposit(1);
            account.withdraw(2);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InsufficientFundsException e) {
            // Expected
        }
    }

    @Test
    public void testPositiveWithdrawal()
    {
        Account account = new AccountImpl(0);
        try {
            account.deposit(3);
            account.withdraw(1);
            assertEquals(2, account.balance());
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InsufficientFundsException e) {
            fail();
        }
    }

    @Test
    public void testDuplicateAccounts()
    {
        Bank bank = new BankImpl();
        Account account = new AccountImpl(0);
        try {
            bank.addAccount(account);
        } catch (DuplicateAccountException e) {
            fail();
        }
        try {
            bank.addAccount(account);
            fail();
        } catch (DuplicateAccountException e) {
            // Expected
        }
    }

    @Test
    public void testPerformance() throws InterruptedException, DuplicateAccountException
    {
        // Most java implementations use "just-in-time" compilation. Frequently used classes start out being
        // interpreted, and then get compiled as they are used more. This results in the most performance-critical
        // code running faster. warmup() exercises all the Bank and Account code to ensure that compilation occurs
        // before timings start.
        warmup();
        // Try each locking strategy
        for (LockStrategy lockStrategy : LockStrategy.values()) {
            Bank bank = createBank();
            // Try various numbers of threads
            for (int nThreads : THREADS) {
                // Create the test threads
                TestThread[] threads = new TestThread[nThreads];
                for (int t = 0; t < nThreads; t++) {
                    threads[t] = new TestThread(bank, lockStrategy, TRANSACTIONS / nThreads, t);
                }
                // Start the test threads
                Stopwatch stopwatch = new Stopwatch();
                for (TestThread thread : threads) {
                    thread.start();
                }
                // Wait for the threads to complete
                for (TestThread thread : threads) {
                    thread.join();
                }
                stopwatch.stop();
                // Report time measurements
                long nSec = stopwatch.nSec();
                double transactionsPerMsec = (1000000.0 * TRANSACTIONS) / nSec;
                if (bank.totalBalances() == ACCOUNTS * INITIAL_BALANCE) {
                    System.out.format("%s, %s -- OK: %f transactions/msec\n",
                                      lockStrategy, nThreads, transactionsPerMsec);
                } else {
                    System.out.format("%s, %s -- BROKEN: %f transactions/msec\tExpected total balances: %d\tActual: %d\n",
                                      lockStrategy, nThreads, transactionsPerMsec, ACCOUNTS * INITIAL_BALANCE, bank.totalBalances());
                }
            }
        }
    }

    private Bank createBank() throws DuplicateAccountException
    {
        Bank bank = new BankImpl();
        for (int id = 0; id < ACCOUNTS; id++) {
            Account account = new AccountImpl(id);
            account.deposit(INITIAL_BALANCE);
            bank.addAccount(account);
        }
        assert bank.totalBalances() == ACCOUNTS * INITIAL_BALANCE;
        return bank;
    }

    private void warmup() throws InterruptedException, DuplicateAccountException
    {
        for (LockStrategy lockStrategy : LockStrategy.values()) {
            Bank bank = createBank();
            TestThread thread = new TestThread(bank, lockStrategy, TRANSACTIONS, 0);
            thread.start();
            thread.join();
        }
    }

    private static final int[] THREADS = new int[]{1, 2, 5, 10, 20};
    private static final int INITIAL_BALANCE = 1000;
    private static final int ACCOUNTS = 100;
    private static final int TRANSACTIONS = 5000000;

    static enum LockStrategy
    {
        NO_LOCKING,
        LOCK_BANK,
        LOCK_ACCOUNTS
    }

    private static class TestThread extends Thread
    {
        @Override
        public void run()
        {
            for (int t = 0; t < transactions; t++) {
                boolean transferred;
                do {
                    // Pick two different accounts at random
                    int from = random.nextInt(bank.numberOfAccounts());
                    int to;
                    do {
                        to = random.nextInt(bank.numberOfAccounts());
                    } while (to == from);
                    // Pick a random transfer amount
                    long amount = random.nextInt(MAX_TRANSFER) + 1; // 1 .. MAX_TRANSFER
                    try {
                        // Do the transfer, using the appropriate method of bank for the current
                        // locking strategy.
                        switch (lockStrategy) {
                            case NO_LOCKING:
                                bank.transferWithoutLocking(from, to, amount);
                                break;
                            case LOCK_BANK:
                                bank.transferLockingBank(from, to, amount);
                                break;
                            case LOCK_ACCOUNTS:
                                bank.transferLockingAccounts(from, to, amount);
                                break;
                        }
                        transferred = true;
                    } catch (InsufficientFundsException e) {
                        // In case of insufficient funds, the transfer should not have happened. Try again, picking
                        // a (probably) different set of accounts and transfer amount.
                        transferred = false;
                    }
                } while (!transferred);
            }
        }

        public TestThread(Bank bank, Tester.LockStrategy lockStrategy, int transactions, int threadId)
        {
            this.bank = bank;
            this.lockStrategy = lockStrategy;
            this.transactions = transactions;
            this.random = new Random(threadId);
        }

        private static final int MAX_TRANSFER = 100;

        private final Bank bank;
        private final Tester.LockStrategy lockStrategy;
        private final int transactions;
        private final Random random;
    }

    public class Stopwatch
    {
        public void start()
        {
            start = System.nanoTime();
        }

        public long stop()
        {
            long stop = System.nanoTime();
            long delta = stop - start;
            accumulated += delta;
            return delta;
        }

        public long nSec()
        {
            return accumulated;
        }

        public Stopwatch()
        {
            start();
        }

        private long start;
        private long accumulated = 0;
    }
}
