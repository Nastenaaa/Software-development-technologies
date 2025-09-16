package app.model;

public class Attachment {
    private int id;
    private Transaction transaction;
    private String filePath;
    public Attachment() {}

    public Attachment(int id, Transaction transaction, String filePath) {
        this.id = id;
        this.transaction = transaction;
        this.filePath = filePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    @Override
    public String toString() {
        return "Attachment{id=" + id +
                ", transaction=" + (transaction != null ? transaction.getId() : null) +
                ", filePath='" + filePath + "'}";
    }
}
