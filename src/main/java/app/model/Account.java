package app.model;

import app.patterns.state.*;

public class Account {
    private int id;
    private User user;
    private String name;
    private String currency;
    private String type;

    private AccountState state = new ActiveState();

    public Account() {}

    public Account(int id, User user, String name, String currency, String type) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.currency = currency;
        this.type = type;
    }


    public void activate() {
        this.state = new ActiveState();
    }

    public void block() {
        this.state = new BlockedState();
    }

    public void close() {
        this.state = new ClosedState();
    }

    public AccountState getState() {
        return state;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Account{id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", name='" + name + "', currency='" + currency + "', type='" + type + "'" +
                ", state=" + (state != null ? state.getName() : "null") +
                "}";
    }
}
