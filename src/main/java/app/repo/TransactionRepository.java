package app.repo;

import app.model.User;
import app.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionRepository {
    Transaction save(Transaction t);
    List<Transaction> findByUserAndPeriod(User user, LocalDate from, LocalDate to);
    Map<Integer, BigDecimal> balancesByUser(int userId);
    BigDecimal balanceForAccount(int accountId);
}

