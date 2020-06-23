package com.zukron.sman1bungo.util.api;

public class AdminEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter id "ALL" and id_admin
     */
    public static String getId(String idAdmin){
        return String.format("%s/api/admin/?id=%s", URL_SERVER, idAdmin);
    }

    public static String getUsername(String username) {
        return String.format("%s/api/admin/?username=%s", URL_SERVER, username);
    }

    public static String postPartial() {
        return String.format("%s/api/admin/?type=PARTIAL", URL_SERVER);
    }

    public static String postFull() {
        return String.format("%s/api/admin/?type=FULL", URL_SERVER);
    }

    public static String putPartial(String idAdmin) {
        return String.format("%s/api/admin/?type=PARTIAL&id=%s", URL_SERVER, idAdmin);
    }

    public static String putFull(String idAdmin) {
        return String.format("%s/api/admin/?type=FULL&id=%s", URL_SERVER, idAdmin);
    }

    /***
     * value of parameter id just id_admin
     */
    public static String delete(String idAdmin) {
        return String.format("%s/api/admin/?id=%s", URL_SERVER, idAdmin);
    }
}
