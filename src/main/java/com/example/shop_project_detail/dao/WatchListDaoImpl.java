package com.example.shop_project_detail.dao;


import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.entity.WatchList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WatchListDaoImpl implements WatchListDao {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void addToWatchList(int user_id, int product_id) {
        Session session = sessionFactory.getCurrentSession();
        WatchList watchList = new WatchList();
        watchList.setUser_id(user_id);
        watchList.setProduct_id(product_id);

        session.save(watchList);
    }

    @Override
    public List<Product> getWatchlistByUserId(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        // Create a Hibernate Criteria object for the Product class
        Criteria criteria = session.createCriteria(Product.class, "p");
        // Create a DetachedCriteria object for the subquery
        DetachedCriteria subquery = DetachedCriteria.forClass(WatchList.class, "wl")
                .add(Restrictions.eq("wl.user_id", user_id))
                .setProjection(Projections.property("wl.product_id"));

        // Add a subquery restriction to the main Criteria
        criteria.add(Restrictions.ne("p.stock_quantity", 0));
        criteria.add(Subqueries.propertyIn("p.product_id", subquery));

        List<Product> products = criteria.list();
        return products;
    }

    @Override
    public void removeFromWatchlist(int user_id, int product_id) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(WatchList.class);
        criteria.add(Restrictions.eq("user_id", user_id));
        criteria.add(Restrictions.eq("product_id", product_id));
        Transaction tx = session.beginTransaction();

        List<WatchList> watchLists = criteria.list();
        System.out.println(watchLists);
        for (WatchList watchList : watchLists) {
            try {
                session.delete(watchList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tx.commit();
    }
}
