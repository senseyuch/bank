package dao.impl;


import dao.TransactionDAO;
import model.Transaction;
import model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/ciicc_db_b11bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Override
    public void save(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions " +
                "(account_number, transaction_type, amount, balance_after, reference_number, remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transaction.getAccountNumber());
            preparedStatement.setString(2, transaction.getTransactionType().name());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setBigDecimal(4, transaction.getBalanceAfter());
            preparedStatement.setString(5, transaction.getReferenceNumber());
            preparedStatement.setString(6, transaction.getRemarks());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Saving transaction failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) throws SQLException {
        String query = "SELECT transaction_id, account_number, transaction_type, amount, balance_after, " +
                "       reference_number, remarks, created_at " +
                "FROM transactions " +
                "WHERE account_number = ? " +
                "ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = connection();) {
            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, accountNumber);

            try (ResultSet resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapRow(resultSet));
                }
            }
            return transactions;


        }
    }

    @Override
    public List<Transaction> findRecentTransactions(String accountNumber, int limit) throws SQLException {
        return List.of();
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction txn = new Transaction();
        txn.setTransactionId(rs.getLong("transaction_id"));
        txn.setAccountNumber(rs.getString("account_number"));
        txn.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        txn.setAmount(rs.getBigDecimal("amount"));
        txn.setBalanceAfter(rs.getBigDecimal("balance_after"));
        txn.setReferenceNumber(rs.getString("reference_number"));
        txn.setRemarks(rs.getString("remarks"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            txn.setCreatedAt(createdAt.toLocalDateTime());
        }

        return txn;
    }

}
