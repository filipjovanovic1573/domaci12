/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.User;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Workbench
 */
public class UserDaoImpl implements UserDao{
    
    @Inject
    private Session session;

    @Override
    public User findById(int id) {
        return (User) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public List<User> findAll() {
        return session.createCriteria(User.class).list();
    }

    @Override
    public User checkUser(String username, String password) {
        return (User) session.createCriteria(User.class).add(Restrictions.eq("username", username)).add(Restrictions.eq("password", password)).uniqueResult();
    }

    @Override
    public void add(User u) {
        session.persist(u);
    }
    
}
