package BankMenu;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.impl.AccountDAOImpl;
import dao.impl.TransactionDAOImpl;

import java.util.Scanner;

public class BankMenu {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final Scanner scanner;

    public BankMenu() {
        this(new AccountDAOImpl(), new TransactionDAOImpl());
    }

    public BankMenu(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        this.scanner = new Scanner(System.in);
    }
}
