/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.Room;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

/**
 *
 * @author Workbench
 */
public class RoomDaoImpl implements RoomDao{
    
    @Inject
    private Session session;

    @Override
    public List findAll() {
        return session.createCriteria(Room.class).list();
    }

    @Override
    public void addRoom(Room r) {
        session.persist(r);
    }
    
}
