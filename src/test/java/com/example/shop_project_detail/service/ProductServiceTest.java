package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.ProductDao;
import com.example.shop_project_detail.dao.WatchListDao;
import com.example.shop_project_detail.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductDao productDao;

    @Test
    public void addNewProductTest() {
        // Arrange
        Product product = new Product();
        product.setName("testProduct");
        product.setDescription("testDescription");
        product.setRetail_price(10.0f);
        product.setWholesale_price(5.0f);
        product.setStock_quantity(100);
        //when(productDao.addNewProduct(product.getName(), product.getDescription(), product.getRetail_price(), product.getWholesale_price(), product.getStock_quantity())).thenReturn(1);

        // Act
        productService.addNewProduct(product.getName(), product.getDescription(), product.getRetail_price(), product.getWholesale_price(), product.getStock_quantity());

        // Assert
        verify(productDao, times(1)).addNewProduct(product.getName(), product.getDescription(), product.getRetail_price(), product.getWholesale_price(), product.getStock_quantity());
    }

    @Test
    public void getAllProductsTest() {
        // Arrange
        Product product1 = new Product();
        product1.setName("product1");
        product1.setDescription("product1 description");
        product1.setRetail_price(10.0f);
        product1.setWholesale_price(5.0f);
        product1.setStock_quantity(100);

        Product product2 = new Product();
        product2.setName("product2");
        product2.setDescription("product2 description");
        product2.setRetail_price(20.0f);
        product2.setWholesale_price(15.0f);
        product2.setStock_quantity(200);

        List<Product> products = Arrays.asList(product1, product2);
        when(productDao.getAllProducts()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(products, result);
        verify(productDao, times(1)).getAllProducts();
    }

    @Test
    public void getProductByIdTest() {
        // Arrange
        int productId = 1;
        Product expectedProduct = new Product();
        expectedProduct.setProduct_id(productId);
        expectedProduct.setName("testProduct");
        expectedProduct.setDescription("testDescription");
        expectedProduct.setRetail_price(10.0f);
        expectedProduct.setWholesale_price(5.0f);
        expectedProduct.setStock_quantity(100);
        when(productDao.getProductById(productId)).thenReturn(expectedProduct);

        // Act
        Product actualProduct = productService.getProductById(productId);

        // Assert
        assertEquals(expectedProduct, actualProduct);
        verify(productDao, times(1)).getProductById(productId);
    }

    @Test
    public void updateQuantityTest() {
        // Arrange
        int productId = 1;
        int quantity = 50;
        Product product = new Product();
        product.setProduct_id(productId);
        product.setStock_quantity(100);
        //when(productDao.getProductById(productId)).thenReturn(product);

        // Act
        productService.updateQuantity(productId, quantity);

        // Assert
//        verify(productDao, times(1)).updateQuantity(productId, quantity);
//        assertEquals(50, product.getStock_quantity());
    }

    @Test
    public void updateProductTest() {
        // Arrange
        Product product = new Product();
        product.setProduct_id(1);
        product.setName("testProduct");
        product.setDescription("testDescription");
        product.setRetail_price(10.0f);
        product.setWholesale_price(5.0f);
        product.setStock_quantity(100);
        doNothing().when(productDao).updateProduct(product);

        // Act
        productService.updateProduct(product);

        // Assert
        verify(productDao, times(1)).updateProduct(product);
    }

}
