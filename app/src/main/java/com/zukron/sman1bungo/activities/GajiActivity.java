package com.zukron.sman1bungo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.adapter.GajiAdapter;
import com.zukron.sman1bungo.fragment.detail.DetailGajiFragment;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.dao.GajiDao;

import java.util.ArrayList;

public class GajiActivity extends AppCompatActivity implements View.OnClickListener, GajiDao.onListener, GajiAdapter.onEditSelectedItem, GajiAdapter.onDeleteSelectedItem {
    private GajiAdapter gajiAdapter;
    private ListView lvListItem;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private GajiDao gajiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        gajiDao = new GajiDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_gaji);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(GajiActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveGajiData();
    }

    /***
     * retrieve data from server
     */
    private void retrieveGajiData() {
        gajiDao.getAll();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailGajiFragment detailGajiFragment = DetailGajiFragment.newInstance("add");
        detailGajiFragment.show(fragmentManager, DetailGajiFragment.class.getSimpleName());
    }

    @Override
    public void onEditSelected(Gaji gaji) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailGajiFragment detailGajiFragment = DetailGajiFragment.newInstance("edit", gaji);
        detailGajiFragment.show(fragmentManager, DetailGajiFragment.class.getSimpleName());
    }

    @Override
    public void onDeleteSelected(final Gaji gaji) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(GajiActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(gaji);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Gaji gaji) {
                gajiDao.delete(gaji.getIdGaji());
                if (gajiDao.isRequestFinished()) {
                    Toast.makeText(GajiActivity.this, "Berhasil Hapus", Toast.LENGTH_SHORT).show();
                    reloadActivity();
                }
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
    public void gajiResponse(Gaji gaji) {

    }

    /***
     * jika data sudah diambil maka ditambilkan ke dalam list
     */
    @Override
    public void gajiListResponse(ArrayList<Gaji> gajiList) {
        gajiAdapter = new GajiAdapter(this, gajiList, this, this);
        lvListItem.setAdapter(gajiAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void defaultResponse(String response) {

    }

    @Override
    public void errorResponse(VolleyError error) {

    }
}
