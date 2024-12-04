package be.pxl.services;


import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
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

    @MockBean
    private RabbitTemplate rabbitTemplate;

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

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = Product.builder()
                .name("Laundry Detergent")
                .description("Cleans clothes effectively")
                .price(BigDecimal.valueOf(10.99))
                .stock(25)
                .category(Category.HOME)
                .score(Score.A)
                .build();

        productRepository.save(product);


        assertEquals(1, productRepository.findAll().size());


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertEquals(0, productRepository.findAll().size());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = Product.builder()
                .name("Laundry Detergent")
                .description("Cleans clothes effectively")
                .price(BigDecimal.valueOf(10.99))
                .stock(25)
                .category(Category.HOME)
                .score(Score.A)
                .build();

        productRepository.save(product);

        assertEquals(1, productRepository.findAll().size());

        ProductRequest updatedRequest = ProductRequest.builder()
                .name("Updated Detergent")
                .description("Even better cleaning power")
                .price(BigDecimal.valueOf(12.99))
                .stock(30)
                .category(Category.HOME)
                .score(Score.A)
                .build();



        doNothing().when(rabbitTemplate).convertAndSend("myQueue", String.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedRequest)))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(product.getId()).get();
        assertEquals("Updated Detergent", updatedProduct.getName());
        assertEquals("Even better cleaning power", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(12.99), updatedProduct.getPrice());
        assertEquals(30, updatedProduct.getStock());
    }

    @Test
    public void testAddLabelToProduct() throws Exception {
        Product product = Product.builder()
                .name("Laundry Detergent")
                .description("Cleans clothes effectively")
                .price(BigDecimal.valueOf(10.99))
                .stock(25)
                .category(Category.HOME)
                .score(Score.A)
                .build();

        productRepository.save(product);

        assertEquals(1, productRepository.findAll().size());

        String label = "Eco-Friendly";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/" + product.getId() + "/labels")
                        .param("label", label)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(product.getId()).get();
        assertTrue(updatedProduct.getLabels().contains(label));
    }

    @Test
    public void testRemoveLabelFromProduct() throws Exception {
        String label = "Eco-Friendly";

        Product product = Product.builder()
                .name("Laundry Detergent")
                .description("Cleans clothes effectively")
                .price(BigDecimal.valueOf(10.99))
                .stock(25)
                .category(Category.HOME)
                .score(Score.A)
                .labels(label)
                .build();

        productRepository.save(product);

        assertTrue(productRepository.findById(product.getId()).get().getLabels().contains(label));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/" + product.getId() + "/labels")
                        .param("label", label)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(product.getId()).get();
        assertFalse(updatedProduct.getLabels().contains(label));
    }
}
