package app.repo.jdbc;

import app.db.Db;
import app.model.User;
import app.repo.UserRepository;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryJdbc implements UserRepository {

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT id, email, full_name FROM users WHERE id = ?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    try { u.getClass().getMethod("setEmail", String.class).invoke(u, rs.getString("email")); } catch (Exception ignore) {}
                    try { u.getClass().getMethod("setFullName", String.class).invoke(u, rs.getString("full_name")); } catch (Exception ignore) {}
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, email, full_name FROM users WHERE email = ?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    try { u.getClass().getMethod("setEmail", String.class).invoke(u, rs.getString("email")); } catch (Exception ignore) {}
                    try { u.getClass().getMethod("setFullName", String.class).invoke(u, rs.getString("full_name")); } catch (Exception ignore) {}
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findByEmail failed", e);
        }
    }

    @Override
    public User save(User u) {
        if (u.getId() == 0) {
            String sql = "INSERT INTO users(email, full_name) VALUES (?, ?) RETURNING id";
            try (Connection c = Db.get();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                String email = null, fullName = null;
                try { email = (String) u.getClass().getMethod("getEmail").invoke(u); } catch (Exception ignore) {}
                try { fullName = (String) u.getClass().getMethod("getFullName").invoke(u); } catch (Exception ignore) {}
                ps.setString(1, email);
                ps.setString(2, fullName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) u.setId(rs.getInt(1));
                }
                return u;
            } catch (Exception e) {
                throw new RuntimeException("insert user failed", e);
            }
        }
        // update за потреби
        return u;
    }
}

