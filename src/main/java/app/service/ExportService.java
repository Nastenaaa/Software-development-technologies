package app.service;

import app.model.User;
import app.repo.TransactionRepository;
import app.patterns.decorator.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class ExportService {
    public enum Format { CSV }

    private final Exporter exporter;


    public ExportService(TransactionRepository txRepo) {
        Objects.requireNonNull(txRepo, "txRepo");
        Exporter base = new CsvExporter(txRepo);
        Exporter withTotals = new FooterTotalsDecorator(base, txRepo);
        this.exporter = new FooterTotalsDecorator(base, txRepo);
    }
    public ExportService(Exporter exporter) {
        this.exporter = Objects.requireNonNull(exporter);
    }

    public File exportTransactions(User user, LocalDate from, LocalDate to, Format format)
            throws IOException {
        if (format != Format.CSV) {
            throw new UnsupportedOperationException("CSV only");
        }
        return exporter.export(user, from, to);
    }
}
