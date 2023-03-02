package com.example.shop_project_detail.dao;


import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.entity.User_Permission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class RegisterDaoImpl implements RegisterDao{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public User findUserByUsername(String username) {
        Session session;
        session = sessionFactory.getCurrentSession();
        TypedQuery<User> query = session.createQuery("SELECT u FROM  User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (Exception e){
            return null;
        }
        //return query.getSingleResult() == null? null : query.getSingleResult();
    }

    @Override
    public User findUserByEmail(String email) {
        Session session;
        session = sessionFactory.getCurrentSession();
        TypedQuery<User> query = session.createQuery("SELECT u FROM  User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (Exception e){
            return null;
        }
        //return query.getSingleResult() == null? null : query.getSingleResult();
    }

    @Override
    public void saveUser(User user) {
        Session session;
        session = sessionFactory.getCurrentSession();
        session.save(user);
    }

    @Override
    public void addPermissions(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User_Permission user_permission = new User_Permission();
        user_permission.setUser_id(user_id);
        user_permission.setPermission_id(1);
        session.save(user_permission);
        session.getTransaction().commit();
        session.close();
    }
}
