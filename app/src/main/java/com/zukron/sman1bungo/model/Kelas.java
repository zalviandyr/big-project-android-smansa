package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kelas implements Parcelable {
    private String idKelas;
    private String nama;
    private String waliKelas;

    public Kelas(String idKelas, String nama, String waliKelas) {
        this.idKelas = idKelas;
        this.nama = nama;
        this.waliKelas = waliKelas;
    }

    protected Kelas(Parcel in) {
        idKelas = in.readString();
        nama = in.readString();
        waliKelas = in.readString();
    }

    public static final Creator<Kelas> CREATOR = new Creator<Kelas>() {
        @Override
        public Kelas createFromParcel(Parcel in) {
            return new Kelas(in);
        }

        @Override
        public Kelas[] newArray(int size) {
            return new Kelas[size];
        }
    };

    public String getIdKelas() {
        return idKelas;
    }

    public String getNama() {
        return nama;
    }

    public String getWaliKelas() {
        return waliKelas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idKelas);
        dest.writeString(nama);
        dest.writeString(waliKelas);
    }
}
