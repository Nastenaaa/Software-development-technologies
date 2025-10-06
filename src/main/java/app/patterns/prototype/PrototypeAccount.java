package app.patterns.prototype;

import app.model.Account;

public class PrototypeAccount implements Prototype<Account> {
    private final Account src;

    private PrototypeAccount(Account src) { this.src = src; }

    public static PrototypeAccount from(Account a) {
        return new PrototypeAccount(a);
    }

    @Override
    public Account copy() {
        Account c = new Account();
        c.setUser(src.getUser());
        c.setName(src.getName());
        c.setCurrency(src.getCurrency());
        c.setType(src.getType());

        return c;
    }
}
