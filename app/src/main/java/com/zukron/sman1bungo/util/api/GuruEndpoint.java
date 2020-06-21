package com.zukron.sman1bungo.util.api;

public class GuruEndpoint {
//    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter nip "ALL" and nip
     */
    public static String getNip(String nip) {
        return String.format("%s/api/guru/?nip=%s", URL_SERVER, nip);
    }

    public static String getUsername(String username) {
        return String.format("%s/api/guru/?username=%s", URL_SERVER, username);
    }

    public static String postPartial() {
        return String.format("%s/api/guru/?type=PARTIAL", URL_SERVER);
    }

    public static String postFull() {
        return String.format("%s/api/guru/?type=FULL", URL_SERVER);
    }

    public static String putPartial(String nip) {
        return String.format("%s/api/guru/?type=PARTIAL&nip=%s", URL_SERVER, nip);
    }

    public static String putFull(String nip) {
        return String.format("%s/api/guru/?type=FULL&nip=%s", URL_SERVER, nip);
    }

    /***
     * value of parameter nip just nip
     */
    public static String delete(String nip) {
        return String.format("%s/api/guru/?nip=%s", URL_SERVER, nip);
    }
}
