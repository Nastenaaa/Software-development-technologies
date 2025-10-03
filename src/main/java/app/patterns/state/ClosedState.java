package app.patterns.state;

import java.math.BigDecimal;

public class ClosedState implements AccountState {
    @Override
    public void ensureCanPost(String trxType, BigDecimal amount) {
        throw new IllegalStateException("Рахунок закрито. Операції неможливі.");
    }

    @Override
    public String getName() {
        return "CLOSED";
    }
}
