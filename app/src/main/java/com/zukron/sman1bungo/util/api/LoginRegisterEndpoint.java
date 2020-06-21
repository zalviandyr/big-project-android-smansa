package com.zukron.sman1bungo.util.api;

public class LoginRegisterEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    public static String get(String username, String password){
        return String.format("%s/api/login_register/?username=%s&password=%s", URL_SERVER, username, password);
    }

    public static String post(String status){
        return String.format("%s/api/login_register/?status=%s", URL_SERVER, status);
    }
}
