package app.repo;

import app.model.Account;
import app.model.User;        // ← додай це

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findByUser(User user);
    Optional<Account> findById(int id);
    Account save(Account a);
}



