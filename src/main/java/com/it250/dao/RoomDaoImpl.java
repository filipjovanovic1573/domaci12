/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.Room;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Workbench
 */
public class RoomDaoImpl extends GenericDaoImpl implements RoomDao{

    @Override
    public Room findById(int id) {
        return (Room) session.createCriteria(Room.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public Room update(Room r) {
        return (Room)session.merge(r);
    }
    
}
