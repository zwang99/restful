package com.example.shop_project_detail.dao;

import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Order_ProductDaoImpl implements Order_ProductDao{
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void addNewOrderProduct(int order_id, int product_id, int purchased_quantity, float execution_retail_price, float execution_wholesale_price) {
        Session session = sessionFactory.getCurrentSession();
        Order_Product order_product = new Order_Product();
        order_product.setOrder_id(order_id);
        order_product.setProduct_id(product_id);
        order_product.setPurchased_quantity(purchased_quantity);
        order_product.setExecution_retail_price(execution_retail_price);
        order_product.setExecution_wholesale_price(execution_wholesale_price);
        session.save(order_product);
    }

    @Override
    @Transactional
    public List<Order_Product> findOrderProductByOrderId(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Order_Product where order_id = :order_id");
        query.setParameter("order_id", order_id);
        List<Order_Product> order_products = query.list();
        return order_products;
    }

    @Override
    public void removeOrderProductByOrderProductId(int order_product_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("DELETE FROM Order_Product WHERE order_product_id = :order_product_id");
        query.setParameter("order_product_id", order_product_id);
        query.executeUpdate();
    }

    @Override
    public MostProfitProductResponse getMostProfitableProduct() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String sql =
                "SELECT " +
                        "    Product.product_id, \n" +
                        "    Product.name,\n" +
                        "    SUM(Order_Product.execution_retail_price - Order_Product.execution_wholesale_price) AS profit\n" +
                        "FROM \n" +
                        "    Order_Product\n" +
                        "    JOIN OrderTable ON Order_Product.order_id = OrderTable.order_id \n" +
                        "    JOIN Product ON Order_Product.product_id = Product.product_id\n" +
                        "WHERE \n" +
                        "    OrderTable.order_status = 'Completed' \n" +
                        "GROUP BY \n" +
                        "    Product.product_id\n" +
                        "ORDER BY \n" +
                        "    profit DESC\n" +
                        "LIMIT 1";

        SQLQuery query = session.createSQLQuery(sql);

        List<Object[]> results = query.list();

        MostProfitProductResponse mostProfitProductResponse = new MostProfitProductResponse();

        for (Object[] result : results) {
            int productId = (int) result[0];
            String name = (String) result[1];
            Double profit = (Double) result[2];

            mostProfitProductResponse.setProduct_id(productId);
            mostProfitProductResponse.setProdcut_name(name);
            mostProfitProductResponse.setProfit(profit.floatValue());
        }

        session.getTransaction().commit();
        session.close();
        return mostProfitProductResponse;
    }

    @Override
    public List<MostSoldProductsResponse> getTop3MostSoldProducts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String sql = "SELECT \n" +
                "    Product.product_id, \n" +
                "    Product.name,\n" +
                "    SUM(Order_Product.purchased_quantity) AS sold_quantity\n" +
                "FROM \n" +
                "    Order_Product\n" +
                "    JOIN OrderTable ON Order_Product.order_id = OrderTable.order_id \n" +
                "    JOIN Product ON Order_Product.product_id = Product.product_id\n" +
                "WHERE \n" +
                "    OrderTable.order_status = 'Completed' \n" +
                "GROUP BY \n" +
                "    Product.product_id\n" +
                "ORDER BY \n" +
                "    sold_quantity DESC\n" +
                "LIMIT 3\n";
        List<MostSoldProductsResponse> mostSoldProductsResponses = new ArrayList<>();

        SQLQuery query = session.createSQLQuery(sql);
        List<Object[]> results = query.list();

        for (Object[] result : results) {
            int productId = (int) result[0];
            String name = (String) result[1];
            BigDecimal soldQuantity = (BigDecimal) result[2];

            MostSoldProductsResponse mostSoldProductsResponse = new MostSoldProductsResponse();
            mostSoldProductsResponse.setProduct_id(productId);
            mostSoldProductsResponse.setProduct_name(name);
            mostSoldProductsResponse.setSold_quantity(soldQuantity.intValue());
            mostSoldProductsResponses.add(mostSoldProductsResponse);
        }
        return mostSoldProductsResponses;
    }

    @Override
    public int getAllItemsSold() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String sql = "SELECT SUM(op.purchased_quantity)\n" +
                "FROM Order_Product op\n" +
                "JOIN OrderTable o ON op.order_id = o.order_id\n" +
                "WHERE o.order_status = 'Completed';";

        SQLQuery query = session.createSQLQuery(sql);
        BigDecimal ans = (BigDecimal) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return ans.intValue();
    }

    @Override
    public List<TopThreeBuyerResponse> getTop3Buyer() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String sql = "SELECT o.user_id, SUM(op.purchased_quantity * op.execution_retail_price) as money_spent\n" +
                "FROM OrderTable o\n" +
                "JOIN Order_Product op ON o.order_id = op.order_id\n" +
                "WHERE o.order_status = 'Completed'\n" +
                "GROUP BY o.user_id\n" +
                "ORDER BY money_spent DESC\n" +
                "LIMIT 3;\n";

        SQLQuery query = session.createSQLQuery(sql);
        List<TopThreeBuyerResponse> topThreeBuyerResponses = new ArrayList<>();
        List<Object[]> results = query.list();
        for (Object[] result : results) {
            int user_id = (int)result[0];
            Double money_spend = (Double) result[1];
            TopThreeBuyerResponse topThreeBuyerResponse = new TopThreeBuyerResponse();
            topThreeBuyerResponse.setUser_id(user_id);
            topThreeBuyerResponse.setMoney_spend(money_spend.intValue());
            topThreeBuyerResponses.add(topThreeBuyerResponse);
        }
        return topThreeBuyerResponses;
    }
}
