package com.zukron.sman1bungo.util.api;

public class PegawaiEndpoint {
    //    private static final String URL_SERVER = "http://10.0.2.2/server-smansa";
    private static final String URL_SERVER = "http://zera-smansa.herokuapp.com";

    /***
     * value of parameter id "ALL" and id_pegawai
     */
    public static String getId(String idPegawai){
        return String.format("%s/api/pegawai/?id=%s", URL_SERVER, idPegawai);
    }

    public static String getUsername(String username) {
        return String.format("%s/api/pegawai/?username=%s", URL_SERVER, username);
    }

    public static String postPartial() {
        return String.format("%s/api/pegawai/?type=PARTIAL", URL_SERVER);
    }

    public static String postFull() {
        return String.format("%s/api/pegawai/?type=FULL", URL_SERVER);
    }

    public static String putPartial(String idPegawai) {
        return String.format("%s/api/pegawai/?type=PARTIAL&id=%s", URL_SERVER, idPegawai);
    }

    public static String putFull(String idPegawai) {
        return String.format("%s/api/pegawai/?type=FULL&id=%s", URL_SERVER, idPegawai);
    }

    /***
     * value of parameter id just id_pegawai
     */
    public static String delete(String idPegawai) {
        return String.format("%s/api/pegawai/?id=%s", URL_SERVER, idPegawai);
    }
}
