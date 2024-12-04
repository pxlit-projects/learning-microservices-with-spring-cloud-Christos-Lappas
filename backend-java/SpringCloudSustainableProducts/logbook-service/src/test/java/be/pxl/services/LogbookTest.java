package be.pxl.services;

import be.pxl.services.domain.Log;
import be.pxl.services.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class LogbookTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogRepository logRepository;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @AfterEach
    public void setUp() {
        logRepository.deleteAll();
    }

    @Test
    public void testCreateLog() throws Exception {
        String changes = "Changed price from 10.99 to 12.99";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changes))
                .andExpect(status().isCreated());

        assertEquals(1, logRepository.findAll().size());
    }

    @Test
    public void testGetLogs() throws Exception {
        Log log = Log.builder()
                .time(LocalDateTime.now())
                .user("customer")
                .changes("Product updated")
                .build();

        Log log2 = Log.builder()
                .time(LocalDateTime.now())
                .user("customer")
                .changes("Product updated")
                .build();

        logRepository.save(log);
        logRepository.save(log2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/log")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertEquals(2, logRepository.findAll().size());
    }
}
