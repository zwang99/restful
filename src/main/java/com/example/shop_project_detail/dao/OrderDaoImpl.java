package com.example.shop_project_detail.dao;


import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.request.ProductQuantity;
import com.example.shop_project_detail.domain.response.*;
import com.example.shop_project_detail.service.LoginService;
import com.example.shop_project_detail.service.Order_ProductService;
import com.example.shop_project_detail.service.ProductService;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class OrderDaoImpl implements OrderDao{
    Order_ProductService order_productService;
    ProductService productService;
    LoginService loginService;

    @Autowired
    public OrderDaoImpl(Order_ProductService order_productService, ProductService productService, LoginService loginService) {
        this.order_productService = order_productService;
        this.productService = productService;
        this.loginService = loginService;
    }

    @Autowired
    SessionFactory sessionFactory;


    @Override
    public int findNextOrderId() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.max("order_id"));
        if(criteria.uniqueResult() == null){
            return 1;
        }else{
            return (int) criteria.uniqueResult() + 1;
        }
    }

    @Override
    public void placeOrder(int user_id, String order_status, Timestamp date_placed, float profit) {
        Session session = sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setUser_id(user_id);
        order.setOrder_status(order_status);
        order.setDate_placed(date_placed);
        order.setProfit(profit);
        session.save(order);
    }

    @Override
    public List<Order> findOrderByUserId(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Order  WHERE user_id = :user_id");
        query.setParameter("user_id", user_id);
        List<Order> orders = query.list();
        return orders;
    }

    @Override
    @Transactional
    public Order findOrderByOrderId(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Order  WHERE order_id = :order_id");
        query.setParameter("order_id", order_id);
        Order order = (Order) query.uniqueResult();
        return order;
    }

    @Override
    public List<MyOrderResponse> viewMyOrder(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        List<MyOrderResponse> myOrderResponses = new ArrayList<>();
        System.out.println("Executed Here");
        List<Order> orders = findOrderByUserId(user_id);
        System.out.println(orders);
        for(int i = 0; i < orders.size(); i++){
            Order order = orders.get(i);
            MyOrderResponse myOrderResponse = new MyOrderResponse();
            List<Order_Product> order_products = order_productService.findOrderProductByOrderId(order.getOrder_id());
            myOrderResponse.setOrder_id(order.getOrder_id());
            myOrderResponse.setOrder_status(order.getOrder_status());
            myOrderResponse.setDate_placed(order.getDate_placed());
            List<ProductQuantity> productQuantities = new ArrayList<>();
            for(int j = 0; j < order_products.size(); j++){
                Order_Product order_product = order_products.get(j);
                ProductQuantity productQuantity = new ProductQuantity();
                productQuantity.setProduct_id(order_product.getProduct_id());
                productQuantity.setQuantity(order_product.getPurchased_quantity());
                productQuantities.add(productQuantity);
            }
            myOrderResponse.setProductQuantityList(productQuantities);
            myOrderResponses.add(myOrderResponse);
        }
        return myOrderResponses;
    }

    @Override
    public void cancelOrder(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction outerTransaction = session.beginTransaction();
        Order order = findOrderByOrderId(order_id);
        order.setOrder_status("Canceled");
        order.setProfit(0);
        session.update(order);
        List<Order_Product> order_products = order_productService.findOrderProductByOrderId(order_id);
//        for(int i = 0; i < order_products.size(); i++){
//            Order_Product order_product = order_products.get(i);
//            //productService.updateQuantity(order_product.getProduct_id(), -order_product.getPurchased_quantity());
//            order_productService.removeOrderProductByOrderProductId(order_product.getOrder_product_id());
//        }
        outerTransaction.commit();
        for(int i = 0; i < order_products.size(); i++){
            outerTransaction = session.beginTransaction();
            Order_Product order_product = order_products.get(i);
            Product product = session.get(Product.class, order_product.getProduct_id());
            product.setStock_quantity(product.getStock_quantity() + order_product.getPurchased_quantity());
            session.update(product);
            outerTransaction.commit();
        }
    }

    @Override
    public void completeOrder(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Order WHERE order_id = :order_id");
        query.setParameter("order_id", order_id);
        Order order = (Order) query.uniqueResult();
        order.setOrder_status("Completed");
        session.update(order);
        session.getTransaction().commit();
    }

    @Override
    public List<MostPurchasedResponse> getMostFrequentlyPurchased(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        String sql = "SELECT `OrderTable`.user_id, Product.product_id, Product.name, SUM(Order_Product.purchased_quantity) as total_quantity" +
                " FROM `OrderTable`" +
                " JOIN Order_Product ON `OrderTable`.order_id = Order_Product.order_id" +
                " JOIN Product ON Order_Product.product_id = Product.product_id" +
                " WHERE `OrderTable`.user_id = :user_id AND `OrderTable`.order_status != 'Canceled' " +
                " GROUP BY `OrderTable`.user_id, Product.product_id, Product.name \n" +
                " ORDER BY total_quantity DESC, Product.product_id ASC" +
                " LIMIT 3;";

        SQLQuery query = session.createSQLQuery(sql)
                .addScalar("product_id", IntegerType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("total_quantity", IntegerType.INSTANCE)
                .setParameter("user_id", user_id);
        List<Object[]> results = query.list();

        List<MostPurchasedResponse> mostPurchasedResponses = new ArrayList<>();

        for (Object[] result : results) {
            Integer productId = (Integer) result[0];
            String name = (String) result[1];
            Integer totalQuantity = (Integer) result[2];
            MostPurchasedResponse mostPurchasedResponse = new MostPurchasedResponse();
            mostPurchasedResponse.setProduct_id(productId);
            mostPurchasedResponse.setProduct_name(name);
            mostPurchasedResponse.setQuantity(totalQuantity);
            mostPurchasedResponses.add(mostPurchasedResponse);
        }
        return mostPurchasedResponses;
    }

    @Override
    @Transactional
    public List<MostPurchasedResponse> getMostRecentlyPurchased(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        String sql = "SELECT OrderTable.user_id, Product.product_id, Product.name, OrderTable.date_placed " +
                "FROM OrderTable " +
                "JOIN Order_Product ON OrderTable.order_id = Order_Product.order_id " +
                "JOIN Product ON Order_Product.product_id = Product.product_id " +
                "WHERE OrderTable.user_id = :user_id AND OrderTable.order_status != 'Canceled' " +
                "GROUP BY OrderTable.user_id, Product.product_id, Product.name, OrderTable.date_placed " +
                "ORDER BY OrderTable.date_placed DESC, Product.product_id ASC " +
                "LIMIT 3";
        SQLQuery query = session.createSQLQuery(sql)
                .addScalar("product_id", IntegerType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("date_placed", TimestampType.INSTANCE)
                .setParameter("user_id", user_id);


        List<Object[]> results = query.list();

        List<MostPurchasedResponse> mostPurchasedResponses = new ArrayList<>();

        for (Object[] result : results) {
            Integer productId = (Integer) result[0];
            String name = (String) result[1];
            Timestamp datePlaced = (Timestamp) result[2];
            MostPurchasedResponse mostPurchasedResponse = new MostPurchasedResponse();
            mostPurchasedResponse.setProduct_id(productId);
            mostPurchasedResponse.setProduct_name(name);
            mostPurchasedResponse.setDate_placed(datePlaced);
            mostPurchasedResponses.add(mostPurchasedResponse);
        }
        return mostPurchasedResponses;
    }

    @Override
    public List<Order> getAllOrders() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Order");
        List<Order> orders = query.list();
        return orders;
     }

    @Override
    public OrderDetailResponse getOrderByOrderId(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Order order = findOrderByOrderId(order_id);
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrder_id(order_id);
        orderDetailResponse.setOrder_status(order.getOrder_status());
        orderDetailResponse.setDate_placed(order.getDate_placed());
        orderDetailResponse.setUser_id(order.getUser_id());
        System.out.println(orderDetailResponse);
        List<ProductQuantity> productQuantities = new ArrayList<>();
        List<Order_Product> order_products = order_productService.findOrderProductByOrderId(order_id);
        System.out.println(order_products);
        for(int i = 0; i < order_products.size(); i++){
            Order_Product order_product = order_products.get(i);
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct_id(order_product.getProduct_id());
            productQuantity.setQuantity(order_product.getPurchased_quantity());
            productQuantities.add(productQuantity);
        }
        orderDetailResponse.setProductQuantityList(productQuantities);
        return orderDetailResponse;
    }

    @Override
    public User findUserByOrderId(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT u FROM User u JOIN Order o ON u.id = o.user_id WHERE o.id = :order_id");
        query.setParameter("order_id", order_id);
        if(query.getSingleResult() == null){
            return null;
        }else{
            return (User) query.getSingleResult();
        }
    }

    @Override
    public UserOrderResponse getUserOrder(int user_id, int order_id) {
        UserOrderResponse userOrderResponse = new UserOrderResponse();
        ServiceStatusResponse serviceStatusResponse = new ServiceStatusResponse();
        serviceStatusResponse.setSuccess(true);
        userOrderResponse.setServiceStatus(serviceStatusResponse);
        System.out.println(userOrderResponse.getServiceStatus());
        Order order = findOrderByOrderId(order_id);
        System.out.println(order);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order_id);
        orderResponse.setTime(order.getDate_placed());
        User user = loginService.getUserByUserId(user_id);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userOrderResponse.setUserResponse(userResponse);

        List<ProductQuantity> productQuantities = new ArrayList<>();
        List<Order_Product> order_products = order_productService.findOrderProductByOrderId(order_id);
        float price = 0;
        for(int i = 0; i < order_products.size(); i++){
            Order_Product order_product = order_products.get(i);
            price += order_product.getExecution_retail_price();
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct_id(order_product.getProduct_id());
            productQuantity.setQuantity(order_product.getPurchased_quantity());
            productQuantities.add(productQuantity);
        }
        orderResponse.setOrderItemResponseList(productQuantities);
        orderResponse.setTotalPrice(price);
        userOrderResponse.setOrderResponse(orderResponse);
        return userOrderResponse;
    }

}
