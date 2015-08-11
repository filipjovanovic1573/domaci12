/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.AbstractEntity;
import java.util.List;

/**
 *
 * @author Workbench
 * @param <T>
 */
public interface GenericDao <T extends AbstractEntity>{
    public List<T> findAll(Class c);
    public void add(T t);
    public void remove(int id, Class c);
    public void merge(T t);
}
