package BankMenu;




import dao.AccountDAO;
import dao.TransactionDAO;
import dao.impl.AccountDAOImpl;
import dao.impl.TransactionDAOImpl;
import model.Account;
import model.Transaction;
import model.TransactionType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class BankMenu {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final Scanner scanner;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BankMenu() {
        this(new AccountDAOImpl(), new TransactionDAOImpl());
    }

    public BankMenu(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        System.out.println("=======================================");
        System.out.println("   Welcome to BANK CORR-APP SYSTEM     ");
        System.out.println("=======================================");

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> deposit();
                    case "3" -> withdraw();
                    case "4" -> transfer();
                    case "5" -> viewBalance();
                    case "6" -> viewTransactionHistory();
                    case "7" -> viewAllAccounts();
                    case "0" -> {
                        running = false;
                        System.out.println("Thank you for banking with us. Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("---------------- MENU ----------------");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Balance Inquiry");
        System.out.println("6. View Transaction History");
        System.out.println("7. View All Accounts");
        System.out.println("0. Exit");
        System.out.println("---------------------------------------");
        System.out.print("Select an option: ");
    }

    private void createAccount() throws SQLException {
        System.out.println();
        System.out.println("-- Create New Account --");

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        if (accountDAO.findByAccountNumber(accountNumber).isPresent()) {
            System.out.println("Account Already exists!");
            return;
        }

        System.out.print("Enter account holder name: ");
        String holderName = scanner.nextLine().trim();

        BigDecimal initialDeposit = readAmount("Enter initial deposit amount: ₽");
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return;
        }

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountHolderName(holderName);
        account.setBalance(initialDeposit);

        accountDAO.createAccount(account);

        if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(accountNumber, TransactionType.DEPOSIT, initialDeposit, initialDeposit, "Initial deposit");
        }

        System.out.println("Account created successfully !");
    }

    private void deposit() throws SQLException {
        System.out.println();
        System.out.println("-- Deposit --");

        Account account = findAccountOrPrompt();
        if (account == null) return;

        BigDecimal amount = readAmount("Enter deposit amount: ₽");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        accountDAO.updateBalance(account.getAccountNumber(), newBalance);
        recordTransaction(account.getAccountNumber(), TransactionType.DEPOSIT, amount, newBalance, "Cash deposit");


        System.out.println("Previous Balance: ₽" + account.getBalance());
        System.out.println("Deposit Amount: ₽ " + amount);
        System.out.println("New Balance: ₽" + newBalance);
        System.out.println();
        System.out.println("Deposit Successfully !");
    }

    private void withdraw() throws SQLException {
        System.out.println();
        System.out.println("-- Withdraw --");

        Account account = findAccountOrPrompt();
        if (account == null) return;

        BigDecimal amount = readAmount("Enter withdrawal amount: ₽");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        if (amount.compareTo(account.getBalance()) > 0) {
            System.out.println("Insufficient funds. Current balance: ₽" + account.getBalance());
            return;
        }
        BigDecimal MiniMumBalance = new BigDecimal("300.00");
        BigDecimal newBalance = account.getBalance().subtract(amount);

        if(newBalance.compareTo(MiniMumBalance) < 0) {
            System.out.println("Withdrawal Denied! ");
            return;
        }


        accountDAO.updateBalance(account.getAccountNumber(), newBalance);
        recordTransaction(account.getAccountNumber(), TransactionType.WITHDRAW, amount, newBalance, "Cash withdrawal");


        System.out.println("Previous Balance: ₽" + account.getBalance());
        System.out.println("Withdrawal Amount: ₽" + amount);
        System.out.println("New Balance: ₽" + newBalance);
        System.out.println();
        System.out.println("Withdraw Successfully !");
    }

    private void transfer() throws SQLException {
        System.out.println();
        System.out.println("-- Transfer --");

        System.out.print("Enter source account number: ");
        String fromNumber = scanner.nextLine().trim();
        Optional<Account> fromOpt = accountDAO.findByAccountNumber(fromNumber);
        if (fromOpt.isEmpty()) {
            System.out.println("Source account not found.");
            return;
        }

        System.out.print("Enter destination account number: ");
        String number = scanner.nextLine().trim();
        Optional<Account> toOpt = accountDAO.findByAccountNumber(number);
        if (toOpt.isEmpty()) {
            System.out.println("Destination account not found.");
            return;
        }

        if (fromNumber.equals(number)) {
            System.out.println("Source and destination accounts must be different.");
            return;
        }

        Account fromAccount = fromOpt.get();
        Account toAccount = toOpt.get();

        BigDecimal amount = readAmount("Enter transfer amount: ₽");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }

        if (amount.compareTo(fromAccount.getBalance()) > 0) {
            System.out.println("Insufficient funds. Current balance: " + fromAccount.getBalance());
            return;
        }

        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal toNewBalance = toAccount.getBalance().add(amount);

        String reference = generateReference();

        accountDAO.updateBalance(fromAccount.getAccountNumber(), fromNewBalance);
        accountDAO.updateBalance(toAccount.getAccountNumber(), toNewBalance);

        recordTransaction(fromAccount.getAccountNumber(), TransactionType.TRANSFER_OUT, amount, fromNewBalance,
                reference, "Transfer to " + toAccount.getAccountNumber());
        recordTransaction(toAccount.getAccountNumber(), TransactionType.TRANSFER_IN, amount, toNewBalance,
                reference, "Transfer from " + fromAccount.getAccountNumber());


        System.out.println("From Account: " + fromAccount.getAccountNumber());
        System.out.println("To Account: " + toAccount.getAccountNumber());
        System.out.println("Amount transferred: ₽" + fromNewBalance);
        System.out.println();
        System.out.println("Transfer Successfully !");
    }

    private void viewBalance() throws SQLException {
        Account account = findAccountOrPrompt();
        if (account == null) return;


        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Name: " + account.getAccountHolderName());
        System.out.println("Balance: ₽" + account.getBalance());
    }

    private void viewTransactionHistory() throws SQLException {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        if (accountDAO.findByAccountNumber(accountNumber).isEmpty()) {
            System.out.println("Account not found.");
            return;
        }

        List<Transaction> transactions = transactionDAO.findByAccountNumber(accountNumber);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.println();
        System.out.printf("%-15s %-14s %12s %14s %-20s %s%n",
                "Reference", "Transaction Type", "Amount", "Balance After", "Remarks", "Date");
        for (Transaction t : transactions) {

            System.out.printf("%-15s %-14s ₽%12.2f ₽%14.2f %-20s %s%n",
                    t.getReferenceNumber(),
                    t.getTransactionType().displayName(),
                    t.getAmount(),
                    t.getBalanceAfter(),
                    t.getRemarks() != null ? t.getRemarks() : "",
                    t.getCreatedAt() != null ? t.getCreatedAt().format(DATE_FORMAT) : "-");
        }
    }

    private void viewAllAccounts() throws SQLException {
        List<Account> accounts = accountDAO.findAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println();
        System.out.printf("%-15s %-25s ₽%12s%n", "Account #", "Holder Name", "Balance");
        for (Account a : accounts) {
            System.out.printf("%-15s %-25s ₽%12.2f%n",
                    a.getAccountNumber(), a.getAccountHolderName(), a.getBalance());
        }
    }



    private Account findAccountOrPrompt() throws SQLException {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        Optional<Account> accountOpt = accountDAO.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            System.out.println("Account not found.");
            return null;
        }
        return accountOpt.get();
    }

    private BigDecimal readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a numeric value.");
            }
        }
    }

    private String generateReference() {
        return "BCP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void recordTransaction(String accountNumber, TransactionType type, BigDecimal amount,
                                   BigDecimal balanceAfter, String remarks) throws SQLException {
        recordTransaction(accountNumber, type, amount, balanceAfter, generateReference(), remarks);
    }

    private void recordTransaction(String accountNumber, TransactionType type, BigDecimal amount,
                                   BigDecimal balanceAfter, String reference, String remarks) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setReferenceNumber(reference);
        transaction.setRemarks(remarks);
        transactionDAO.save(transaction);
    }
}



























