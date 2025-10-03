package app.service;

import app.model.Account;
import app.model.Category;
import app.model.Transaction;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;
import app.patterns.state.AccountState;   // підключаємо стан

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

        account.getState().ensureCanPost("EXPENSE", amount);


        BigDecimal balance = txRepo.balanceForAccount(account.getId());
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    "Недостатньо коштів на рахунку '" + account.getName() +
                            "'. Баланс: " + balance + ", сума витрати: " + amount
            );
        }
        Transaction t = new Transaction(0, account, date, amount.negate(), "EXPENSE",
                category, note, user);
        return txRepo.save(t);
    }
    public BigDecimal getCurrentBalance(Account account) {
        return txRepo.balanceForAccount(account.getId());
    }


    public Transaction addIncome(User user, Account account, Category category,
                                 BigDecimal amount, LocalDate date, String note) {

        account.getState().ensureCanPost("INCOME", amount);

        Transaction t = new Transaction(0, account, date, amount, "INCOME",
                category, note, user);
        return txRepo.save(t);
    }
}
