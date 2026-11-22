package dio.budgeting.domain;

import java.util.Objects;

public final class Transaction {
    private final TransactionId id;
    private final String description;
    private final long amount;
    private final Category category;

    public Transaction(String description, long amount, Category category) {
        this(new TransactionId(), description, amount, category);
    }

    public Transaction(TransactionId id, String description, long amount, Category category) {
        this.id = Objects.requireNonNull(id, "O identificador é obrigatório");
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição é obrigatória");
        }
        if (description.length() > 120) {
            throw new IllegalArgumentException("A descrição deve ter no máximo 120 caracteres");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }
        this.description = description.trim();
        this.amount = amount;
        this.category = Objects.requireNonNull(category, "A categoria é obrigatória");
    }

    public TransactionId getId() { return id; }
    public String getDescription() { return description; }
    public long getAmount() { return amount; }
    public Category getCategory() { return category; }
}
