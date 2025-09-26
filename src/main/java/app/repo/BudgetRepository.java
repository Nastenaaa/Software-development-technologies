package app.repo;

import app.model.Budget;
import app.model.User;   // ← додати цей імпорт

import java.util.List;

public interface BudgetRepository {
    List<Budget> findByUser(User user);
    Budget save(Budget b);
}



