package com.zukron.sman1bungo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.detail.DetailAdminActivity;
import com.zukron.sman1bungo.adapter.AdminAdapter;
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.dao.AdminDao;
import com.zukron.sman1bungo.model.dao.GajiDao;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, AdminDao.onListener, GajiDao.onListener, AdminAdapter.onEditSelectedItem, AdminAdapter.onDeleteSelectedItem {
    private AdminAdapter adminAdapter;
    private ListView lvListItem;
    private ArrayList<Gaji> gajiList;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private AdminDao adminDao;
    private GajiDao gajiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        adminDao = new AdminDao(this, this);
        gajiDao = new GajiDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_administrator);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrieveAdminData();
        retrieveGajiData();
    }

    /***
     * retrieve data from server
     */
    private void retrieveAdminData() {
        adminDao.getAll();
    }

    /***
     * retrieve data from server, this data will be using on DetailPegawaiActivity
     */
    private void retrieveGajiData() {
        gajiDao.getAll();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminActivity.this, DetailAdminActivity.class);
        intent.putExtra("action", "add");
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan PegawaiActivity yang menyimpan list data lama
        finish();
        // membuat DetailPegawaiActivity
        startActivity(intent);
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
    public void adminResponse(Admin admin) {

    }

    /***
     * menampilkan item ke list
     */
    @Override
    public void adminListResponse(ArrayList<Admin> adminList) {
        adminAdapter = new AdminAdapter(this, adminList, this, this);
        lvListItem.setAdapter(adminAdapter);
        progressDialog.dismiss();
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

    @Override
    public void onEditSelected(Admin admin) {
        Intent intent = new Intent(AdminActivity.this, DetailAdminActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("admin", admin);
        intent.putParcelableArrayListExtra("gajiList", gajiList);
        // menyelesaikan PegawaiActivity yang menyimpan list data lama
        finish();
        // membuat DetailPegawaiActivity
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(final Admin admin) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(admin);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Admin admin) {
                adminDao.delete(admin.getIdAdmin());
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
}
