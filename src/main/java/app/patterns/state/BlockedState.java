package app.patterns.state;

import java.math.BigDecimal;

public class BlockedState implements AccountState {
    @Override
    public void ensureCanPost(String trxType, BigDecimal amount) {
        if ("EXPENSE".equalsIgnoreCase(trxType)) {
            throw new IllegalStateException("Рахунок заблоковано для витрат");
        }
    }

    @Override
    public String getName() {
        return "BLOCKED";
    }
}

