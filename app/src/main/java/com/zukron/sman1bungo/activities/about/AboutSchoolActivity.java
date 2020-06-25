package com.zukron.sman1bungo.activities.about;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Sekolah;
import com.zukron.sman1bungo.model.dao.SekolahDao;

public class AboutSchoolActivity extends AppCompatActivity implements View.OnClickListener, SekolahDao.onListener {
    private TextView tvSekolahAboutSchool, tvJalanAboutSchool, tvKotaProvinsiAboutSchool;
    private Button btnOkAboutSchool;
    private ProgressDialog progressDialog;
    private SekolahDao sekolahDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_school);

        sekolahDao = new SekolahDao(this, this);

        tvSekolahAboutSchool = findViewById(R.id.tv_sekolah_about_school);
        tvJalanAboutSchool = findViewById(R.id.tv_jalan_about_school);
        tvKotaProvinsiAboutSchool = findViewById(R.id.tv_kota_provinsi_about_school);
        btnOkAboutSchool = findViewById(R.id.btn_ok_about_school);
        btnOkAboutSchool.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveData();
    }

    private void retrieveData() {
        sekolahDao.get();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void sekolahResponse(Sekolah sekolah) {
        progressDialog.dismiss();

        String namaSekolah = sekolah.getNama();
        String jalan = sekolah.getAlamat();
        String kotaProvinsi = sekolah.getKota() + ", " + sekolah.getProvinsi();

        tvSekolahAboutSchool.setText(namaSekolah);
        tvJalanAboutSchool.setText(jalan);
        tvKotaProvinsiAboutSchool.setText(kotaProvinsi);
    }

    @Override
    public void messageResponse(int method, String message) {
        // no need
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}