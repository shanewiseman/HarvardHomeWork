

import cscie55.hw6.bank.command.Command;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class Tester
{
    @Test
    public void testSetup() throws IOException, InterruptedException
    {
        runTest(0);
    }

    @Test
    public void testCorrectness() throws IOException, InterruptedException
    {
        runTest(CLIENT_THREADS);
    }

    private void runTest(int clientThreads)
        throws IOException, InterruptedException
    {
        setup();
        // Create the test threads
        TestThread[] threads = new TestThread[clientThreads];
        for (int t = 0; t < clientThreads; t++) {
            threads[t] = new TestThread(TRANSACTIONS / clientThreads, t);
        }
        // Start the test threads
        for (TestThread thread : threads) {
            thread.start();
        }
        // Wait for the threads to complete
        for (TestThread thread : threads) {
            thread.join();
        }
        // Check balances
        assertEquals(ACCOUNTS * INITIAL_BALANCE, totalBalances());
    }

    private void setup() throws IOException
    {
        // Get socket to server for setup
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true);
        // Delete all Accounts to get a clean start for the current test
        executeCommand(Command.deleteAllAccounts(), socketInput, socketOutput);
        // Create Accounts
        String commandOutput;
        for (int id = 0; id < ACCOUNTS; id++) {
            commandOutput = executeCommand(Command.createAccount(id), socketInput, socketOutput);
            assertEquals("", commandOutput);
            commandOutput = executeCommand(Command.deposit(id, INITIAL_BALANCE), socketInput, socketOutput);
            assertEquals("", commandOutput);
        }
    }

    private long totalBalances() throws IOException
    {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true);
        String output = executeCommand(Command.totalBalances(), socketInput, socketOutput);
        return Long.parseLong(output);
    }

    private String executeCommand(Command command, BufferedReader socketInput, PrintWriter socketOutput)
        throws IOException
    {
        socketOutput.println(command.asString());
        String commandOutput = socketInput.readLine();
        assertNotNull(commandOutput);
        if (commandOutput.startsWith("ERROR: ")) {
            System.out.format("%s -> %s\n", command.asString(), commandOutput);
        }
        return commandOutput;
    }

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9090;
    private static final int INITIAL_BALANCE = 1000;
    private static final int ACCOUNTS = 100;
    private static final int TRANSACTIONS = 10000;
    private static final int CLIENT_THREADS = 20;

    // Client-side thread for submitting commands to the BankServer, (similar to HW4, HW5).

    private static class TestThread extends Thread
    {
        @Override
        public void run()
        {
            for (int t = 0; t < transactions; t++) {
                // Pick two different accounts at random
                int from = random.nextInt(ACCOUNTS);
                int to;
                do {
                    to = random.nextInt(ACCOUNTS);
                } while (to == from);
                // Pick a random transfer amount
                long amount = random.nextInt(MAX_TRANSFER) + 1; // 1 .. MAX_TRANSFER
                socketOutput.println(Command.transfer(from, to, amount).asString());
                try {
                    socketInput.readLine();
                } catch (IOException e) {
                    fail();
                }
            }
            socketOutput.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public TestThread(int transactions, int threadId) throws IOException
        {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socketOutput = new PrintWriter(socket.getOutputStream(), true);
            this.transactions = transactions;
            this.random = new Random(threadId);
        }

        private static final int MAX_TRANSFER = 100;

        private final Socket socket;
        private final BufferedReader socketInput;
        private final PrintWriter socketOutput;
        private final int transactions;
        private final Random random;
    }
}
