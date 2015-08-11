/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.User;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Workbench
 */
public class UserDaoImpl extends GenericDaoImpl implements UserDao{
    
    @Override
    public User findById(int id) {
        return (User) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public User checkUser(String username, String password) {
        return (User) session.createCriteria(User.class).add(Restrictions.eq("username", username)).add(Restrictions.eq("password", password)).uniqueResult();
    }
}
