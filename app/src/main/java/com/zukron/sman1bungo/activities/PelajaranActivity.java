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
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.adapter.PelajaranAdapter;
import com.zukron.sman1bungo.fragment.detail.DetailPelajaranFragment;
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.model.dao.PelajaranDao;

import java.util.ArrayList;

public class PelajaranActivity extends AppCompatActivity implements View.OnClickListener, PelajaranDao.onListener, PelajaranAdapter.onEditSelectedItem, PelajaranAdapter.onDeleteSelectedItem {
    private ListView lvListItem;
    private Button btnTambah;
    private PelajaranAdapter pelajaranAdapter;
    private ProgressDialog progressDialog;
    private PelajaranDao pelajaranDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        pelajaranDao = new PelajaranDao(this, this);

        lvListItem = findViewById(R.id.lv_list_item);
        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setText(R.string.tambah_pelajaran);
        btnTambah.setOnClickListener(this);

        progressDialog = new ProgressDialog(PelajaranActivity.this);
        progressDialog.setMessage("Tunggu ambil data");
        progressDialog.show();

        retrievePelajaranData();
    }

    /***
     * retrieve data from server
     */
    private void retrievePelajaranData() {
        pelajaranDao.getAll();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailPelajaranFragment detailPelajaranFragment = DetailPelajaranFragment.newInstance("add");
        detailPelajaranFragment.show(fragmentManager, DetailPelajaranFragment.class.getSimpleName());
    }

    @Override
    public void onEditSelected(Pelajaran pelajaran) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailPelajaranFragment detailPelajaranFragment = DetailPelajaranFragment.newInstance("edit", pelajaran);
        detailPelajaranFragment.show(fragmentManager, DetailPelajaranFragment.class.getSimpleName());
    }

    @Override
    public void onDeleteSelected(final Pelajaran pelajaran) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PelajaranActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Bisa menyebabkan data yang menggunakan data yang dihapus akan terhapus.\n\nYakin menghapus ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(pelajaran);
            }

            /***
             * delete data when user click "OK" in dialog
             */
            private void deleteData(Pelajaran pelajaran) {
                pelajaranDao.delete(pelajaran.getIdPelajaran());
                reloadActivity();
                Toast.makeText(PelajaranActivity.this, "Berhasil hapus", Toast.LENGTH_SHORT).show();
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
    public void pelajaranResponse(Pelajaran pelajaran) {

    }

    /***
     * menampilkan data yang telah di retrieve ke list
     */
    @Override
    public void pelajaranListResponse(ArrayList<Pelajaran> pelajaranList) {
        pelajaranAdapter = new PelajaranAdapter(this, pelajaranList, this, this);
        lvListItem.setAdapter(pelajaranAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void defaultResponse(String response) {

    }

    @Override
    public void errorResponse(VolleyError error) {

    }
}
