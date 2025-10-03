package app.patterns.state;

import java.math.BigDecimal;

public class ActiveState implements AccountState {
    @Override
    public void ensureCanPost(String trxType, BigDecimal amount) {
    }

    @Override
    public String getName() {
        return "ACTIVE";
    }
}
