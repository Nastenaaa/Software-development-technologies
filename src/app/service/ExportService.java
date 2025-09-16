package app.service;

import app.model.Transaction;
import app.model.User;
import app.repo.TransactionRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ExportService {
    public enum Format { CSV }

    private final TransactionRepository txRepo;

    public ExportService(TransactionRepository txRepo) {
        this.txRepo = Objects.requireNonNull(txRepo);
    }

    public File exportTransactions(User user, LocalDate from, LocalDate to, Format format)
            throws IOException {
        List<Transaction> data = txRepo.findByUserAndPeriod(user, from, to);
        File out = File.createTempFile("transactions-", ".csv");
        try (FileWriter fw = new FileWriter(out)) {
            fw.write("date,account,category,type,amount,note\n");
            for (Transaction t : data) {
                fw.write(String.format("%s,%s,%s,%s,%s,%s%n",
                        t.getDate(),
                        t.getAccount() != null ? t.getAccount().getName() : "",
                        t.getCategory() != null ? t.getCategory().getName() : "",
                        t.getType(), t.getAmount(), t.getNote() != null ? t.getNote() : ""));
            }
        }
        return out;
    }
}

