package com.it250.pages;

import com.it250.dao.UserDao;
import com.it250.entities.User;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Login {

    @Inject
    private AlertManager alertManager;

    @InjectComponent
    private BeanEditForm login;

    @Property
    private User logUser;

    @SessionState
    private User user;
    
    @Inject
    private UserDao userDao;

    void onValidateFromLogin() {
        logUser = userDao.checkUser(logUser.getUsername(), logUser.getPassword());
        
        if(logUser.getId() == null){
            login.recordError("Korisnik nije pronadjen");
        }
        
    }

    Object onSuccessFromLogin() {
        user = logUser;
        return Index.class;
    }

}
