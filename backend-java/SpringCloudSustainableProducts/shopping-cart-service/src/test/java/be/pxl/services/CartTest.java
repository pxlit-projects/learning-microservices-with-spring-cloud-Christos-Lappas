package be.pxl.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.CartRequest;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.domain.dto.OrderCartRequest;
import be.pxl.services.repository.CartRepository;
import be.pxl.services.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CartTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepository cartRepository;

    @MockBean
    private ProductClient productClient;

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
        cartRepository.deleteAll();
    }

    @Test
    public void testCreateCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart")).andExpect(status().isCreated());

        assertEquals(1, cartRepository.findAll().size());
    }

    @Test
    public void testGetCart() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.valueOf(100.00));
        cart.setOrdered(false);
        cartRepository.save(cart);

        assertEquals(1, cartRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/" + cart.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cart.getId()))
                .andExpect(jsonPath("$.customer").value("Test Customer"))
                .andExpect(jsonPath("$.total").value(100.00))
                .andExpect(jsonPath("$.ordered").value(false));
    }

    @Test
    public void testGetCartWithWrongId() throws Exception {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.valueOf(100.00));
        cart.setOrdered(false);
        cartRepository.save(cart);

        assertEquals(1, cartRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/" + 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddItemToCart() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .price(BigDecimal.valueOf(1500.00))
                .category(Category.ELECTRONICS)
                .score(Score.A)
                .build();

        Mockito.when(productClient.getProduct(product.getId())).thenReturn(product);


        CartRequest cartRequest = new CartRequest();
        cartRequest.setId(cart.getId());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/product/add/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(status().isOk()).andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CartResponse responseCart = objectMapper.readValue(jsonResponse, CartResponse.class);

        assertTrue(responseCart.getItems().stream()
                .anyMatch(item -> item.getName().equals("Laptop") && item.getPrice().compareTo(BigDecimal.valueOf(1500.00)) == 0));
    }

    @Test
    public void testAddItemToCartWithWrongId() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .price(BigDecimal.valueOf(1500.00))
                .category(Category.ELECTRONICS)
                .score(Score.A)
                .build();

        Mockito.when(productClient.getProduct(product.getId())).thenReturn(product);


        CartRequest cartRequest = new CartRequest();
        cartRequest.setId(cart.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/product/add/" + 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRemoveItemFromCart() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .price(BigDecimal.valueOf(1500.00))
                .category(Category.ELECTRONICS)
                .score(Score.A)
                .build();


        CartRequest cartRequest = new CartRequest();
        cartRequest.setId(cart.getId());
        cartRequest.addItem(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/product/remove/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(status().isOk());

        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        assertFalse(updatedCart.getItems().contains(product));
    }

    @Test
    public void testOrderCart() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.valueOf(200.00));
        cart.setOrdered(false);
        cartRepository.save(cart);

        OrderCartRequest orderCartRequest = new OrderCartRequest(cart.getTotal(), true);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/" + cart.getId() + "/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCartRequest)))
                .andExpect(status().isOk());

        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        assertTrue(updatedCart.isOrdered());
    }

    @Test
    public void testDeleteCart() throws Exception {
        Cart cart = new Cart();
        cart.setCustomer("Test Customer");
        cart.setTotal(BigDecimal.valueOf(200.00));
        cartRepository.save(cart);

        assertEquals(1, cartRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/" + cart.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, cartRepository.findAll().size());
    }

}
