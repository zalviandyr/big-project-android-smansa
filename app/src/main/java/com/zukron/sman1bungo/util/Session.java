package com.zukron.sman1bungo.util;

import com.zukron.sman1bungo.model.User;

public class Session {
    private static User userSession;

    public static void setSession(User userSession) {
        Session.userSession = userSession;
    }

    public static User getSession() {
        return Session.userSession;
    }
}
