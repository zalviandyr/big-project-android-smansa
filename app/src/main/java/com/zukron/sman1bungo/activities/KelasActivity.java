package com.zukron.sman1bungo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.adapter.KelasAdapter;
import com.zukron.sman1bungo.fragment.detail.DetailKelasFragment;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.KelasDao;

import java.util.ArrayList;

public class KelasActivity extends AppCompatActivity implements View.OnClickListener, KelasDao.onListener, GuruDao.onListener, KelasAdapter.onEditSelectedItem, KelasAdapter.onDeleteSelectedItem {
    private KelasAdapter kelasAdapter;
    private ListView lvListItem;
    private ArrayList<Kelas> kelasList;
    private ArrayList<Guru> guruList;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private KelasDao kelasDao;
    private GuruDao guruDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        kelasDao = new KelasDao(this, this);
        guruDao = new GuruDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_kelas);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(KelasActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveKelasData();
        retrieveGuruData();
    }

    private void retrieveKelasData() {
        kelasDao.getAll();
    }

    private void retrieveGuruData() {
        guruDao.getAll();
    }

    /***
     * menampilkan data yang telah diambil dari server ke list,
     * jika kedua request berhasil
     */
    private void showKelas() {
        if (kelasDao.isRequestFinished() && guruDao.isRequestFinished()) {
            kelasAdapter = new KelasAdapter(this, kelasList, guruList, this, this);
            lvListItem.setAdapter(kelasAdapter);
        } else {
            retrieveKelasData();
            retrieveGuruData();
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailKelasFragment detailKelasFragment = DetailKelasFragment.newInstance("add", guruList);
        detailKelasFragment.show(fragmentManager, DetailKelasFragment.class.getSimpleName());
    }

    @Override
    public void onEditSelected(Kelas kelas) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailKelasFragment detailKelasFragment = DetailKelasFragment.newInstance("edit", kelas, guruList);
        detailKelasFragment.show(fragmentManager, DetailKelasFragment.class.getSimpleName());
    }

    @Override
    public void onDeleteSelected(final Kelas kelas) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(KelasActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(kelas);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Kelas kelas) {
                kelasDao.delete(kelas.getIdKelas());
                reloadActivity();
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
        showKelas();
        progressDialog.dismiss();
    }

    @Override
    public void guruResponse(Guru guru) {
        // no need
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        this.guruList = guruList;
    }

    @Override
    public void messageResponse(int method, String message) {
        if (method == Request.Method.DELETE)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
