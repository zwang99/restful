package com.example.shop_project_detail.dao;

import com.example.shop_project_detail.domain.entity.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao{
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void addNewProduct(String name, String description, float retail_price, float wholesale_price, int stock_quantity) {
        Session session;
        session = sessionFactory.getCurrentSession();
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setRetail_price(retail_price);
        product.setWholesale_price(wholesale_price);
        product.setStock_quantity(stock_quantity);

        session.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        Session session;
        session = sessionFactory.getCurrentSession();
        TypedQuery<Product> query = session.createQuery("SELECT p FROM Product p WHERE p.stock_quantity != 0");
        List<Product> list = query.getResultList();
        return list;
    }

    @Override
    public Product getProductById(int product_id) {
        Session session;
        session = sessionFactory.getCurrentSession();
        TypedQuery<Product> query = session.createQuery("SELECT p FROM Product p WHERE p.product_id = :product_id");
        query.setParameter("product_id", product_id);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getQuantityById(int product_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Product.class);
        criteria.add(Restrictions.eq("product_id", product_id));
        criteria.setProjection(Projections.property("stock_quantity"));
        return (int) criteria.uniqueResult();
    }

    @Override
    public void updateQuantity(int product_id, int quantity) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Product product = session.get(Product.class, product_id);
        product.setStock_quantity(product.getStock_quantity() - quantity);
        session.update(product);
        session.getTransaction().commit();
    }

    @Override
    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Product oldProduct = session.get(Product.class, product.getProduct_id());
        oldProduct.setStock_quantity(product.getStock_quantity());
        oldProduct.setRetail_price(product.getRetail_price());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setName(product.getName());
        oldProduct.setWholesale_price(product.getWholesale_price());
        session.update(oldProduct);
        session.getTransaction().commit();
    }

}
