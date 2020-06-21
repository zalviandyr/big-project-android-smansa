package com.zukron.sman1bungo.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Sekolah;
import com.zukron.sman1bungo.model.dao.SekolahDao;

public class DetailSekolahActivity extends AppCompatActivity implements View.OnClickListener, SekolahDao.onListener, OnMapReadyCallback {
    private TextInputLayout inputLayoutNamaSekolahDetail, inputLayoutAlamatSekolahDetail, inputLayoutKotaSekolahDetail, inputLayoutProvinsiSekolahDetail;
    private TextInputEditText inputNamaSekolahDetail, inputAlamatSekolahDetail, inputKotaSekolahDetail, inputProvinsiSekolahDetail;
    private Button btnShowFullMapSekolahDetail, btnSaveSekolahDetail;
    private GoogleMap googleMapSekolah;
    private LatLng latSman1bungo;
    private Sekolah sekolah;
    private ProgressDialog progressDialog;
    private static final int INITIAL_ZOOM = 15;
    private SekolahDao sekolahDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sekolah);

        sekolahDao = new SekolahDao(this, this);

        // set map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map_sekolah_detail);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        inputLayoutNamaSekolahDetail = findViewById(R.id.input_layout_nama_sekolah_detail);
        inputNamaSekolahDetail = findViewById(R.id.input_nama_sekolah_detail);
        inputLayoutAlamatSekolahDetail = findViewById(R.id.input_layout_alamat_sekolah_detail);
        inputAlamatSekolahDetail = findViewById(R.id.input_alamat_sekolah_detail);
        inputLayoutKotaSekolahDetail = findViewById(R.id.input_layout_kota_sekolah_detail);
        inputKotaSekolahDetail = findViewById(R.id.input_kota_sekolah_detail);
        inputLayoutProvinsiSekolahDetail = findViewById(R.id.input_layout_provinsi_sekolah_detail);
        inputProvinsiSekolahDetail = findViewById(R.id.input_provinsi_sekolah_detail);

        btnShowFullMapSekolahDetail = findViewById(R.id.btn_show_full_map_sekolah_detail);
        btnShowFullMapSekolahDetail.setOnClickListener(this);
        btnSaveSekolahDetail = findViewById(R.id.btn_save_sekolah_detail);
        btnSaveSekolahDetail.setOnClickListener(this);

        progressDialog = new ProgressDialog(DetailSekolahActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveSekolahData();
    }

    /***
     * retrieve data dari server
     */
    private void retrieveSekolahData() {
        sekolahDao.get();
    }

    /***
     * Untuk menampilkan data-data yang ada ke TextInputEditText
     * @param sekolah object sekolah
     */
    private void showSekolahData(Sekolah sekolah) {
        inputNamaSekolahDetail.setText(sekolah.getNama());
        inputAlamatSekolahDetail.setText(sekolah.getAlamat());
        inputKotaSekolahDetail.setText(sekolah.getKota());
        inputProvinsiSekolahDetail.setText(sekolah.getProvinsi());
    }

    /***
     * Untuk menset marker lokasi Sekolah pada saat selesai retrieve data dari server
     */
    private void setMarkerSman(GoogleMap googleMapSekolah, Sekolah sekolah) {
        // Add Lat and long position sekolah
        latSman1bungo = new LatLng(sekolah.getLatitude(), sekolah.getLongitude());

        googleMapSekolah.addMarker(new MarkerOptions().position(latSman1bungo).title("SMAN 1 Muara Bungo"));
        googleMapSekolah.moveCamera(CameraUpdateFactory.newLatLng(latSman1bungo));
        googleMapSekolah.moveCamera(CameraUpdateFactory.newLatLngZoom(latSman1bungo, INITIAL_ZOOM));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_full_map_sekolah_detail:
                Intent intent = new Intent(DetailSekolahActivity.this, DetailSekolahMapsActivity.class);
                intent.putExtra("sekolah", sekolah);
                finish();
                startActivity(intent);
                break;
            case R.id.btn_save_sekolah_detail:
                if (validateInput()) {
                    sendDataToServer();
                    reloadActivity();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapSekolah = googleMap;
        googleMapSekolah.getUiSettings().setScrollGesturesEnabled(false); // set disable draggable
        googleMapSekolah.clear();
    }

    /***
     * send data to server jika user menekan tombol save dan semua input tidak ada yang kosong
     */
    private void sendDataToServer() {
        String nama = inputNamaSekolahDetail.getText().toString().trim();
        String alamat = inputAlamatSekolahDetail.getText().toString().trim();
        String kota = inputKotaSekolahDetail.getText().toString().trim();
        String provinsi = inputProvinsiSekolahDetail.getText().toString().trim();
        double longitude = latSman1bungo.longitude;
        double latitude = latSman1bungo.latitude;

        Sekolah sekolah = new Sekolah(nama, alamat, kota, provinsi, longitude, latitude);
        sekolahDao.put(sekolah);
    }

    /***
     * memeriksa jika ada input yang kosong
     * @return boolean
     */
    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNamaSekolahDetail.getText().toString().trim())) {
            inputLayoutNamaSekolahDetail.setError(errorMsg);
            valid = false;
        }
        if (TextUtils.isEmpty(inputAlamatSekolahDetail.getText().toString().trim())) {
            inputLayoutAlamatSekolahDetail.setError(errorMsg);
            valid = false;
        }
        if (TextUtils.isEmpty(inputKotaSekolahDetail.getText().toString().trim())) {
            inputLayoutKotaSekolahDetail.setError(errorMsg);
            valid = false;
        }
        if (TextUtils.isEmpty(inputProvinsiSekolahDetail.getText().toString().trim())) {
            inputLayoutProvinsiSekolahDetail.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    /***
     * reload activity jika user melakukan update
     */
    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void sekolahResponse(Sekolah sekolah) {
        this.sekolah = sekolah;
        showSekolahData(sekolah);
        setMarkerSman(googleMapSekolah, sekolah);
        progressDialog.dismiss();
    }

    @Override
    public void defaultResponse(String response) {
        // no need
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
