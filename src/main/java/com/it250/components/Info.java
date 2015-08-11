/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it250.components;

import com.it250.entities.User;
import java.util.Date;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

/**
 *
 * @author Workbench
 */
public class Info {

    @SessionState
    @Property
    private User user;

    public String getInfo() {
        if (user.getId() != null) {
            return "Korisnik: " + user.getUsername() + " Vreme: " + new Date();
        }
        else {
            return " Vreme: " + new Date();
        }
    }
}
