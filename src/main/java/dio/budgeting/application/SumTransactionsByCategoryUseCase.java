package dio.budgeting.application;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SumTransactionsByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public SumTransactionsByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "sum-transactions-by-category", description = "Calcula o total gasto em uma categoria")
    public BigDecimal execute(
            @ToolParam(description = "Categoria: GROCERIES, PHARMA ou AUTO") Category category) {
        return BigDecimal.valueOf(transactionRepository.sumAmountByCategory(category), 2);
    }
}
