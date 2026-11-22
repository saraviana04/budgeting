package dio.budgeting.infrastructure.http.request;

import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
        @NotBlank @Size(max = 120) String description,
        @NotNull Category category,
        @Positive long amount) {
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, amount, category);
    }
}
