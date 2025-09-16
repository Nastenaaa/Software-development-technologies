package app.service;

import app.model.*;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class FinanceService {
    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public FinanceService(TransactionRepository txRepo, AccountRepository accountRepo) {
        this.txRepo = Objects.requireNonNull(txRepo);
        this.accountRepo = Objects.requireNonNull(accountRepo);
    }

    public Transaction addExpense(User user, Account account, Category category,
                                  BigDecimal amount, LocalDate date, String note) {
        Transaction t = new Transaction(0, account, date, amount.negate(), "EXPENSE",
                category, note, user);
        return txRepo.save(t);
    }

    public Transaction addIncome(User user, Account account, Category category,
                                 BigDecimal amount, LocalDate date, String note) {
        Transaction t = new Transaction(0, account, date, amount, "INCOME",
                category, note, user);
        return txRepo.save(t);
    }
}

