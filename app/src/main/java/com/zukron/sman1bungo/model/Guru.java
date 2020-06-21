package com.zukron.sman1bungo.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

public class Guru implements Parcelable {
    private String nip;
    private String firstName;
    private String lastName;
    private String email;
    private String noHp;
    private String jekel;
    private LocalDate tanggalLahir;
    private String provinsiLahir;
    private String kotaLahir;
    private String golongan;
    private String idPelajaran;
    private String username;
    private String idGaji;

    public Guru(String nip, String firstName, String lastName, String email, String noHp, String jekel, LocalDate tanggalLahir, String provinsiLahir, String kotaLahir, String golongan, String idPelajaran, String username, String idGaji) {
        this.nip = nip;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.noHp = noHp;
        this.jekel = jekel;
        this.tanggalLahir = tanggalLahir;
        this.provinsiLahir = provinsiLahir;
        this.kotaLahir = kotaLahir;
        this.golongan = golongan;
        this.idPelajaran = idPelajaran;
        this.username = username;
        this.idGaji = idGaji;
    }

    protected Guru(Parcel in) {
        nip = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        noHp = in.readString();
        jekel = in.readString();
        tanggalLahir = (LocalDate) in.readSerializable();
        provinsiLahir = in.readString();
        kotaLahir = in.readString();
        golongan = in.readString();
        idPelajaran = in.readString();
        username = in.readString();
        idGaji = in.readString();
    }

    public static final Creator<Guru> CREATOR = new Creator<Guru>() {
        @Override
        public Guru createFromParcel(Parcel in) {
            return new Guru(in);
        }

        @Override
        public Guru[] newArray(int size) {
            return new Guru[size];
        }
    };

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getJekel() {
        return jekel;
    }

    public void setJekel(String jekel) {
        this.jekel = jekel;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getProvinsiLahir() {
        return provinsiLahir;
    }

    public void setProvinsiLahir(String provinsiLahir) {
        this.provinsiLahir = provinsiLahir;
    }

    public String getKotaLahir() {
        return kotaLahir;
    }

    public void setKotaLahir(String kotaLahir) {
        this.kotaLahir = kotaLahir;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getIdPelajaran() {
        return idPelajaran;
    }

    public void setIdPelajaran(String idPelajaran) {
        this.idPelajaran = idPelajaran;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdGaji() {
        return idGaji;
    }

    public void setIdGaji(String idGaji) {
        this.idGaji = idGaji;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nip);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(noHp);
        dest.writeString(jekel);
        dest.writeSerializable(tanggalLahir);
        dest.writeString(provinsiLahir);
        dest.writeString(kotaLahir);
        dest.writeString(golongan);
        dest.writeString(idPelajaran);
        dest.writeString(username);
        dest.writeString(idGaji);
    }
}
