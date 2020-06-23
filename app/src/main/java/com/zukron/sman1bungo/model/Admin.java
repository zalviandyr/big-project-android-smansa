package com.zukron.sman1bungo.model;

import org.threeten.bp.LocalDate;

public class Admin {
    private String idAdmin;
    private String firstName;
    private String lastName;
    private LocalDate tanggalLahir;
    private String noHp;
    private String jekel;
    private String idGaji;
    private String username;

    public Admin(String idAdmin, String firstName, String lastName, LocalDate tanggalLahir, String noHp, String jekel, String idGaji, String username) {
        this.idAdmin = idAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tanggalLahir = tanggalLahir;
        this.noHp = noHp;
        this.jekel = jekel;
        this.idGaji = idGaji;
        this.username = username;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getJekel() {
        return jekel;
    }

    public String getIdGaji() {
        return idGaji;
    }

    public String getUsername() {
        return username;
    }
}
