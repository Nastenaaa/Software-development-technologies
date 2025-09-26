package app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private Account account;      // містить id + name
    private LocalDate date;
    private BigDecimal amount;
    private String type;          // "INCOME" / "EXPENSE"
    private Category category;    // містить id + name (може бути null)
    private String note;
    private User user;            // хто створив (поточний користувач)

    public Transaction() {}

    public Transaction(int id,
                       Account account,
                       LocalDate date,
                       BigDecimal amount,
                       String type,
                       Category category,
                       String note,
                       User user) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.note = note;
        this.user = user;
    }

    // ---------- core getters/setters ----------
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

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // ---------- convenience getters for export/UI ----------
    public Integer getAccountId() {
        return account != null ? account.getId() : null;
    }

    public String getAccountName() {
        return account != null ? account.getName() : null;
    }

    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + getAccountId() +
                ", accountName='" + getAccountName() + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", categoryId=" + getCategoryId() +
                ", categoryName='" + getCategoryName() + '\'' +
                ", note='" + note + '\'' +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }

    // (не обов'язково, але корисно)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
