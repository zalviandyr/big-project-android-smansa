package com.zukron.sman1bungo.activities.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Sekolah;
import com.zukron.sman1bungo.model.dao.SekolahDao;

import java.util.Locale;

public class DetailSekolahMapsActivity extends AppCompatActivity implements SekolahDao.onListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final int INITIAL_ZOOM = 15;
    private LatLng latSman1bungo;
    private Sekolah sekolah;
    private double longitudePerpindahan = 0;
    private double latitudePerpindahan = 0;
    private SekolahDao sekolahDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sekolah_maps);

        sekolahDao = new SekolahDao(this, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add Lat and long position sekolah
        sekolah = getIntent().getParcelableExtra("sekolah");
        assert sekolah != null;
        latSman1bungo = new LatLng(sekolah.getLatitude(), sekolah.getLongitude());

        mMap.addMarker(new MarkerOptions().position(latSman1bungo).title("SMAN 1 Muara Bungo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latSman1bungo));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latSman1bungo, INITIAL_ZOOM));

        enableMyLocation();
        enableDropPin(mMap);
        enableLongClick(mMap);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void enableDropPin(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker marker = map.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));
                marker.showInfoWindow();
            }
        });
    }

    private void enableLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(), getString(R.string.lat_long_snippet), latLng.latitude, latLng.latitude);
                map.clear();
                longitudePerpindahan = latLng.longitude;
                latitudePerpindahan = latLng.latitude;
                map.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.dropped_pin)).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        // cek ada perpindahan lokasi atau tidak
        if ((longitudePerpindahan != 0) && (latitudePerpindahan != 0)) {
            if ((longitudePerpindahan != latSman1bungo.longitude) && (latitudePerpindahan != latSman1bungo.latitude)) {
                createAlertDialog();
            }
        } else {
            moveToDetailSekolahActivity();
        }
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubah Posisi");
        builder.setMessage("Yakin ubah posisi ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendDataToServer();
                moveToDetailSekolahActivity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveToDetailSekolahActivity();
            }
        });

        builder.show();
    }

    private void sendDataToServer() {
        Sekolah sekolahData = new Sekolah(sekolah.getNama(), sekolah.getAlamat(), sekolah.getKota(), sekolah.getProvinsi(), longitudePerpindahan, latitudePerpindahan);
        sekolahDao.put(sekolahData);
    }

    private void moveToDetailSekolahActivity() {
        // reload DetailSekolahActivity
        Intent intent = new Intent(DetailSekolahMapsActivity.this, DetailSekolahActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void sekolahResponse(Sekolah sekolah) {
        // no need
    }

    @Override
    public void defaultResponse(String response) {
        Toast.makeText(DetailSekolahMapsActivity.this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
