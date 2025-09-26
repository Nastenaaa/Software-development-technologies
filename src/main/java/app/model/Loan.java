package app.model;

import app.model.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {
    private int id;
    private Account account;
    private String type;
    private BigDecimal principal;
    private double rateAPR;
    private LocalDate startDate;
    private LocalDate endDate;

    public Loan() {}

    public Loan(int id, Account account, String type, BigDecimal principal,
                double rateAPR, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.principal = principal;
        this.rateAPR = rateAPR;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }

    public double getRateAPR() { return rateAPR; }
    public void setRateAPR(double rateAPR) { this.rateAPR = rateAPR; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "Loan{id=" + id +
                ", account=" + (account != null ? account.getId() : null) +
                ", type='" + type + '\'' +
                ", principal=" + principal +
                ", rateAPR=" + rateAPR +
                ", startDate=" + startDate +
                ", endDate=" + endDate + "}";
    }
}
