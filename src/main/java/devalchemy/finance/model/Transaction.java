package devalchemy.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private String id;
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String description;
    private String accountId;

    public Transaction() {}

    public Transaction(LocalDate date, BigDecimal amount, String category, String description, String accountId) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.accountId = accountId;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getAccountId() { return accountId; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %s",
                id, date, amount, category == null ? "-" : category, description == null ? "-" : description);
    }
}
