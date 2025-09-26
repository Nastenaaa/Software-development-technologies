package app.service;

import app.model.Account;
import app.model.Category;
import app.model.Transaction;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

    /** Баланси по рахунках користувача */
    public Map<Account, BigDecimal> getAccountBalances(User user) {
        // 1) отримаємо мапу accountId -> сума з репозиторію
        Map<Integer, BigDecimal> sums = txRepo.balancesByUser(user.getId());
        // 2) підтягнемо самі об’єкти Account
        List<Account> accounts = accountRepo.findByUser(user);

        Map<Account, BigDecimal> result = new LinkedHashMap<>();
        for (Account a : accounts) {
            BigDecimal bal = sums.getOrDefault(a.getId(), BigDecimal.ZERO);
            result.put(a, bal);
        }
        return result;
    }

    /** Загальний баланс по всіх рахунках користувача */
    public BigDecimal getTotalBalance(User user) {
        return getAccountBalances(user).values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
