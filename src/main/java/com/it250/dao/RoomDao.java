/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.Room;

/**
 *
 * @author Workbench
 */
public interface RoomDao extends GenericDao{
    public Room findById(int id);
    public Room update(Room r);
}
