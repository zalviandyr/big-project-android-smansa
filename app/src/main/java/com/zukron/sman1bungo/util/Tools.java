package com.zukron.sman1bungo.util;

import com.zukron.sman1bungo.model.dao.GuruDao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Tools {
    public static String toMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(str.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<String> provinsiList() {
        ArrayList<String> provinsi = new ArrayList<>();
        // Sumatra
        provinsi.add("Aceh");
        provinsi.add("Sumatera Utara");
        provinsi.add("Sumatera Barat");
        provinsi.add("Riau");
        provinsi.add("Kepulauan Riau");
        provinsi.add("Jambi");
        provinsi.add("Bengkulu");
        provinsi.add("Sumatera Selatan");
        provinsi.add("Kepulauan Bangka Belitung");
        provinsi.add("Lampung");

        // Jawa
        provinsi.add("Banten");
        provinsi.add("Jawa Barat");
        provinsi.add("DKI Jakarta");
        provinsi.add("Jawa Tengah");
        provinsi.add("Yogyakarta");
        provinsi.add("Jawa Timur");

        // Bali dan NT
        provinsi.add("Bali");
        provinsi.add("Nusa Tenggara Barat");
        provinsi.add("Nusa Tenggara Timur");

        // Kalimantan
        provinsi.add("Kalimantan Utara");
        provinsi.add("Kalimantan Barat");
        provinsi.add("Kalimantan Tengah");
        provinsi.add("Kalimantan Selatan");
        provinsi.add("Kalimantan Timur");

        // Sulawesi
        provinsi.add("Gorontalo");
        provinsi.add("Sulawesi Utara");
        provinsi.add("Sulawesi Barat");
        provinsi.add("Sulawesi Tengah");
        provinsi.add("Sulawesi Selatan");
        provinsi.add("Sulawesi Tenggara");

        // Maluku dan papua
        provinsi.add("Maluku Utara");
        provinsi.add("Maluku");
        provinsi.add("Papua");
        provinsi.add("Papua Barat");

        return provinsi;
    }

    public static Object toIdr(Object value) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setPositivePrefix("Rp. ");
        decimalFormat.setNegativePrefix("Rp. -");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);
    }
}
