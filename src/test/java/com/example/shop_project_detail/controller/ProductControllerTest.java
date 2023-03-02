package com.example.shop_project_detail.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.service.OrderService;
import com.example.shop_project_detail.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    public void testAddNewProduct_success() throws Exception {
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setRetail_price(10);
        product.setWholesale_price(5);
        product.setStock_quantity(100);

        mockMvc.perform(post("/admin/addNewProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().string("Add product successfully"));

        verify(productService, times(1)).addNewProduct("Product 1", "Description 1", 10, 5, 100);
    }

    @Test
    public void testAddNewProduct_missingInformation() throws Exception {
        Product product = new Product();

        mockMvc.perform(post("/admin/addNewProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product information not complete"));

        verify(productService, times(0)).addNewProduct(anyString(), anyString(), (float) anyDouble(), (float) anyDouble(), anyInt());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(
        );

        when(productService.getAllProducts()).thenReturn(products);

        MvcResult result = mockMvc.perform(get("/user/allProducts"))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> responseProducts = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>(){});
        assertEquals(responseProducts, products);

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductDetail() throws Exception {
        Product product = new Product();

        when(productService.getProductById(1)).thenReturn(product);

        MvcResult result = mockMvc.perform(get("/user/product_detail?product_id=1"))
                .andExpect(status().isOk())
                .andReturn();

        Product responseProduct = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(responseProduct, product);

        verify(productService, times(1)).getProductById(1);
    }
}
