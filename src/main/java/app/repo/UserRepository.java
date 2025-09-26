package app.repo;

import app.model.User;   // ✅ додаємо цей імпорт
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    User save(User u);
}

