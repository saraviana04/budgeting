package dio.budgeting.infrastructure.persistence.repository;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepository implements TransactionRepository {
    private final TransactionEntityRepository repository;

    public JpaTransactionRepository(TransactionEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(TransactionEntity.from(transaction)).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return repository.findAllByCategoryOrderById(category).stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }

    @Override
    public long sumAmountByCategory(Category category) {
        return repository.sumAmountByCategory(category);
    }
}
