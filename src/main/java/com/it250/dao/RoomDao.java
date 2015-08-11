/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.dao;

import com.it250.entities.Room;
import java.util.List;

/**
 *
 * @author Workbench
 */
public interface RoomDao {
    public List findAll();
    public void addRoom(Room r);
}
