package devalchemy.finance.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private String id;
    private String name;
    private BigDecimal balance = BigDecimal.ZERO;

    public Account() {}

    public Account(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getBalance() { return balance; }

    public void applyTransaction(java.math.BigDecimal amount) {
        if (amount == null) return;
        this.balance = this.balance.add(amount);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s", id, name, balance);
    }
}
