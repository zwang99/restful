package com.example.shop_project_detail.dao;
import com.example.shop_project_detail.domain.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class LoginDaoImpl implements LoginDao{
    @Autowired
    SessionFactory sessionFactory;

//    @Override
//    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
//        Session session;
//        session = sessionFactory.getCurrentSession();
//        TypedQuery<User> query = session.createQuery("SELECT u FROM  User u WHERE u.username = :username" +
//                " AND u.password = :password", User.class);
//        query.setParameter("username", username);
//        query.setParameter("password", password);
//        try {
//            return Optional.of(query.getSingleResult());
//        } catch (Exception e){
//            return Optional.empty();
//        }
//    }

    @Override
    public Optional<User> loadUserByUsername(String username) {
        Session session;
        session = sessionFactory.getCurrentSession();
        TypedQuery<User> query = session.createQuery("SELECT u FROM  User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public User getUserByUserId(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM User u WHERE u.user_id = :user_id");
        query.setParameter("user_id", user_id);
        if(query.getSingleResult() == null) return null;
        else return (User)query.getSingleResult();
    }

    @Override
    public List<String> getPermissionsByUserId(int user_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("SELECT p.permission FROM Permissions p, User_Permission up " +
                "WHERE up.user_id = :user_id AND up.permission_id = p.permission_id");
        query.setParameter("user_id", user_id);
        List<String> permissions = query.list();
        //System.out.println(permissions);
        return permissions;
    }
}
