/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.User;

/**
 *
 * @author Workbench
 */
public interface UserDao extends GenericDao{
    
    public User findById(int id);
    public User checkUser(String username, String password);
}
