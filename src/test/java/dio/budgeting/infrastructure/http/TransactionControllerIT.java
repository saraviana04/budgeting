package dio.budgeting.infrastructure.http;

import dio.budgeting.infrastructure.persistence.repository.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:budgeting-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class TransactionControllerIT {
    @Autowired MockMvc mockMvc;
    @Autowired TransactionEntityRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsListsAndSumsTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Supermercado","category":"GROCERIES","amount":8050}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(80.50));

        mockMvc.perform(get("/transactions/GROCERIES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Supermercado"));

        mockMvc.perform(get("/transactions/GROCERIES/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(80.50));
    }

    @Test
    void rejectsInvalidTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"","category":"GROCERIES","amount":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição inválida"));
    }
}
