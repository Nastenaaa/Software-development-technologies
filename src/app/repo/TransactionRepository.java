package app.repo;

import app.model.*;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> findByAccount(Account account);
    List<Transaction> findByUserAndPeriod(User user, LocalDate from, LocalDate to);
    Transaction save(Transaction t);
}
