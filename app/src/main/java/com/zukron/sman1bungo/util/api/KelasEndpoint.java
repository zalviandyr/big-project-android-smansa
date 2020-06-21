package com.zukron.sman1bungo.util.api;

public class KelasEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter id "ALL" and id_kelas
     */
    public static String get(String idKelas){
        return String.format("%s/api/kelas/?id=%s", URL_SERVER, idKelas);
    }

    public static String post(){
        return String.format("%s/api/kelas/", URL_SERVER);
    }

    public static String put(String idKelas){
        return String.format("%s/api/kelas/?id=%s", URL_SERVER, idKelas);
    }

    public static String delete(String idKelas){
        return String.format("%s/api/kelas/?id=%s", URL_SERVER, idKelas);
    }
}
