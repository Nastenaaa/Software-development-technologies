package app.model;

import java.time.LocalDate;

public class Budget {
    private int id;
    private User user;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    public Budget() {}

    public Budget(int id, User user, LocalDate periodStart, LocalDate periodEnd) {
        this.id = id;
        this.user = user;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }

    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }

    @Override
    public String toString() {
        return "Budget{id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd + "}";
    }
}
