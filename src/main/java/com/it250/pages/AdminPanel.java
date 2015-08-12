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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.got5.tapestry5.jquery.components.InPlaceEditor;
import org.json.JSONObject;

/**
 *
 * @author Workbench
 */
@ProtectedPage
@RolesAllowed(value = {"Admin"})
public class AdminPanel {

    @InjectComponent
    private BeanEditForm addUser, addRoom;

    @Inject
    private UserDao userDao;

    @Inject
    private RoomDao roomDao;

    @Property
    @Persist
    private User newUser;

    @Property
    private User userValue;

    @Property
    private Room newRoom, gridRoom;

    @Property
    private ArrayList<User> users;

    @Property
    @Persist
    private ArrayList<Room> rooms;

    @InjectComponent
    private Zone formZone;

    @InjectComponent
    private Zone gridZone;

    @Inject
    private Request request;

    @Inject
    private ComponentResources resources;

    @Inject
    private AjaxResponseRenderer renderer;

    @Property
    private User ajaxUser;

    void onActivate() {
        users = new ArrayList<User>();

        users = (ArrayList<User>) userDao.findAll(User.class);

        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }

        rooms = (ArrayList<Room>) roomDao.findAll(Room.class);
    }

    void onValidateFromAddRoom() {
        if (newRoom.getFloor() == null) {
            addRoom.recordError("Unesi sprat");
        }

        if (userValue == null) {
            addRoom.recordError("Iyaberi korisnika");
        }

    }

    void onValidateFromAddUser() {
        if (userDao.checkUser(newUser.getUsername(), newUser.getPassword()) != null) {
            addUser.recordError("Korisnik postoji");
        }
    }

    @CommitAfter
    Object onSuccessFromAddRoom() {
        newRoom.setUserId(userValue);
        roomDao.add(newRoom);
        return this;
    }

    @CommitAfter
    Object onSuccessFromAddUser() {
        userDao.add(newUser);
        users = (ArrayList<User>) userDao.findAll(User.class);
        newUser = new User();
        if (request.isXHR()) {
            renderer.addRender(gridZone).addRender(formZone);
        }

        return request.isXHR() ? gridZone.getBody() : null;
    }

    @CommitAfter
    @OnEvent(component = "username", value = InPlaceEditor.SAVE_EVENT)
    void setUsername(int id, String value) {
        User u = userDao.findById(id);
        u.setUsername(value);
        userDao.merge(u);
    }

    /*public JSONObject getOptions() {
        JSONObject params = new JSONObject();
        Object[] context = new Object[]{currentIndex};
        String listenerURI = _componentResources.createEventLink("refresh", context).toAbsoluteURI(false);
        String zoneID = updateZone.getClientId();
        params.put("tooltip", "Cliquer pour �diter");
        params.put("callback", new JSONLiteral("function(value, settings)"
                + "{"
                + " var zoneElement = $('#" + zoneID + "');"
                + " zoneElement.tapestryZone('update',{url : '" + listenerURI + "',params : {id:" + currentIndex + "} });"
                + "}"));
        return params;
    }*/

    @CommitAfter
    public Object onActionFromDeleteRoom(int id) {
        roomDao.remove(id, Room.class);
        return this;
    }

    public ValueEncoder getEncoder() {
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
