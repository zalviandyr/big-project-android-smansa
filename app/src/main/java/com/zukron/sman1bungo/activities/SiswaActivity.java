package com.zukron.sman1bungo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.detail.DetailSiswaActivity;
import com.zukron.sman1bungo.adapter.SiswaAdapter;
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.dao.KelasDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;

import java.util.ArrayList;

public class SiswaActivity extends AppCompatActivity implements View.OnClickListener, SiswaDao.onListener, KelasDao.onListener, SiswaAdapter.onEditSelectedItem, SiswaAdapter.onDeleteSelectedItem {
    private SiswaAdapter siswaAdapter;
    private ListView lvListItem;
    private ArrayList<Siswa> siswaList;
    private ArrayList<Kelas> kelasList;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private SiswaDao siswaDao;
    private KelasDao kelasDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        siswaDao = new SiswaDao(this, this);
        kelasDao = new KelasDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_siswa);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(SiswaActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveSiswaData();
        retrieveKelasData();
    }

    private void retrieveSiswaData() {
        siswaDao.getAll();
    }

    private void retrieveKelasData() {
        kelasDao.getAll();
    }

    private void showSiswa() {
        if (siswaDao.isRequestFinished() && kelasDao.isRequestFinished()) {
            siswaAdapter = new SiswaAdapter(this, siswaList, kelasList, this, this);
            lvListItem.setAdapter(siswaAdapter);
        } else {
            retrieveSiswaData();
            retrieveKelasData();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SiswaActivity.this, DetailSiswaActivity.class);
        intent.putExtra("action", "add");
        intent.putParcelableArrayListExtra("kelasList", kelasList);
//         menyelesaikan SiswaActivity yang menyimpan list data lama
        finish();
//         membuat DetailSiswaActivity
        startActivity(intent);
    }

    @Override
    public void onEditSelected(Siswa siswa) {
        Intent intent = new Intent(SiswaActivity.this, DetailSiswaActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("siswa", siswa);
        intent.putParcelableArrayListExtra("kelasList", kelasList);
        // menyelesaikan SiswaActivity yang menyimpan list data lama
        finish();
        // membuat DetailSiswaActivity
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(final Siswa siswa) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SiswaActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(siswa);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Siswa siswa) {
                siswaDao.delete(siswa.getNisn());
                reloadActivity();
                Toast.makeText(SiswaActivity.this, "Berhasil hapus", Toast.LENGTH_SHORT).show();
            }

            /***
             * reload activity jika data berhasil di delete
             */
            private void reloadActivity() {
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void kelasResponse(Kelas kelas) {
        // no need
    }

    @Override
    public void kelasListResponse(ArrayList<Kelas> kelasList) {
        this.kelasList = kelasList;
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        // no need
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
        this.siswaList = siswaList;
        showSiswa();
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
