package be.pxl.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import be.pxl.services.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

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
        productRepository.deleteAll();
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = Product.builder()
                .name("Wasmiddel")
                .description("Beste wasmiddel")
                .price(BigDecimal.valueOf(12.99))
                .stock(18)
                .category(Category.HOME)
                .score(Score.B)
                .build();

        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String name = productRepository.findById(product.getId()).get().getName();

        assertEquals("Wasmiddel", name);
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = Product.builder()
                .name("Wasmiddel")
                .description("Beste wasmiddel")
                .price(BigDecimal.valueOf(12.99))
                .stock(18)
                .category(Category.HOME)
                .score(Score.B)
                .build();

        String productString = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productString))
                .andExpect(status().isCreated());

        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    public void testGetProducts() throws Exception {
        Product product = Product.builder()
                .name("Wasmiddel")
                .description("Beste wasmiddel")
                .price(BigDecimal.valueOf(12.99))
                .stock(18)
                .category(Category.HOME)
                .score(Score.B)
                .build();

        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    public void testSearchProduct() throws Exception {
        Product product = Product.builder()
                .name("Wasmiddel")
                .description("Beste wasmiddel")
                .price(BigDecimal.valueOf(12.99))
                .stock(18)
                .category(Category.HOME)
                .score(Score.B)
                .build();

        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/filter?category=HOME&score=B&name=mid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Wasmiddel"));

        assertEquals(1, productRepository.findAll().size());
    }

    // TODO: Write more tests
}
