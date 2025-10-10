package app.patterns.decorator;

import app.model.Transaction;
import app.model.User;
import app.repo.TransactionRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvExporter implements Exporter {
    private final TransactionRepository txRepo;

    public CsvExporter(TransactionRepository txRepo) {
        this.txRepo = Objects.requireNonNull(txRepo);
    }
    @Override
    public File export(User user, LocalDate from, LocalDate to) throws IOException {
        List<Transaction> data = txRepo.findByUserAndPeriod(user, from, to);

        File out = File.createTempFile("transactions-", ".csv");
        final String SEP = ";";

        try (OutputStream fos = new FileOutputStream(out);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write('\uFEFF');

            // Заголовок
            bw.write(Stream.of("date","account","category","type","amount","note")
                    .collect(Collectors.joining(SEP)));
            bw.write("\r\n");

            for (Transaction t : data) {
                String line = Stream.of(
                        t.getDate() != null ? t.getDate().toString() : "",
                        safe(csvText(t.getAccount()  != null ? t.getAccount().getName()  : "")),
                        safe(csvText(t.getCategory() != null ? t.getCategory().getName() : "")),
                        safe(csvText(t.getType())),
                        t.getAmount() != null ? t.getAmount().toString() : "",
                        safe(csvText(t.getNote()))
                ).collect(Collectors.joining(SEP));

                bw.write(line);
                bw.write("\r\n");
            }
        }
        return out;
    }
    private static String safe(String s) {
        return "\"" + (s == null ? "" : s) + "\"";
    }
    private static String csvText(String s) {
        return s == null ? "" : s.replace("\"", "\"\"");
    }
}
