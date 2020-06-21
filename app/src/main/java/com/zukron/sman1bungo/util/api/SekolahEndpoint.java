package com.zukron.sman1bungo.util.api;

public class SekolahEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    public static String get(){
        return String.format("%s/api/sekolah/", URL_SERVER);
    }

    public static String post(){
        return String.format("%s/api/sekolah/", URL_SERVER);
    }

    public static String put(){
        return String.format("%s/api/sekolah/", URL_SERVER);
    }
}
