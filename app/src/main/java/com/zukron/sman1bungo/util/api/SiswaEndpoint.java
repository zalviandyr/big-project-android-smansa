package com.zukron.sman1bungo.util.api;

public class SiswaEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter nisn "ALL" and nisn
     */
    public static String getNisn(String nisn){
        return String.format("%s/api/siswa/?nisn=%s", URL_SERVER, nisn);
    }

    public static String getUsername(String username) {
        return String.format("%s/api/siswa/?username=%s", URL_SERVER, username);
    }

    public static String postPartial() {
        return String.format("%s/api/siswa/?type=PARTIAL", URL_SERVER);
    }

    public static String postFull() {
        return String.format("%s/api/siswa/?type=FULL", URL_SERVER);
    }

    public static String putPartial(String nisn) {
        return String.format("%s/api/siswa/?type=PARTIAL&nisn=%s", URL_SERVER, nisn);
    }

    public static String putFull(String nisn) {
        return String.format("%s/api/siswa/?type=FULL&nisn=%s", URL_SERVER, nisn);
    }

    /***
     * value of parameter nisn just nisn
     */
    public static String delete(String nisn) {
        return String.format("%s/api/siswa/?nisn=%s", URL_SERVER, nisn);
    }
}
