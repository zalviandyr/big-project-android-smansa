package com.zukron.sman1bungo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gaji implements Parcelable {
    private String idGaji;
    private int gajiPokok;

    public Gaji(String idGaji, int gajiPokok) {
        this.idGaji = idGaji;
        this.gajiPokok = gajiPokok;
    }

    protected Gaji(Parcel in) {
        idGaji = in.readString();
        gajiPokok = in.readInt();
    }

    public static final Creator<Gaji> CREATOR = new Creator<Gaji>() {
        @Override
        public Gaji createFromParcel(Parcel in) {
            return new Gaji(in);
        }

        @Override
        public Gaji[] newArray(int size) {
            return new Gaji[size];
        }
    };

    public String getIdGaji() {
        return idGaji;
    }

    public int getGajiPokok() {
        return gajiPokok;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idGaji);
        dest.writeInt(gajiPokok);
    }
}
