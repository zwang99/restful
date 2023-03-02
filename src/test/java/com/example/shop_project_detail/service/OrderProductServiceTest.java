package com.example.shop_project_detail.service;

import com.example.shop_project_detail.dao.Order_ProductDao;
import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.runners.model.MultipleFailureException.assertEmpty;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderProductServiceTest {

    @InjectMocks
    Order_ProductService order_productService;

    @Mock
    Order_ProductDao order_productDao;

    @Test
    public void testAddNewOrderProduct() {
        // Arrange
        int order_id = 1;
        int product_id = 2;
        int purchased_quantity = 3;
        float execution_retail_price = 10.0f;
        float execution_wholesale_price = 5.0f;

        // Act
        order_productService.addNewOrderProduct(order_id, product_id, purchased_quantity, execution_retail_price, execution_wholesale_price);

        // Assert
        // Verify that the addNewOrderProduct function was called with the correct parameters
        // This can be done using a mocking framework like Mockito to mock the OrderProductDao object and verify that it was called with the correct parameters
        verify(order_productDao).addNewOrderProduct(order_id, product_id, purchased_quantity, execution_retail_price, execution_wholesale_price);
    }

    @Test
    public void findOrderProductByOrderIdTest() {
        int order_id = 1;
        List<Order_Product> orderProducts = order_productService.findOrderProductByOrderId(order_id);
        assertNotNull(orderProducts);
        for (Order_Product orderProduct : orderProducts) {
            assertEquals(order_id, orderProduct.getOrder_id());
        }
    }

    @Test
    public void testRemoveOrderProductByOrderProductId() {
        // Arrange
        int order_product_id = 1;
        Order_Product orderProduct = new Order_Product();
        orderProduct.setOrder_product_id(order_product_id);

        // Act
        order_productService.removeOrderProductByOrderProductId(order_product_id);
        List<Order_Product> result = order_productService.findOrderProductByOrderId(order_product_id);
    }

    @Test
    public void testGetMostProfitableProduct() {

        // Act
        MostProfitProductResponse result = order_productService.getMostProfitableProduct();
        // Assert
//        assertEquals(result.getProduct_id(), result.getProduct_id());
//        assertEquals(result.getProdcut_name(), result.getProdcut_name());
//        assertEquals(result.getProfit(), result.getProfit(), 0.001);
    }
}
