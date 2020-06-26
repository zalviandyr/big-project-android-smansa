package com.zukron.sman1bungo.activities.detail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.AdminActivity;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.model.dao.AdminDao;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailAdminActivity extends AppCompatActivity implements View.OnClickListener, AdminDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextView tvTitleAdminDetail;
    private TextInputLayout inputLayoutIdAdminDetail, inputLayoutFirstNameAdminDetail, inputLayoutLastNameAdminDetail;
    private TextInputEditText inputIdAdminDetail, inputFirstNameAdminDetail, inputLastNameAdminDetail;
    private RadioButton rbLakiLakiAdminDetail, rbPerempuanAdminDetail;
    private Spinner spinGajiAdminDetail;
    private Button btnTanggalLahirAdminDetail, btnSaveAdminDetail;

    private ArrayList<Gaji> gajiList;
    private ArrayList<String> idGaji, gajiPokok; // untuk gaji
    private String action;
    private AdminDao adminDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);

        adminDao = new AdminDao(this, this);

        tvTitleAdminDetail = findViewById(R.id.tv_title_admin_detail);
        inputLayoutIdAdminDetail = findViewById(R.id.input_layout_id_admin_detail);
        inputIdAdminDetail = findViewById(R.id.input_id_admin_detail);
        inputLayoutFirstNameAdminDetail = findViewById(R.id.input_layout_first_name_admin_detail);
        inputFirstNameAdminDetail = findViewById(R.id.input_first_name_admin_detail);
        inputLayoutLastNameAdminDetail = findViewById(R.id.input_layout_last_name_admin_detail);
        inputLastNameAdminDetail = findViewById(R.id.input_last_name_admin_detail);

        // set non editable input id admin
        inputIdAdminDetail.setEnabled(false);

        rbLakiLakiAdminDetail = findViewById(R.id.rb_laki_laki_admin_detail);
        rbPerempuanAdminDetail = findViewById(R.id.rb_perempuan_admin_detail);
        spinGajiAdminDetail = findViewById(R.id.spin_gaji_admin_detail);
        btnTanggalLahirAdminDetail = findViewById(R.id.btn_tanggal_lahir_admin_detail);
        btnSaveAdminDetail = findViewById(R.id.btn_save_admin_detail);

        btnTanggalLahirAdminDetail.setOnClickListener(this);
        btnSaveAdminDetail.setOnClickListener(this);

        // set action and title
        action = getIntent().getStringExtra("action");
        gajiList = getIntent().getParcelableArrayListExtra("gajiList");

        setGajiSpinner();

        assert action != null;
        if (action.equals("edit")) {
            Admin admin = getIntent().getParcelableExtra("admin");
            tvTitleAdminDetail.setText(R.string.edit_admin);

            // set edit text
            if (admin != null) {
                inputIdAdminDetail.setText(admin.getIdAdmin());
                inputFirstNameAdminDetail.setText(admin.getFirstName());
                inputLastNameAdminDetail.setText(admin.getLastName());
                btnTanggalLahirAdminDetail.setText(admin.getTanggalLahir().toString());

                // set radio button
                String jekel = admin.getJekel();
                if (jekel.equals("Laki laki"))
                    rbLakiLakiAdminDetail.setChecked(true);
                if (jekel.equals("Perempuan"))
                    rbPerempuanAdminDetail.setChecked(true);

                setSelectedItemSpinner(admin);
            }
        }
        if (action.equals("add")) {
            tvTitleAdminDetail.setText(R.string.tambah_admin);
            inputIdAdminDetail.setText("-");

            // set initial radio button jekel
            rbLakiLakiAdminDetail.setChecked(true);
        }
    }

    private void setGajiSpinner() {
        // idGaji
        idGaji = new ArrayList<>();
        for (Gaji gaji : gajiList) {
            idGaji.add(gaji.getIdGaji());
        }

        // gajiPokok
        gajiPokok = new ArrayList<>();
        for (Gaji gaji : gajiList) {
            gajiPokok.add("Rp. " + gaji.getGajiPokok());
        }

        ArrayAdapter<String> gajiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gajiPokok);
        gajiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGajiAdminDetail.setAdapter(gajiAdapter);
    }

    private void setSelectedItemSpinner(Admin admin) {
        spinGajiAdminDetail.setSelection(idGaji.indexOf(admin.getIdGaji()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tanggal_lahir_admin_detail:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_save_admin_detail:
                if (validateInput()) {
                    sendDataAdmin(action);
                    moveToAdminActivity();
                }
                break;
        }
    }

    private void sendDataAdmin(String action) {
        String idAdmin = inputIdAdminDetail.getText().toString().trim();
        String firstName = inputFirstNameAdminDetail.getText().toString().trim();
        String lastName = inputLastNameAdminDetail.getText().toString().trim();
        LocalDate tanggalLahir = LocalDate.parse(btnTanggalLahirAdminDetail.getText().toString().trim());
        String jekel = "";
        if (rbLakiLakiAdminDetail.isChecked())
            jekel = rbLakiLakiAdminDetail.getText().toString().trim();
        if (rbPerempuanAdminDetail.isChecked())
            jekel = rbPerempuanAdminDetail.getText().toString().trim();
        String idGajiData = idGaji.get(spinGajiAdminDetail.getSelectedItemPosition());


        if (action.equals("add")) {
            Admin admin = new Admin(
                    null, firstName, lastName, tanggalLahir,
                    null, jekel, idGajiData, null
            );

            adminDao.postPartial(admin);
        }

        if (action.equals("edit")) {
            Admin admin = new Admin(
                    idAdmin, firstName, lastName, tanggalLahir,
                    null, jekel, idGajiData, null
            );

            adminDao.putPartial(admin);
        }
    }

    private void moveToAdminActivity() {
        // membuat AdminActivity baru dengan list data yang telah diupdate
        Intent intent = new Intent(DetailAdminActivity.this, AdminActivity.class);
        // menyelesaikan DetailAdminActivty agar user tidak bisa kembali ke DetailAdminActivty menggunakan tombol back
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // jika user tidak menekan tombol save atau melakukan perubahan,
        // maka buat AdminActivity yang telah diselesaikan sebelumnya
        moveToAdminActivity();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = simpleDateFormat.format(calendar.getTime());

        btnTanggalLahirAdminDetail.setText(dateString);
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputFirstNameAdminDetail.getText().toString().trim())) {
            inputLayoutFirstNameAdminDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNameAdminDetail.getText().toString().trim())) {
            inputLayoutLastNameAdminDetail.setError(errorMsg);
            valid = false;
        }

        if (btnTanggalLahirAdminDetail.getText().toString().equals("Tanggal")) {
            Toast.makeText(DetailAdminActivity.this, "Pilih tanggal", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    @Override
    public void adminResponse(Admin admin) {
        // no need
    }

    @Override
    public void adminListResponse(ArrayList<Admin> adminList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        if (method == Request.Method.PUT || method == Request.Method.POST)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
