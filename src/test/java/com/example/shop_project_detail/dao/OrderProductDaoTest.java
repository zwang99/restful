package com.example.shop_project_detail.dao;

import com.example.shop_project_detail.domain.entity.Order_Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static javafx.beans.binding.Bindings.when;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNull;


@ActiveProfiles(value = "test")     // using application-test.properties
@SpringBootTest
public class OrderProductDaoTest {
    @Autowired
    Order_ProductDao order_productDao;

    @Mock
    SessionFactory session;


//    @Test
//    public void testFindOrderProductByOrderId() {
//        // Arrange
//        int orderId = 1;
//
//        // Act
//        List<Order_Product> orderProducts = order_productDao.findOrderProductByOrderId(orderId);
//
//        // Assert
//        assertNotNull(orderProducts);
//        assertTrue(orderProducts.size() > 0);
//        for (Order_Product orderProduct : orderProducts) {
//            assertEquals(orderId, orderProduct.getOrder_id());
//        }
//    }

}
