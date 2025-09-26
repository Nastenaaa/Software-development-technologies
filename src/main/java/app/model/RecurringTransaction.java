package app.model;

/** Робить будь-яку Transaction рекурентною через розклад. */
public class RecurringTransaction {
    private int id;
    private Transaction transaction;
    private String cron;

    public RecurringTransaction() {}

    public RecurringTransaction(int id, Transaction transaction, String cron) {
        this.id = id;
        this.transaction = transaction;
        this.cron = cron;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }

    @Override
    public String toString() {
        return "RecurringTransaction{id=" + id +
                ", transaction=" + (transaction != null ? transaction.getId() : null) +
                ", cron='" + cron + "'}";
    }
}
