package app.repo.jdbc;

import app.db.Db;
import app.model.Account;
import app.model.User;
import app.repo.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryJdbc implements AccountRepository {

    @Override
    public List<Account> findByUser(User user) {
        String sql = "SELECT id, name, currency, type FROM accounts WHERE user_id = ? ORDER BY id";
        List<Account> list = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setId(rs.getInt("id"));
                    a.setUser(user);
                    a.setName(rs.getString("name"));
                    a.setCurrency(rs.getString("currency"));
                    a.setType(rs.getString("type"));
                    list.add(a);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("findByUser failed", e);
        }
    }

    @Override
    public Optional<Account> findById(int id) {
        String sql = "SELECT id, user_id, name, currency, type FROM accounts WHERE id = ?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User(); u.setId(rs.getInt("user_id"));
                    Account a = new Account(
                            rs.getInt("id"),
                            u,
                            rs.getString("name"),
                            rs.getString("currency"),
                            rs.getString("type")
                    );
                    return Optional.of(a);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    @Override
    public Account save(Account a) {
        if (a.getId() == 0) {
            String sql = "INSERT INTO accounts(user_id, name, currency, type) VALUES (?,?,?,?) RETURNING id";
            try (Connection c = Db.get();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, a.getUser().getId());
                ps.setString(2, a.getName());
                ps.setString(3, a.getCurrency());
                ps.setString(4, a.getType());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) a.setId(rs.getInt(1));
                }
                return a;
            } catch (Exception e) {
                throw new RuntimeException("insert account failed", e);
            }
        } else {
            String sql = "UPDATE accounts SET name=?, currency=?, type=? WHERE id=?";
            try (Connection c = Db.get();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, a.getName());
                ps.setString(2, a.getCurrency());
                ps.setString(3, a.getType());
                ps.setInt(4, a.getId());
                ps.executeUpdate();
                return a;
            } catch (Exception e) {
                throw new RuntimeException("update account failed", e);
            }
        }
    }
}
