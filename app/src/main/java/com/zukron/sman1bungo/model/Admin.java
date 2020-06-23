package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

public class Admin implements Parcelable {
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

    protected Admin(Parcel in) {
        idAdmin = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        tanggalLahir = (LocalDate) in.readSerializable();
        noHp = in.readString();
        jekel = in.readString();
        idGaji = in.readString();
        username = in.readString();
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAdmin);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeSerializable(tanggalLahir);
        dest.writeString(noHp);
        dest.writeString(jekel);
        dest.writeString(idGaji);
        dest.writeString(username);
    }
}
