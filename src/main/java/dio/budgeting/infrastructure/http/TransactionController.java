package dio.budgeting.infrastructure.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.application.SumTransactionsByCategoryUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.infrastructure.http.request.TransactionRequest;
import dio.budgeting.infrastructure.http.response.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final PersistTransactionUseCase persist;
    private final ListTransactionsByCategoryUseCase list;
    private final SumTransactionsByCategoryUseCase sum;

    public TransactionController(PersistTransactionUseCase persist,
                                 ListTransactionsByCategoryUseCase list,
                                 SumTransactionsByCategoryUseCase sum) {
        this.persist = persist;
        this.list = list;
        this.sum = sum;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody TransactionRequest request) {
        return TransactionResponse.from(persist.execute(request.toInput()));
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> list(@PathVariable Category category) {
        return list.execute(category).stream().map(TransactionResponse::from).toList();
    }

    @GetMapping("/{category}/total")
    public BigDecimal total(@PathVariable Category category) {
        return sum.execute(category);
    }
}
