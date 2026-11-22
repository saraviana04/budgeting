package dio.budgeting.infrastructure.ai;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.application.SumTransactionsByCategoryUseCase;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Profile("openai")
public class AiConfiguration {
    @Bean
    ChatClient budgetingChatClient(
            ChatClient.Builder builder,
            @Value("classpath:prompts/system-message.st") Resource systemPrompt,
            PersistTransactionUseCase persist,
            ListTransactionsByCategoryUseCase list,
            SumTransactionsByCategoryUseCase sum) throws IOException {
        return builder
                .defaultSystem(systemPrompt.getContentAsString(StandardCharsets.UTF_8))
                .defaultTools(persist, list, sum)
                .build();
    }
}
