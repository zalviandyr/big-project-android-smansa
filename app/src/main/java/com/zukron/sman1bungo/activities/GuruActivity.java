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
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.detail.DetailGuruActivity;
import com.zukron.sman1bungo.adapter.GuruAdapter;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.model.dao.GajiDao;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.PelajaranDao;

import java.util.ArrayList;

public class GuruActivity extends AppCompatActivity implements View.OnClickListener, GuruDao.onListener, GajiDao.onListener, PelajaranDao.onListener, GuruAdapter.onEditSelectedItem, GuruAdapter.onDeleteSelectedItem {
    private GuruAdapter guruAdapter;
    private ListView lvListItem;
    private ArrayList<Guru> guruList;
    private ArrayList<Pelajaran> pelajaranList;
    private ArrayList<Gaji> gajiList;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private GuruDao guruDao;
    private GajiDao gajiDao;
    private PelajaranDao pelajaranDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        guruDao = new GuruDao(this, this);
        gajiDao = new GajiDao(this, this);
        pelajaranDao = new PelajaranDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_guru);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(GuruActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveGuruData();
        retrieveGajiData();
        retrievePelajaranData();
    }

    private void retrieveGuruData() {
        guruDao.getAll();
    }

    private void retrieveGajiData() {
        gajiDao.getAll();
    }

    private void retrievePelajaranData() {
        pelajaranDao.getAll();
    }

    /***
     * set list data after retrieve it from server
     * jika semua request berhasil
     */
    private void showGuru() {
        if (guruDao.isRequestFinished() && gajiDao.isRequestFinished() && pelajaranDao.isRequestFinished()) {
            guruAdapter = new GuruAdapter(GuruActivity.this, guruList, pelajaranList, this, this);
            lvListItem.setAdapter(guruAdapter);
        } else {
            retrieveGuruData();
            retrieveGajiData();
            retrievePelajaranData();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GuruActivity.this, DetailGuruActivity.class);
        intent.putExtra("action", "add");
        intent.putParcelableArrayListExtra("pelajaranList", pelajaranList);
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan GuruActivity yang menyimpan list data lama
        finish();
        // membuat DetailGuruActivity
        startActivity(intent);
    }

    @Override
    public void onEditSelected(Guru guru) {
        Intent intent = new Intent(GuruActivity.this, DetailGuruActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("guru", guru);
        intent.putParcelableArrayListExtra("pelajaranList", pelajaranList);
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan GuruActivity yang menyimpan list data lama
        finish();
        // membuat DetailGuruActivity
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(final Guru guru) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuruActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(guru);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Guru guru) {
                guruDao.delete(guru.getNip());
                Toast.makeText(GuruActivity.this, "Berhasil hapus", Toast.LENGTH_SHORT).show();
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void guruResponse(Guru guru) {
        // no need
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        this.guruList = guruList;
        showGuru();
        progressDialog.dismiss();
    }

    @Override
    public void gajiResponse(Gaji gaji) {
        // no need
    }

    @Override
    public void gajiListResponse(ArrayList<Gaji> gajiList) {
        this.gajiList = gajiList;
    }

    @Override
    public void pelajaranResponse(Pelajaran pelajaran) {
        // no need
    }

    @Override
    public void pelajaranListResponse(ArrayList<Pelajaran> pelajaranList) {
        this.pelajaranList = pelajaranList;
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
