package app.repo;

import app.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    /** Усі категорії (наприклад для стартового заповнення). */
    List<Category> findAll();

    /** Категорії за типом транзакції: "INCOME" або "EXPENSE". */
    List<Category> findByType(String trxType);

    Optional<Category> findById(int id);
}

