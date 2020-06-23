package com.zukron.sman1bungo.activities.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

public class EditNoHpActivity extends AppCompatActivity implements View.OnClickListener, PegawaiDao.onListener {
    private TextInputLayout inputLayoutNoHpEditNoHp;
    private TextInputEditText inputNoHpEditNoHp;
    private Button btnOkEditNoHp;
    private PegawaiDao pegawaiDao;
    private Pegawai pegawai;
    private ProgressDialog progressDialog;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_no_hp);

        pegawaiDao = new PegawaiDao(this, this);

        inputLayoutNoHpEditNoHp = findViewById(R.id.input_layout_no_hp_edit_no_hp);
        inputNoHpEditNoHp = findViewById(R.id.input_no_hp_edit_no_hp);
        btnOkEditNoHp = findViewById(R.id.btn_ok_edit_no_hp);
        btnOkEditNoHp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveData();
    }

    private void retrieveData() {
        String username = userSession.getUsername();
        pegawaiDao.getUsername(username);
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData();
            reloadActivity();
        }
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNoHpEditNoHp.getText().toString().trim())) {
            inputLayoutNoHpEditNoHp.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    private void sendData() {
        String noHp = inputNoHpEditNoHp.getText().toString().trim();

        Pegawai pegawaiData = new Pegawai(
                pegawai.getIdPegawai(), pegawai.getFirstName(), pegawai.getLastName(), pegawai.getTanggalLahir(),
                noHp, pegawai.getJekel(), pegawai.getJabatan(),
                pegawai.getIdGaji(), pegawai.getUsername()
        );

        pegawaiDao.putFull(pegawaiData);
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        progressDialog.dismiss();
        this.pegawai = pegawai;

        String noHp = (pegawai.getNoHp().equals("null")) ? "" : pegawai.getNoHp();

        inputNoHpEditNoHp.setText(noHp);
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void defaultResponse(String response) {
        if (response.equals("Berhasil Ubah")) {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        progressDialog.dismiss();
        error.printStackTrace();
    }
}