package com.zukron.sman1bungo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.detail.DetailPegawaiActivity;
import com.zukron.sman1bungo.adapter.PegawaiAdapter;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.dao.GajiDao;
import com.zukron.sman1bungo.model.dao.PegawaiDao;

import java.util.ArrayList;

public class PegawaiActivity extends AppCompatActivity implements View.OnClickListener, PegawaiDao.onListener, GajiDao.onListener,PegawaiAdapter.onEditSelectedItem, PegawaiAdapter.onDeleteSelectedItem {
    private PegawaiAdapter pegawaiAdapter;
    private ListView lvListItem;
    private ArrayList<Gaji> gajiList;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private PegawaiDao pegawaiDao;
    private GajiDao gajiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        pegawaiDao = new PegawaiDao(this, this);
        gajiDao = new GajiDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_pegawai);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(PegawaiActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrievePegawaiData();
        retrieveGajiData();
    }

    /***
     * retrieve data from server
     */
    private void retrievePegawaiData() {
        pegawaiDao.getAll();
    }

    /***
     * retrieve data from server, this data will be using on DetailPegawaiActivity
     */
    private void retrieveGajiData() {
        gajiDao.getAll();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PegawaiActivity.this, DetailPegawaiActivity.class);
        intent.putExtra("action", "add");
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan PegawaiActivity yang menyimpan list data lama
        finish();
        // membuat DetailPegawaiActivity
        startActivity(intent);
    }

    @Override
    public void onEditSelected(Pegawai pegawai) {
        Intent intent = new Intent(PegawaiActivity.this, DetailPegawaiActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("pegawai", pegawai);
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan PegawaiActivity yang menyimpan list data lama
        finish();
        // membuat DetailPegawaiActivity
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(final Pegawai pegawai) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PegawaiActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(pegawai);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Pegawai pegawai) {
                pegawaiDao.delete(pegawai.getIdPegawai());
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

    /***
     * reload activity jika data berhasil di delete
     */
    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {

    }

    /***
     * menampilkan item ke list
     */
    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        pegawaiAdapter = new PegawaiAdapter(this, pegawaiList, this, this);
        lvListItem.setAdapter(pegawaiAdapter);
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
    public void messageResponse(int method, String message) {
        if (method == Request.Method.DELETE) {
            reloadActivity();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
