package app.service;

import app.model.User;
import app.repo.TransactionRepository;
import app.patterns.decorator.*;
import app.patterns.bridge.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class ExportService {


    public enum Format { CSV, HTML }

    private final Exporter exporter;
    private final TransactionRepository txRepo;

    public ExportService(TransactionRepository txRepo) {
        this.txRepo = Objects.requireNonNull(txRepo, "txRepo");
        Exporter base = new CsvExporter(txRepo);
        this.exporter = new FooterTotalsDecorator(base, txRepo);
    }

    public ExportService(Exporter exporter, TransactionRepository txRepo) {
        this.txRepo = Objects.requireNonNull(txRepo, "txRepo");
        this.exporter = Objects.requireNonNull(exporter);
    }

    public File exportTransactions(User user, LocalDate from, LocalDate to, Format format)
            throws IOException {

        switch (format) {
            case CSV -> {

                return exporter.export(user, from, to);
            }
            case HTML -> {
                ReportRenderer renderer = new HtmlReportRenderer();
                Report report = new TransactionsReport(renderer, user, from, to, txRepo);
                return report.generate();
            }
            default -> throw new UnsupportedOperationException("Невідомий формат: " + format);
        }
    }
}
