package app.patterns.decorator;

import app.model.Transaction;
import app.model.User;
import app.repo.TransactionRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FooterTotalsDecorator extends AbstractExportDecorator {
    private final TransactionRepository txRepo;

    public FooterTotalsDecorator(Exporter inner, TransactionRepository txRepo) {
        super(inner);
        this.txRepo = Objects.requireNonNull(txRepo);
    }

    @Override
    public File export(User user, LocalDate from, LocalDate to) throws IOException {
        File csv = super.export(user, from, to);

        List<Transaction> data = txRepo.findByUserAndPeriod(user, from, to);
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Transaction t : data) {
            if (t == null || t.getAmount() == null || t.getType() == null) continue;
            if ("INCOME".equalsIgnoreCase(t.getType())) {
                income = income.add(t.getAmount());
            } else if ("EXPENSE".equalsIgnoreCase(t.getType())) {
                expense = expense.add(t.getAmount());
            }
        }
        BigDecimal net = income.add(expense);

        try (OutputStream fos = new FileOutputStream(csv, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write("\r\n");
            bw.write("TOTALS;;;;;\r\n");
            bw.write(String.format(",,,%s,%s,%s\r\n", "INCOME",  money(income),  "" ));
            bw.write(String.format(",,,%s,%s,%s\r\n", "EXPENSE", money(expense.abs()), "" ));
            bw.write(String.format(",,,%s,%s,%s\r\n", "NET",     money(net),     "" ));

        }

        return csv;
    }

    private static String money(BigDecimal x) {
        return (x == null ? "0.00" : x.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }
}
