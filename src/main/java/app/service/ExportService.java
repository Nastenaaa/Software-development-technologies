package app.service;

import app.model.Transaction;
import app.repo.TransactionRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExportService {
    public enum Format { CSV }

    private final TransactionRepository txRepo;

    public ExportService(TransactionRepository txRepo) {
        this.txRepo = Objects.requireNonNull(txRepo);
    }

    public File exportTransactions(app.model.User user, LocalDate from, LocalDate to, Format format)
            throws IOException {

        List<Transaction> data = txRepo.findByUserAndPeriod(user, from, to);

        File out = File.createTempFile("transactions-", ".csv");

        // UTF-8 + BOM + ; як роздільник
        final String SEP = ";";

        try (OutputStream fos = new FileOutputStream(out);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            // BOM, щоб Excel зрозумів UTF-8
            bw.write('\uFEFF');

            // Заголовок
            bw.write(Stream.of("date","account","category","type","amount","note")
                    .collect(Collectors.joining(SEP)));
            bw.write("\r\n");

            for (Transaction t : data) {
                String line = Stream.of(
                        t.getDate() != null ? t.getDate().toString() : "",
                        safe(csvText(t.getAccount() != null ? t.getAccount().getName() : "")),
                        safe(csvText(t.getCategory() != null ? t.getCategory().getName() : "")),
                        safe(csvText(t.getType())),
                        // amount як рядок — хай Excel сам розпізнає число
                        t.getAmount() != null ? t.getAmount().toString() : "",
                        safe(csvText(t.getNote()))
                ).collect(Collectors.joining(SEP));

                bw.write(line);
                bw.write("\r\n");
            }
        }

        return out;
    }

    // обгортаємо у лапки
    private static String safe(String s) {
        return "\"" + s + "\"";
    }

    // екрануємо лапки усередині
    private static String csvText(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }
}
