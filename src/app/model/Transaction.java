package app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int id;
    private Account account;
    private LocalDate date;
    private BigDecimal amount;
    private String type;
    private Category category;
    private String note;
    private User createdBy;

    public Transaction() {}

    public Transaction(int id, Account account, LocalDate date, BigDecimal amount,
                       String type, Category category, String note, User createdBy) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.note = note;
        this.createdBy = createdBy;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account=" + (account != null ? account.getId() : null) +
                ", date=" + date +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", category=" + (category != null ? category.getId() : null) +
                ", note='" + note + '\'' +
                ", createdBy=" + (createdBy != null ? createdBy.getId() : null) +
                '}';
    }
}
