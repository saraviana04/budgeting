package dio.budgeting.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {
    @Test
    void createsValidTransaction() {
        var transaction = new Transaction("Supermercado", 8050, Category.GROCERIES);
        assertThat(transaction.getAmount()).isEqualTo(8050);
        assertThat(transaction.getDescription()).isEqualTo("Supermercado");
        assertThat(transaction.getId().uuid()).isNotNull();
    }

    @Test
    void rejectsNonPositiveAmount() {
        assertThatThrownBy(() -> new Transaction("Teste", 0, Category.AUTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maior que zero");
    }
}
