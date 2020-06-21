package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

public class Pegawai implements Parcelable {
    private String idPegawai;
    private String firstName;
    private String lastName;
    private LocalDate tanggalLahir;
    private String noHp;
    private String jekel;
    private String jabatan;
    private String idGaji;
    private String username;

    public Pegawai(String idPegawai, String firstName, String lastName, LocalDate tanggalLahir, String noHp, String jekel, String jabatan, String idGaji, String username) {
        this.idPegawai = idPegawai;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tanggalLahir = tanggalLahir;
        this.noHp = noHp;
        this.jekel = jekel;
        this.jabatan = jabatan;
        this.idGaji = idGaji;
        this.username = username;
    }

    protected Pegawai(Parcel in) {
        idPegawai = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        tanggalLahir = (LocalDate) in.readSerializable();
        noHp = in.readString();
        jekel = in.readString();
        jabatan = in.readString();
        idGaji = in.readString();
        username = in.readString();
    }

    public static final Creator<Pegawai> CREATOR = new Creator<Pegawai>() {
        @Override
        public Pegawai createFromParcel(Parcel in) {
            return new Pegawai(in);
        }

        @Override
        public Pegawai[] newArray(int size) {
            return new Pegawai[size];
        }
    };

    public String getIdPegawai() {
        return idPegawai;
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

    public String getJabatan() {
        return jabatan;
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
        dest.writeString(idPegawai);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeSerializable(tanggalLahir);
        dest.writeString(noHp);
        dest.writeString(jekel);
        dest.writeString(jabatan);
        dest.writeString(idGaji);
        dest.writeString(username);
    }
}
