package dio.budgeting.infrastructure.persistence.entity;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "financial_transaction")
public class TransactionEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String description;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    protected TransactionEntity() { }

    private TransactionEntity(UUID id, String description, long amount, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public static TransactionEntity from(Transaction transaction) {
        return new TransactionEntity(transaction.getId().uuid(), transaction.getDescription(),
                transaction.getAmount(), transaction.getCategory());
    }

    public Transaction toDomain() {
        return new Transaction(new TransactionId(id), description, amount, category);
    }
}
