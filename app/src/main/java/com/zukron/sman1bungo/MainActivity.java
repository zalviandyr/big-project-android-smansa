package com.zukron.sman1bungo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.VolleyError;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.dao.GajiDao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GajiDao.onListener {
    private GajiDao gajiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gajiDao = new GajiDao(this, this);

        gajiDao.get("G001");
        gajiDao.getAll();

        System.out.println("GET " + gajiDao.isRequestFinished());
        System.out.println("GET ALL " + gajiDao.isRequestFinished());
        System.out.println("ALL " + (gajiDao.isRequestFinished() && gajiDao.isRequestFinished()));
    }

    @Override
    public void errorResponse(VolleyError error) {

    }

    @Override
    public void gajiResponse(Gaji gaji) {
        System.out.println("XXX " + gaji.getIdGaji());
        System.out.println("XXX " + gaji.getGajiPokok());
    }

    @Override
    public void gajiListResponse(ArrayList<Gaji> gajiList) {
        for (Gaji gaji : gajiList) {
            System.out.println("ZZZ " + gaji);
        }
    }

    @Override
    public void defaultResponse(String response) {

    }
}
