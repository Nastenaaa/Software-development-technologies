package app.repo.jdbc;

import app.db.Db;
import app.model.Category;
import app.repo.CategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryJdbc implements CategoryRepository {

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name FROM categories ORDER BY name";
        List<Category> list = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getInt("id"));
                cat.setName(rs.getString("name"));
                list.add(cat);
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll categories failed", e);
        }
        return list;
    }

    @Override
    public List<Category> findByType(String trxType) {
        // Якщо в БД cat_type — ENUM transaction_type, лишаємо приведення:
        // якщо ти змінила колонку на VARCHAR, заміни умову на: "WHERE cat_type = ?"
        String sql = """
            SELECT id, name
            FROM categories
            WHERE cat_type = ?::transaction_type
            ORDER BY name
        """;
        List<Category> list = new ArrayList<>();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, trxType); // "INCOME" або "EXPENSE"
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category cat = new Category();
                    cat.setId(rs.getInt("id"));
                    cat.setName(rs.getString("name"));
                    list.add(cat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByType categories failed", e);
        }
        return list;
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category();
                    cat.setId(rs.getInt("id"));
                    cat.setName(rs.getString("name"));
                    return Optional.of(cat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById category failed", e);
        }
        return Optional.empty();
    }

    // Не обов'язково, але може знадобитись:
    public Category save(Category cgt, String catType /* "INCOME"/"EXPENSE" */) {
        String sql = """
            INSERT INTO categories(name, cat_type) VALUES (?, ?::transaction_type)
            ON CONFLICT (name) DO UPDATE SET cat_type = EXCLUDED.cat_type
            RETURNING id
        """;
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cgt.getName());
            ps.setString(2, catType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) cgt.setId(rs.getInt(1));
            }
            return cgt;
        } catch (SQLException e) {
            throw new RuntimeException("save category failed", e);
        }
    }
}
