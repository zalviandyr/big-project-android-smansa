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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.DashboardActivity;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.AdminDao;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.util.Session;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileAdminPegawaiActivity extends AppCompatActivity implements View.OnClickListener, PegawaiDao.onListener, AdminDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextInputLayout inputLayoutIdAdminPegawaiEditProfile, inputLayoutFirstNameAdminPegawaiEditProfile, inputLayoutLastNameAdminPegawaiEditProfile;
    private TextInputEditText inputIdAdminPegawaiEditProfile, inputFirstNameAdminPegawaiEditProfile, inputLastNameAdminPegawaiEditProfile;
    private RadioButton rbLakiLakiAdminPegawaiEditProfile, rbPerempuanAdminPegawaiEditProfile;
    private Button btnTanggaLahirAdminPegawaiEditProfile, btnOkAdminPegawaiEditProfile;
    private PegawaiDao pegawaiDao;
    private Pegawai pegawai;
    private AdminDao adminDao;
    private Admin admin;
    private ProgressDialog progressDialog;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin_pegawai);

        pegawaiDao = new PegawaiDao(this, this);
        adminDao = new AdminDao(this, this);

        inputLayoutIdAdminPegawaiEditProfile = findViewById(R.id.input_layout_id_admin_pegawai_edit_profile);
        inputIdAdminPegawaiEditProfile = findViewById(R.id.input_id_admin_pegawai_edit_profile);
        inputLayoutFirstNameAdminPegawaiEditProfile = findViewById(R.id.input_layout_first_name_admin_pegawai_edit_profile);
        inputFirstNameAdminPegawaiEditProfile = findViewById(R.id.input_first_name_admin_pegawai_edit_profile);
        inputLayoutLastNameAdminPegawaiEditProfile = findViewById(R.id.input_layout_last_name_admin_pegawai_edit_profile);
        inputLastNameAdminPegawaiEditProfile = findViewById(R.id.input_last_name_admin_pegawai_edit_profile);

        rbLakiLakiAdminPegawaiEditProfile = findViewById(R.id.rb_laki_laki_admin_pegawai_edit_profile);
        rbPerempuanAdminPegawaiEditProfile = findViewById(R.id.rb_perempuan_admin_pegawai_edit_profile);

        btnTanggaLahirAdminPegawaiEditProfile = findViewById(R.id.btn_tanggal_lahir_admin_pegawai_edit_profile);
        btnTanggaLahirAdminPegawaiEditProfile.setOnClickListener(this);
        btnOkAdminPegawaiEditProfile = findViewById(R.id.btn_ok_admin_pegawai_edit_profile);
        btnOkAdminPegawaiEditProfile.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harus diisi");
        progressDialog.show();

        retrieveData();
    }

    private void retrieveData() {
        String username = userSession.getUsername();

        if (userSession.getLevel().equals("Admin")) {
            inputIdAdminPegawaiEditProfile.setHint("ID Admin");

            adminDao.getUsername(username);
        }

        if (userSession.getLevel().equals("Pegawai")) {
            inputIdAdminPegawaiEditProfile.setHint("ID Pegawai");

            pegawaiDao.getUsername(username);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tanggal_lahir_admin_pegawai_edit_profile:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_ok_admin_pegawai_edit_profile:
                if (validateInput()) {
                    sendData();
                    moveToDashboardActivity();
                }
        }
    }

    private void sendData() {
        String idPegawai = inputIdAdminPegawaiEditProfile.getText().toString().trim();
        String firstName = inputFirstNameAdminPegawaiEditProfile.getText().toString().trim();
        String lastName = inputLastNameAdminPegawaiEditProfile.getText().toString().trim();
        String jekel = "";
        if (rbLakiLakiAdminPegawaiEditProfile.isChecked())
            jekel = "Laki laki";
        if (rbPerempuanAdminPegawaiEditProfile.isChecked())
            jekel = "Perempuan";
        LocalDate tanggalLahir = LocalDate.parse(btnTanggaLahirAdminPegawaiEditProfile.getText().toString().trim());

        if (userSession.getLevel().equals("Admin")) {
            Admin adminData = new Admin(
                    idPegawai, firstName, lastName,
                    tanggalLahir, admin.getNoHp(), jekel,
                    admin.getIdGaji(), admin.getUsername()
            );

            adminDao.putFull(adminData);
        }

        if (userSession.getLevel().equals("Pegawai")) {
            Pegawai pegawaiData = new Pegawai(
                    idPegawai, firstName, lastName, tanggalLahir,
                    pegawai.getNoHp(), jekel, pegawai.getJabatan(),
                    pegawai.getIdGaji(), pegawai.getUsername()
            );

            pegawaiDao.putFull(pegawaiData);
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputIdAdminPegawaiEditProfile.getText().toString().trim())) {
            inputLayoutIdAdminPegawaiEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputFirstNameAdminPegawaiEditProfile.getText().toString().trim())) {
            inputLayoutFirstNameAdminPegawaiEditProfile.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNameAdminPegawaiEditProfile.getText().toString().trim())) {
            inputLayoutLastNameAdminPegawaiEditProfile.setError(errorMsg);
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
        if (userSession.getLevel().equals("Pegawai")) {
            progressDialog.dismiss();

            this.pegawai = pegawai;
            String idPegawai = pegawai.getIdPegawai();
            String firstName = pegawai.getFirstName();
            String lastName = pegawai.getLastName();
            String jekel = pegawai.getJekel();
            String tanggalLahir = pegawai.getTanggalLahir().toString();

            inputIdAdminPegawaiEditProfile.setText(idPegawai);
            inputFirstNameAdminPegawaiEditProfile.setText(firstName);
            inputLastNameAdminPegawaiEditProfile.setText(lastName);
            if (jekel.equals("Laki laki"))
                rbLakiLakiAdminPegawaiEditProfile.setChecked(true);
            if (jekel.equals("Perempuan"))
                rbPerempuanAdminPegawaiEditProfile.setChecked(true);
            btnTanggaLahirAdminPegawaiEditProfile.setText(tanggalLahir);
        }
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void adminResponse(Admin admin) {
        if (userSession.getLevel().equals("Admin")) {
            progressDialog.dismiss();

            this.admin = admin;
            String idPegawai = admin.getIdAdmin();
            String firstName = admin.getFirstName();
            String lastName = admin.getLastName();
            String jekel = admin.getJekel();
            String tanggalLahir = admin.getTanggalLahir().toString();

            inputIdAdminPegawaiEditProfile.setText(idPegawai);
            inputFirstNameAdminPegawaiEditProfile.setText(firstName);
            inputLastNameAdminPegawaiEditProfile.setText(lastName);
            if (jekel.equals("Laki laki"))
                rbLakiLakiAdminPegawaiEditProfile.setChecked(true);
            if (jekel.equals("Perempuan"))
                rbPerempuanAdminPegawaiEditProfile.setChecked(true);
            btnTanggaLahirAdminPegawaiEditProfile.setText(tanggalLahir);
        }
    }

    @Override
    public void adminListResponse(ArrayList<Admin> adminList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        if (method == Request.Method.PUT)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

        btnTanggaLahirAdminPegawaiEditProfile.setText(dateString);
    }
}