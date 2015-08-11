/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.User;
import java.util.List;

/**
 *
 * @author Workbench
 */
public interface UserDao {
    
    public List<User> findAll();
    public User findById(int id);
    public User checkUser(String username, String password);
    public void add(User u);
}
