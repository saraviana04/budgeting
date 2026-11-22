package dio.budgeting.domain;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID uuid) {
    public TransactionId {
        Objects.requireNonNull(uuid, "O identificador não pode ser nulo");
    }

    public TransactionId() {
        this(UUID.randomUUID());
    }
}
