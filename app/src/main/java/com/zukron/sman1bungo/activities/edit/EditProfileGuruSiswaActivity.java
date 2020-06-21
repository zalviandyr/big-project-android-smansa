package com.zukron.sman1bungo.activities.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.DashboardActivity;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.fragment.ProfileFragment;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

public class EditProfileGuruSiswaActivity extends AppCompatActivity implements View.OnClickListener, GuruDao.onListener, SiswaDao.onListener {
    private TextInputLayout inputLayoutIdGuruSiswaEditProfile, inputLayoutFirstNameGuruSiswaEditProfile, inputLayoutLastNameGuruSiswaEditProfile;
    private TextInputEditText inputIdGuruSiswaEditProfile, inputFirstNameGuruSiswaEditProfile, inputLastNameGuruSiswaEditProfile;
    private RadioButton rbLakiLakiGuruSiswaEditProfile, rbPerempuanGuruSiswaEditProfile;
    private Button btnOkGuruSiswaEditProfile;
    private GuruDao guruDao;
    private SiswaDao siswaDao;
    private Guru guru;
    private Siswa siswa;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_guru_siswa);

        guruDao = new GuruDao(this, this);
        siswaDao = new SiswaDao(this, this);

        inputLayoutIdGuruSiswaEditProfile = findViewById(R.id.input_layout_id_guru_siswa_edit_profile);
        inputIdGuruSiswaEditProfile = findViewById(R.id.input_id_guru_siswa_edit_profile);
        inputLayoutFirstNameGuruSiswaEditProfile = findViewById(R.id.input_layout_first_name_guru_siswa_edit_profile);
        inputFirstNameGuruSiswaEditProfile = findViewById(R.id.input_first_name_guru_siswa_edit_profile);
        inputLayoutLastNameGuruSiswaEditProfile = findViewById(R.id.input_layout_last_name_guru_siswa_edit_profile);
        inputLastNameGuruSiswaEditProfile = findViewById(R.id.input_last_name_guru_siswa_edit_profile);
        rbLakiLakiGuruSiswaEditProfile = findViewById(R.id.rb_laki_laki_guru_siswa_edit_profile);
        rbPerempuanGuruSiswaEditProfile = findViewById(R.id.rb_perempuan_guru_siswa_edit_profile);
        btnOkGuruSiswaEditProfile = findViewById(R.id.btn_ok_guru_siswa_edit_profile);
        btnOkGuruSiswaEditProfile.setOnClickListener(this);

        retrieveData();
    }

    private void retrieveData() {
        if (userSession.getLevel().equals("Guru")) {
            String username = userSession.getUsername();
            inputLayoutIdGuruSiswaEditProfile.setHint("NIP");

            guruDao.getUsername(username);
        }

        if (userSession.getLevel().equals("Siswa")) {
            String username = userSession.getUsername();
            inputLayoutIdGuruSiswaEditProfile.setHint("NISN");

            siswaDao.getUsername(username);
        }
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData();
            moveToDashboardActivity();
        }
    }

    private void sendData() {
        if (userSession.getLevel().equals("Guru")) {
            String nip = inputIdGuruSiswaEditProfile.getText().toString().trim();
            String firstName = inputFirstNameGuruSiswaEditProfile.getText().toString().trim();
            String lastName = inputLastNameGuruSiswaEditProfile.getText().toString().trim();
            String jekel = "";
            if (rbLakiLakiGuruSiswaEditProfile.isChecked())
                jekel = rbLakiLakiGuruSiswaEditProfile.getText().toString().trim();
            if (rbPerempuanGuruSiswaEditProfile.isChecked())
                jekel = rbPerempuanGuruSiswaEditProfile.getText().toString().trim();

            Guru guruData = new Guru(
                    nip, firstName, lastName, guru.getEmail(),
                    guru.getNoHp(), jekel, guru.getTanggalLahir(),
                    guru.getProvinsiLahir(), guru.getKotaLahir(), guru.getGolongan(),
                    guru.getIdPelajaran(), guru.getUsername(), guru.getIdGaji()
            );

            guruDao.putFull(guruData);
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputIdGuruSiswaEditProfile.getText().toString().trim())) {
            inputLayoutIdGuruSiswaEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputFirstNameGuruSiswaEditProfile.getText().toString().trim())) {
            inputLayoutFirstNameGuruSiswaEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNameGuruSiswaEditProfile.getText().toString().trim())) {
            inputLayoutLastNameGuruSiswaEditProfile.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    @Override
    public void guruResponse(Guru guru) {
        if (userSession.getLevel().equals("Guru")) {
            this.guru = guru;

            String nip = guru.getNip();
            String firstName = guru.getFirstName();
            String lastName = guru.getLastName();
            String jekel = guru.getJekel();

            inputIdGuruSiswaEditProfile.setText(nip);
            inputFirstNameGuruSiswaEditProfile.setText(firstName);
            inputLastNameGuruSiswaEditProfile.setText(lastName);

            if (jekel.equals("Laki laki"))
                rbLakiLakiGuruSiswaEditProfile.setChecked(true);
            if (jekel.equals("Perempuan"))
                rbPerempuanGuruSiswaEditProfile.setChecked(true);
        }
    }

    private void moveToDashboardActivity(){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        if (userSession.getLevel().equals("Siswa")) {
            this.siswa = siswa;

            String nisn = siswa.getNisn();
            String firstName = siswa.getFirstName();
            String lastName = siswa.getLastName();
            String jekel = siswa.getJekel();

            inputIdGuruSiswaEditProfile.setText(nisn);
            inputFirstNameGuruSiswaEditProfile.setText(firstName);
            inputLastNameGuruSiswaEditProfile.setText(lastName);
            if (jekel.equals("Laki laki"))
                rbLakiLakiGuruSiswaEditProfile.setChecked(true);
            if (jekel.equals("Perempuan"))
                rbPerempuanGuruSiswaEditProfile.setChecked(true);
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
        error.printStackTrace();
    }
}