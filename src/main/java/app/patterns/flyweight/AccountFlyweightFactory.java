package app.patterns.flyweight;

import app.model.Account;
import app.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AccountFlyweightFactory {
    private static final Map<Integer, Account> CACHE = new ConcurrentHashMap<>();

    private AccountFlyweightFactory() {}

    public static Account get(int id, User user, String name, String currency, String type) {
        return CACHE.compute(id, (k, existing) -> {
            if (existing == null) {
                Account a = new Account();
                a.setId(id);
                a.setUser(user);
                a.setName(name);
                a.setCurrency(currency);
                a.setType(type);
                return a;
            } else {
                if (existing.getUser() == null && user != null) existing.setUser(user);
                if (existing.getName() == null && name != null) existing.setName(name);
                if (existing.getCurrency() == null && currency != null) existing.setCurrency(currency);
                if (existing.getType() == null && type != null) existing.setType(type);
                return existing;
            }
        });
    }

    public static int size() { return CACHE.size(); }
    public static void clear() { CACHE.clear(); }
}

