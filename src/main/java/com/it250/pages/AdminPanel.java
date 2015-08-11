/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.pages;

import com.it250.dao.RoomDao;
import com.it250.dao.UserDao;
import com.it250.entities.Room;
import com.it250.entities.User;
import com.it250.services.ProtectedPage;
import java.util.ArrayList;
import javax.annotation.security.RolesAllowed;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageLoaded;
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
    
    @Property @Persist
    private User newUser;
    
    @Property
    private User userValue;
    
    @Property @Persist
    private Room newRoom; 
            
    @Property        
    private Room gridRoom;
    
    @Property
    private ArrayList<User> users;
    
    @Property
    private ArrayList<Room> rooms;
    
    private User tmp;
    
    void onActivate(){
        
    }
    
    @PageLoaded
    void createLocalCache(){
        tmp = new User();
        if(users == null){
            users = new ArrayList<User>();
        }
        
        users = (ArrayList<User> )userDao.findAll(User.class);
        
        if(rooms == null){
            rooms = new ArrayList<Room>();
        }
        
        rooms = (ArrayList<Room>)roomDao.findAll(Room.class);
    }
    
    
    void onValidateFromAddRoom(){
        if(newRoom.getFloor() == null){
            addRoom.recordError("Unesi sprat");
        }
        
        if(userValue == null){
            addRoom.recordError("Iyaberi korisnika");
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
        
        if(rooms.contains(newRoom))
            rooms.remove(newRoom);
        
        rooms.add(roomDao.update(newRoom));
        newRoom = new Room();
        return this;
    }
    
    @CommitAfter
    Object onSuccessFromAddUser(){
        userDao.add(newUser);
        users.add(newUser);
        newUser = new User();
        return this;
    }
    
    @CommitAfter
    public Object onActionFromDeleteRoom(int id){
        rooms.remove(roomDao.findById(id));
        roomDao.remove(id, Room.class);
        return this;
    }
    
    public Object onActionFromEditRoom(Room r){
        userValue = r.getUserId();
        newRoom = r;
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
