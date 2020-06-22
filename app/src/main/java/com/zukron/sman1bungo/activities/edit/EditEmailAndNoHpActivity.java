package com.zukron.sman1bungo.activities.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

/***
 * EditEmailAndNoHpActivity hanya untuk guru dan siswa
 */
public class EditEmailAndNoHpActivity extends AppCompatActivity implements View.OnClickListener, GuruDao.onListener, SiswaDao.onListener {
    private TextInputLayout inputLayoutEmailEditEmailAndNoHp, inputLayoutNoHpEditEmailAndNoHp;
    private TextInputEditText inputEmailEditEmailAndNoHp, inputNoHpEditEmailAndNoHp;
    private Button btnOkEditEmailAndNoHp;
    private GuruDao guruDao;
    private Guru guru;
    private SiswaDao siswaDao;
    private Siswa siswa;
    private ProgressDialog progressDialog;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email_and_no_hp);

        guruDao = new GuruDao(this, this);
        siswaDao = new SiswaDao(this, this);

        inputLayoutEmailEditEmailAndNoHp = findViewById(R.id.input_layout_email_edit_email_and_no_hp);
        inputEmailEditEmailAndNoHp = findViewById(R.id.input_email_edit_email_and_no_hp);
        inputLayoutNoHpEditEmailAndNoHp = findViewById(R.id.input_layout_no_hp_edit_email_and_no_hp);
        inputNoHpEditEmailAndNoHp = findViewById(R.id.input_no_hp_edit_email_and_no_hp);
        btnOkEditEmailAndNoHp = findViewById(R.id.btn_ok_edit_email_and_no_hp);
        btnOkEditEmailAndNoHp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveData();
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData();
            reloadActivity();
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputEmailEditEmailAndNoHp.getText().toString().trim())) {
            inputLayoutEmailEditEmailAndNoHp.setError(errorMsg);
            valid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(inputEmailEditEmailAndNoHp.getText().toString()).matches()) {
                inputLayoutEmailEditEmailAndNoHp.setError("Email tidak valid");
                valid = false;
            }
        }

        if (TextUtils.isEmpty(inputNoHpEditEmailAndNoHp.getText().toString().trim())) {
            inputLayoutNoHpEditEmailAndNoHp.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    private void sendData() {
        String email = inputEmailEditEmailAndNoHp.getText().toString().trim();
        String noHp = inputNoHpEditEmailAndNoHp.getText().toString().trim();

        if (userSession.getLevel().equals("Guru")) {
            Guru guruData = new Guru(
                    guru.getNip(), guru.getFirstName(), guru.getLastName(), email,
                    noHp, guru.getJekel(), guru.getTanggalLahir(),
                    guru.getProvinsiLahir(), guru.getKotaLahir(), guru.getGolongan(),
                    guru.getIdPelajaran(), guru.getUsername(), guru.getIdGaji()
            );

            guruDao.putFull(guruData);
        }

        if (userSession.getLevel().equals("Siswa")) {
            Siswa siswaData = new Siswa(
                    siswa.getNisn(), siswa.getFirstName(), siswa.getLastName(), siswa.getJekel(), email,
                    noHp, siswa.getTanggalLahir(), siswa.getKotaLahir(),
                    siswa.getProvinsiLahir(), siswa.getIdKelas(), siswa.getUsername()
            );

            siswaDao.putFull(siswaData);
        }
    }

    private void retrieveData() {
        String username = userSession.getUsername();

        if (userSession.getLevel().equals("Guru"))
            guruDao.getUsername(username);
        if (userSession.getLevel().equals("Siswa"))
            siswaDao.getUsername(username);
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void guruResponse(Guru guru) {
        if (userSession.getLevel().equals("Guru")) {
            progressDialog.dismiss();
            this.guru = guru;

            String email = (guru.getEmail().equals("null")) ? "" : guru.getEmail();
            String noHp = (guru.getNoHp().equals("null")) ? "" : guru.getNoHp();

            inputEmailEditEmailAndNoHp.setText(email);
            inputNoHpEditEmailAndNoHp.setText(noHp);
        }
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        if (userSession.getLevel().equals("Siswa")) {
            progressDialog.dismiss();
            this.siswa = siswa;

            String email = (siswa.getEmail().equals("null")) ? "" : siswa.getEmail();
            String noHp = (siswa.getNoHp().equals("null")) ? "" : siswa.getNoHp();

            inputEmailEditEmailAndNoHp.setText(email);
            inputNoHpEditEmailAndNoHp.setText(noHp);
        }
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
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