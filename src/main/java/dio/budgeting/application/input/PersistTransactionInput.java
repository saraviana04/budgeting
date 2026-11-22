package dio.budgeting.application.input;

import dio.budgeting.domain.Category;
import org.springframework.ai.tool.annotation.ToolParam;

public record PersistTransactionInput(
        @ToolParam(description = "Descrição do gasto") String description,
        @ToolParam(description = "Valor do gasto em centavos. Exemplo: R$ 12,50 equivale a 1250") long amount,
        @ToolParam(description = "Categoria: GROCERIES, PHARMA ou AUTO") Category category) {
}
