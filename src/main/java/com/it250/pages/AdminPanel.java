/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.pages;

import com.it250.dao.GuestDao;
import com.it250.dao.RoomDao;
import com.it250.dao.UserDao;
import com.it250.entities.Room;
import com.it250.entities.User;
import com.it250.services.ProtectedPage;
import java.util.ArrayList;
import javax.annotation.security.RolesAllowed;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 *
 * @author Workbench
 */
@ProtectedPage
@RolesAllowed(value={"Admin"})
public class AdminPanel {
    
    @InjectComponent
    private BeanEditForm addUser, addRoom;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private RoomDao roomDao;
    
    @Inject
    private GuestDao guestDao;
    
    @Property
    private User newUser;
    
    @Property
    private User userValue;
    
    @Property
    private Room newRoom;
    
    @Property @Persist
    private ArrayList<User> users;
    
    @Property @Persist
    private ArrayList<Room> rooms;
    
    private User tmp;
    
    void onActivate(){
        tmp = new User();
        if(users == null){
            users = new ArrayList<User>();
        }
        
        users = (ArrayList<User> )userDao.findAll();
        
        if(rooms == null){
            rooms = new ArrayList<Room>();
        }
        
        rooms = (ArrayList<Room>)roomDao.findAll();
    }
    
    
    void onValidateFromAddRoom(){
        if(newRoom.getFloor() == null){
            addRoom.recordError("Unesi sprat");
        }
    }
    
    void onValidateFromAddUser(){
        tmp = userDao.checkUser(newUser.getUsername(), newUser.getPassword());
        
        if(tmp != null){
            addUser.recordError("Korisnik postoji");
        }
    }
    
    @CommitAfter
    Object onSuccessFromAddRoom(){
        newRoom.setUserId(userValue);
        roomDao.addRoom(newRoom);
        return this;
    }
    
    @CommitAfter
    Object onSuccessFromAddUser(){
        userDao.add(newUser);
        return this;
    }
    
     public ValueEncoder getEncoder(){
        return new ValueEncoder<User>() {

            @Override
            public String toClient(User v) {
                return String.valueOf(v.getId());
            }

            @Override
            public User toValue(String string) {
                return userDao.findById(Integer.parseInt(string));
            }
        };
    }
   
}
