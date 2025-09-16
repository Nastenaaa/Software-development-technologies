package app.repo;

import app.model.Budget;
import app.model.User;
import java.util.List;

public interface BudgetRepository {
    List<Budget> findByUser(User user);
    Budget save(Budget b);
}

