package com.example.shop_project_detail.service;

import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.request.ProductQuantity;
import com.example.shop_project_detail.domain.response.OrderResponse;
import com.example.shop_project_detail.domain.response.ServiceStatusResponse;
import com.example.shop_project_detail.domain.response.UserOrderResponse;
import com.example.shop_project_detail.domain.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    Order_ProductService order_productService;
    ProductService productService;
    UserService userService;
    OrderService orderService;

    @Autowired
    public AsyncService(Order_ProductService order_productService, ProductService productService, UserService userService, OrderService orderService) {
        this.order_productService = order_productService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    public CompletableFuture<UserOrderResponse> getResponseAsync(int user_id, int order_id) {
        CompletableFuture<User> userAsync = userService.getUserAsync(user_id);
        CompletableFuture<Order> orderAsync = orderService.findOrderAsync(order_id);
        CompletableFuture<List<Order_Product>> orderProductAsync = order_productService.findOrderProductAsync(order_id);

        return CompletableFuture.allOf(userAsync, orderAsync, orderProductAsync)
                .thenApply((Void) -> {
                    User user = userAsync.join();
                    Order order = orderAsync.join();
                    List<Order_Product> orderProducts = orderProductAsync.join();

                    UserOrderResponse response = new UserOrderResponse();
                    ServiceStatusResponse serviceStatusResponse = new ServiceStatusResponse();
                    serviceStatusResponse.setSuccess(true);
                    response.setServiceStatus(serviceStatusResponse);

                    OrderResponse orderResponse = new OrderResponse();
                    orderResponse.setOrderId(order_id);
                    orderResponse.setTime(order.getDate_placed());
                    UserResponse userResponse = new UserResponse();
                    userResponse.setUsername(user.getUsername());
                    userResponse.setEmail(user.getEmail());
                    response.setUserResponse(userResponse);

                    List<ProductQuantity> productQuantities = new ArrayList<>();
                    float price = 0;
                    for (Order_Product orderProduct : orderProducts) {
                        ProductQuantity productQuantity = new ProductQuantity();
                        productQuantity.setProduct_id(orderProduct.getProduct_id());
                        productQuantity.setQuantity(orderProduct.getPurchased_quantity());
                        productQuantities.add(productQuantity);
                        price += orderProduct.getExecution_retail_price();
                    }
                    orderResponse.setOrderItemResponseList(productQuantities);
                    orderResponse.setTotalPrice(price);
                    response.setOrderResponse(orderResponse);

                    return response;
                });
    }

}
