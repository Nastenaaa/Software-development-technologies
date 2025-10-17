package app.patterns.bridge;

import app.model.Transaction;
import app.model.User;
import app.repo.TransactionRepository;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class TransactionsReport extends Report {
    private final User user;
    private final LocalDate from;
    private final LocalDate to;
    private final TransactionRepository txRepo;

    public TransactionsReport(ReportRenderer renderer,
                              User user, LocalDate from, LocalDate to,
                              TransactionRepository txRepo) {
        super(renderer);
        this.user = user;
        this.from = from;
        this.to = to;
        this.txRepo = txRepo;
    }

    @Override
    public File generate() {
        renderer.startReport("Transactions " + from + " .. " + to);
        renderer.addHeader(List.of("Date","Account","Category","Type","Amount","Note"));

        List<Transaction> data = txRepo.findByUserAndPeriod(user, from, to);
        for (Transaction t : data) {
            renderer.addRow(List.of(
                    t.getDate() == null ? "" : t.getDate().toString(),
                    t.getAccount()  != null ? t.getAccount().getName()  : "",
                    t.getCategory() != null ? t.getCategory().getName() : "",
                    t.getType(),
                    t.getAmount() != null ? t.getAmount().toString() : "",
                    t.getNote() == null ? "" : t.getNote()
            ));
        }
        return renderer.endReport();
    }
}
