package com.zukron.sman1bungo.util.api;

public class GajiEndpoint {
//    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter id "ALL" and id_gaji
     */
    public static String get(String idGaji){
        return String.format("%s/api/gaji/?id=%s", URL_SERVER, idGaji);
    }

    public static String post(){
        return String.format("%s/api/gaji/", URL_SERVER);
    }

    public static String put(String idGaji){
        return String.format("%s/api/gaji/?id=%s", URL_SERVER, idGaji);
    }

    public static String delete(String idGaji){
        return String.format("%s/api/gaji/?id=%s", URL_SERVER, idGaji);
    }
}
