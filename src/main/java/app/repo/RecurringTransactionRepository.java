package app.repo;

import app.model.RecurringTransaction;

import java.time.LocalDate;
import java.util.List;

public interface RecurringTransactionRepository {
    List<RecurringTransaction> findDue(LocalDate today);
    RecurringTransaction save(RecurringTransaction r);
}

