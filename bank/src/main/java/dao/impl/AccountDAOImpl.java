package dao.impl;

import dao.AccountDAO;
import model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAOImpl implements AccountDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/cicc_bank_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
//private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());
//    private static final String SQL_INSERT =
//            "INSERT INTO accounts (account_number, account_name, balance) VALUES (?, ?, ?)";
//
//    private static final String SQL_FIND_BY_NUMBER =
//            "SELECT account_id, account_number, account_name, balance, created_at, updated_at " +
//                    "FROM accounts WHERE account_number = ?";
//
//    private static final String SQL_FIND_ALL =
//            "SELECT account_id, account_number, account_name, balance, created_at, updated_at " +
//                    "FROM accounts ORDER BY created_at ASC";
//
//    private static final String SQL_UPDATE_BALANCE =
//            "UPDATE accounts SET balance = ? WHERE account_number = ?";

    @Override
    public void createAccount(Account account) throws SQLException {
        String query = "INSERT INTO accounts (account_number, account_name, balance) VALUES (?, ?, ?)";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountHolderName());
            preparedStatement.setBigDecimal(3, account.getBalance());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        String query = "SELECT account_number, account_name, balance FROM accounts WHERE account_number = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setAccountNumber(resultSet.getString("account_number"));
                    account.setAccountHolderName(resultSet.getString("account_name"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    return Optional.of(account);
                }
                return Optional.empty();
            }
        }
    }


    @Override
    public List<Account> findAllAccounts() throws SQLException {
        String query = "SELECT account_number, account_name, balance FROM accounts";
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountNumber(resultSet.getString("account_number"));
                account.setAccountHolderName(resultSet.getString("account_name"));
                account.setBalance(resultSet.getBigDecimal("balance"));
                accounts.add(account);
            }
        }

        return accounts;
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal balance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setString(2, accountNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating balance failed, no account found with account number: " + accountNumber);
            }
        }
    }


}
