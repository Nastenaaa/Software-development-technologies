package app.service;

import app.model.RecurringTransaction;
import app.model.Transaction;
import app.repo.RecurringTransactionRepository;
import app.repo.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RecurringService {
    private final RecurringTransactionRepository rtxRepo;
    private final TransactionRepository txRepo;

    public RecurringService(RecurringTransactionRepository rtxRepo,
                            TransactionRepository txRepo) {
        this.rtxRepo = Objects.requireNonNull(rtxRepo);
        this.txRepo = Objects.requireNonNull(txRepo);
    }

    public int runDueRules(LocalDate today) {
        List<RecurringTransaction> due = rtxRepo.findDue(today);
        int created = 0;
        for (RecurringTransaction r : due) {
            Transaction t = r.getTransaction();
            Transaction instance = new Transaction(0, t.getAccount(), today, t.getAmount(),
                    t.getType(), t.getCategory(), t.getNote(), t.getUser());
            txRepo.save(instance);
            created++;
        }
        return created;
    }
}

