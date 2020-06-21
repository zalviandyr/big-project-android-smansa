package com.zukron.sman1bungo.util.api;

public class PelajaranEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter id "ALL" and id_pelajaran
     */
    public static String get(String idPelajaran){
        return String.format("%s/api/pelajaran/?id=%s", URL_SERVER, idPelajaran);
    }

    public static String post(){
        return String.format("%s/api/pelajaran/", URL_SERVER);
    }

    public static String put(String idPelajaran){
        return String.format("%s/api/pelajaran/?id=%s", URL_SERVER, idPelajaran);
    }

    public static String delete(String idPelajaran){
        return String.format("%s/api/pelajaran/?id=%s", URL_SERVER, idPelajaran);
    }
}
