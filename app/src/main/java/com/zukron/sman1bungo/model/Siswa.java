package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

public class Siswa implements Parcelable {
    private String nisn;
    private String firstName;
    private String lastName;
    private String jekel;
    private String email;
    private String noHp;
    private LocalDate tanggalLahir;
    private String kotaLahir;
    private String provinsiLahir;
    private String idKelas;
    private String username;

    public Siswa(String nisn, String firstName, String lastName, String jekel, String email, String noHp, LocalDate tanggalLahir, String kotaLahir, String provinsiLahir, String idKelas, String username) {
        this.nisn = nisn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jekel = jekel;
        this.email = email;
        this.noHp = noHp;
        this.tanggalLahir = tanggalLahir;
        this.kotaLahir = kotaLahir;
        this.provinsiLahir = provinsiLahir;
        this.idKelas = idKelas;
        this.username = username;
    }

    protected Siswa(Parcel in) {
        nisn = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        jekel = in.readString();
        email = in.readString();
        noHp = in.readString();
        tanggalLahir = (LocalDate) in.readSerializable();
        kotaLahir = in.readString();
        provinsiLahir = in.readString();
        idKelas = in.readString();
        username = in.readString();
    }

    public static final Creator<Siswa> CREATOR = new Creator<Siswa>() {
        @Override
        public Siswa createFromParcel(Parcel in) {
            return new Siswa(in);
        }

        @Override
        public Siswa[] newArray(int size) {
            return new Siswa[size];
        }
    };

    public String getNisn() {
        return nisn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJekel() {
        return jekel;
    }

    public String getEmail() {
        return email;
    }

    public String getNoHp() {
        return noHp;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public String getKotaLahir() {
        return kotaLahir;
    }

    public String getProvinsiLahir() {
        return provinsiLahir;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nisn);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(jekel);
        dest.writeString(email);
        dest.writeString(noHp);
        dest.writeSerializable(tanggalLahir);
        dest.writeString(kotaLahir);
        dest.writeString(provinsiLahir);
        dest.writeString(idKelas);
        dest.writeString(username);
    }
}
