package com.zukron.sman1bungo.activities.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.DashboardActivity;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.util.Session;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditProfilePegawaiActivity extends AppCompatActivity implements View.OnClickListener, PegawaiDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextInputLayout inputLayoutIdPegawaiEditProfile, inputLayoutFirstNamePegawaiEditProfile, inputLayoutLastNamePegawaiEditProfile;
    private TextInputEditText inputIdPegawaiEditProfile, inputFirstNamePegawaiEditProfile, inputLastNamePegawaiEditProfile;
    private RadioButton rbLakiLakiPegawaiEditProfile, rbPerempuanPegawaiEditProfile;
    private Button btnTanggaLahirPegawaiEditProfile, btnOkPegawaiEditProfile;
    private PegawaiDao pegawaiDao;
    private Pegawai pegawai;
    private ProgressDialog progressDialog;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_pegawai);

        pegawaiDao = new PegawaiDao(this, this);

        inputLayoutIdPegawaiEditProfile = findViewById(R.id.input_layout_id_pegawai_edit_profile);
        inputIdPegawaiEditProfile = findViewById(R.id.input_id_pegawai_edit_profile);
        inputLayoutFirstNamePegawaiEditProfile = findViewById(R.id.input_layout_first_name_pegawai_edit_profile);
        inputFirstNamePegawaiEditProfile = findViewById(R.id.input_first_name_pegawai_edit_profile);
        inputLayoutLastNamePegawaiEditProfile = findViewById(R.id.input_layout_last_name_pegawai_edit_profile);
        inputLastNamePegawaiEditProfile = findViewById(R.id.input_last_name_pegawai_edit_profile);

        rbLakiLakiPegawaiEditProfile = findViewById(R.id.rb_laki_laki_pegawai_edit_profile);
        rbPerempuanPegawaiEditProfile = findViewById(R.id.rb_perempuan_pegawai_edit_profile);

        btnTanggaLahirPegawaiEditProfile = findViewById(R.id.btn_tanggal_lahir_pegawai_edit_profile);
        btnTanggaLahirPegawaiEditProfile.setOnClickListener(this);
        btnOkPegawaiEditProfile = findViewById(R.id.btn_ok_pegawai_edit_profile);
        btnOkPegawaiEditProfile.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harus diisi");
        progressDialog.show();

        retrieveData();
    }

    private void retrieveData() {
        String username = userSession.getUsername();
        pegawaiDao.getUsername(username);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tanggal_lahir_pegawai_edit_profile:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_ok_pegawai_edit_profile:
                if (validateInput()) {
                    sendData();
                    moveToDashboardActivity();
                }
        }
    }

    private void sendData() {
        String idPegawai = inputIdPegawaiEditProfile.getText().toString().trim();
        String firstName = inputFirstNamePegawaiEditProfile.getText().toString().trim();
        String lastName = inputLastNamePegawaiEditProfile.getText().toString().trim();
        String jekel = "";
        if (rbLakiLakiPegawaiEditProfile.isChecked())
            jekel = "Laki laki";
        if (rbPerempuanPegawaiEditProfile.isChecked())
            jekel = "Perempuan";
        LocalDate tanggalLahir = LocalDate.parse(btnTanggaLahirPegawaiEditProfile.getText().toString().trim());

        Pegawai pegawaiData = new Pegawai(
                idPegawai, firstName, lastName, tanggalLahir,
                pegawai.getNoHp(), jekel, pegawai.getJabatan(),
                pegawai.getIdGaji(), pegawai.getUsername()
        );

        pegawaiDao.putFull(pegawaiData);
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputIdPegawaiEditProfile.getText().toString().trim())) {
            inputLayoutIdPegawaiEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputFirstNamePegawaiEditProfile.getText().toString().trim())) {
            inputLayoutFirstNamePegawaiEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNamePegawaiEditProfile.getText().toString().trim())) {
            inputLayoutLastNamePegawaiEditProfile.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    private void moveToDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        progressDialog.dismiss();

        this.pegawai = pegawai;
        String idPegawai = pegawai.getIdPegawai();
        String firstName = pegawai.getFirstName();
        String lastName = pegawai.getLastName();
        String jekel = pegawai.getJekel();
        String tanggalLahir = pegawai.getTanggalLahir().toString();

        inputIdPegawaiEditProfile.setText(idPegawai);
        inputFirstNamePegawaiEditProfile.setText(firstName);
        inputLastNamePegawaiEditProfile.setText(lastName);
        if (jekel.equals("Laki laki"))
            rbLakiLakiPegawaiEditProfile.setChecked(true);
        if (jekel.equals("Perempuan"))
            rbPerempuanPegawaiEditProfile.setChecked(true);
        btnTanggaLahirPegawaiEditProfile.setText(tanggalLahir);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = simpleDateFormat.format(calendar.getTime());

        btnTanggaLahirPegawaiEditProfile.setText(dateString);
    }
}