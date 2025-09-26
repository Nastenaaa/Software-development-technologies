package app.repo.jdbc;

import app.db.Db;
import app.model.Account;
import app.model.Category;
import app.model.Transaction;
import app.model.User;
import app.repo.TransactionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TransactionRepositoryJdbc implements TransactionRepository {

    @Override
    public Transaction save(Transaction t) {
        final String sql = """
            INSERT INTO transactions (account_id, category_id, amount, trx_type, trx_date, note)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
        """;
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, t.getAccount().getId());
            if (t.getCategory() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, t.getCategory().getId());
            ps.setBigDecimal(3, t.getAmount());
            ps.setString(4, t.getType());
            ps.setObject(5, t.getDate()); // LocalDate -> JDBC
            ps.setString(6, t.getNote());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
            return t;
        } catch (SQLException e) {
            throw new RuntimeException("save transaction failed", e);
        }
    }

    @Override
    public List<Transaction> findByUserAndPeriod(User user, LocalDate from, LocalDate to) {
        final String sql = """
            SELECT
                t.id,
                t.amount,
                t.trx_type,
                t.trx_date,
                t.note,
                a.id   AS account_id,
                a.name AS account_name,
                c.id   AS category_id,
                c.name AS category_name
            FROM transactions t
            JOIN accounts a       ON a.id = t.account_id
            LEFT JOIN categories c ON c.id = t.category_id
            WHERE a.user_id = ? AND t.trx_date BETWEEN ? AND ?
            ORDER BY t.trx_date DESC, t.id DESC
        """;

        List<Transaction> list = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ps.setObject(2, from);
            ps.setObject(3, to);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // account
                    Account acc = new Account();
                    acc.setId(rs.getInt("account_id"));
                    acc.setName(rs.getString("account_name"));

                    // category (може бути null)
                    Category cat = null;
                    int catId = rs.getInt("category_id");
                    if (!rs.wasNull()) {
                        cat = new Category();
                        cat.setId(catId);
                        cat.setName(rs.getString("category_name"));
                    }

                    // transaction
                    Transaction t = new Transaction();
                    t.setId(rs.getInt("id"));
                    t.setAccount(acc);
                    t.setCategory(cat);
                    t.setAmount(rs.getBigDecimal("amount"));
                    t.setType(rs.getString("trx_type"));
                    t.setDate(rs.getObject("trx_date", LocalDate.class));
                    t.setNote(rs.getString("note"));
                    t.setUser(user);

                    list.add(t);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findByUserAndPeriod failed", e);
        }
    }

    @Override
    public Map<Integer, BigDecimal> balancesByUser(int userId) {
        final String sql = """
            SELECT a.id AS account_id, COALESCE(SUM(t.amount), 0) AS balance
            FROM accounts a
            LEFT JOIN transactions t ON t.account_id = a.id
            WHERE a.user_id = ?
            GROUP BY a.id
            ORDER BY a.id
        """;
        Map<Integer, BigDecimal> map = new LinkedHashMap<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("account_id"), rs.getBigDecimal("balance"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("balancesByUser failed", e);
        }
        return map;
    }
}
