package app.patterns.prototype;

import app.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PrototypeTransaction implements Prototype<Transaction> {
    private final Transaction src;

    private PrototypeTransaction(Transaction src) { this.src = src; }

    public static PrototypeTransaction from(Transaction t) {
        return new PrototypeTransaction(t);
    }

    @Override
    public Transaction copy() {
        Transaction t = new Transaction();
        t.setAccount(src.getAccount());
        t.setCategory(src.getCategory());
        t.setType(src.getType());
        t.setAmount(src.getAmount());
        t.setDate(src.getDate());
        t.setNote(src.getNote());
        t.setUser(src.getUser());
        return t;
    }


    public Transaction copyWith(LocalDate date, BigDecimal amount, String note) {
        Transaction t = copy();
        if (date   != null) t.setDate(date);
        if (amount != null) t.setAmount(amount);
        if (note   != null) t.setNote(note);
        return t;
    }
}

