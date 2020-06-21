package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pelajaran implements Parcelable {
    private String idPelajaran;
    private String nama;

    public Pelajaran(String idPelajaran, String nama) {
        this.idPelajaran = idPelajaran;
        this.nama = nama;
    }

    protected Pelajaran(Parcel in) {
        idPelajaran = in.readString();
        nama = in.readString();
    }

    public static final Creator<Pelajaran> CREATOR = new Creator<Pelajaran>() {
        @Override
        public Pelajaran createFromParcel(Parcel in) {
            return new Pelajaran(in);
        }

        @Override
        public Pelajaran[] newArray(int size) {
            return new Pelajaran[size];
        }
    };

    public String getIdPelajaran() {
        return idPelajaran;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPelajaran);
        dest.writeString(nama);
    }
}
