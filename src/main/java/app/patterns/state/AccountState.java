package app.patterns.state;

import java.math.BigDecimal;

public interface AccountState {
    void ensureCanPost(String trxType, BigDecimal amount);
    String getName();
}

