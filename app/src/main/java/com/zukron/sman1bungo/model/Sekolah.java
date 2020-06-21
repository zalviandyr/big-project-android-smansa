package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sekolah implements Parcelable {
    private String nama;
    private String alamat;
    private String kota;
    private String provinsi;
    private double longitude;
    private double latitude;

    public Sekolah(String nama, String alamat, String kota, String provinsi, double longitude, double latitude) {
        this.nama = nama;
        this.alamat = alamat;
        this.kota = kota;
        this.provinsi = provinsi;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Sekolah(Parcel in) {
        nama = in.readString();
        alamat = in.readString();
        kota = in.readString();
        provinsi = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Sekolah> CREATOR = new Creator<Sekolah>() {
        @Override
        public Sekolah createFromParcel(Parcel in) {
            return new Sekolah(in);
        }

        @Override
        public Sekolah[] newArray(int size) {
            return new Sekolah[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKota() {
        return kota;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nama);
        parcel.writeString(alamat);
        parcel.writeString(kota);
        parcel.writeString(provinsi);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }
}
