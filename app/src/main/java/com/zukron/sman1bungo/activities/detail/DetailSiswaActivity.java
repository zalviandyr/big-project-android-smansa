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
import com.zukron.sman1bungo.activities.SiswaActivity;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.dao.SiswaDao;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailSiswaActivity extends AppCompatActivity implements View.OnClickListener, SiswaDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextView tvTitleSiswaDetail;
    private TextInputLayout inputLayoutNisnSiswaDetail, inputLayoutFirstNameSiswaDetail, inputLayoutLastNameSiswaDetail;
    private TextInputEditText inputNisnSiswaDetail, inputFirstNameSiswaDetail, inputLastNameSiswaDetail;
    private RadioButton rbLakiLakiSiswaDetail, rbPerempuanSiswaDetail;
    private Spinner spinKelasSiswaDetail;
    private Button btnTanggalLahirSiswaDetail, btnSaveSiswaDetail;
    private ArrayList<Kelas> kelasList;
    private ArrayList<String> idKelas, nama; // untuk kelas
    private String action;
    private SiswaDao siswaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        siswaDao = new SiswaDao(this, this);

        tvTitleSiswaDetail = findViewById(R.id.tv_title_siswa_detail);
        inputLayoutNisnSiswaDetail = findViewById(R.id.input_layout_nisn_siswa_detail);
        inputNisnSiswaDetail = findViewById(R.id.input_nisn_siswa_detail);
        inputLayoutFirstNameSiswaDetail = findViewById(R.id.input_layout_first_name_siswa_detail);
        inputFirstNameSiswaDetail = findViewById(R.id.input_first_name_siswa_detail);
        inputLayoutLastNameSiswaDetail = findViewById(R.id.input_layout_last_name_siswa_detail);
        inputLastNameSiswaDetail = findViewById(R.id.input_last_name_siswa_detail);

        btnTanggalLahirSiswaDetail = findViewById(R.id.btn_tanggal_lahir_siswa_detail);
        rbLakiLakiSiswaDetail = findViewById(R.id.rb_laki_laki_siswa_detail);
        rbPerempuanSiswaDetail = findViewById(R.id.rb_perempuan_siswa_detail);
        spinKelasSiswaDetail = findViewById(R.id.spin_kelas_siswa_detail);
        btnSaveSiswaDetail = findViewById(R.id.btn_save_siswa_detail);

        btnTanggalLahirSiswaDetail.setOnClickListener(this);
        btnSaveSiswaDetail.setOnClickListener(this);

        // set action and title
        action = getIntent().getStringExtra("action");
        kelasList = getIntent().getParcelableArrayListExtra("kelasList");

        setKelasSpinner();

        assert action != null;
        if (action.equals("edit")) {
            Siswa siswa = getIntent().getParcelableExtra("siswa");
            tvTitleSiswaDetail.setText(R.string.edit_siswa);

            // set edit text
            if (siswa != null) {
                inputNisnSiswaDetail.setEnabled(false);
                inputNisnSiswaDetail.setText(siswa.getNisn());
                inputFirstNameSiswaDetail.setText(siswa.getFirstName());
                inputLastNameSiswaDetail.setText(siswa.getLastName());
                btnTanggalLahirSiswaDetail.setText(siswa.getTanggalLahir().toString());

                // set radio button
                String jekel = siswa.getJekel();
                if (jekel.equals("Laki laki"))
                    rbLakiLakiSiswaDetail.setChecked(true);
                if (jekel.equals("Perempuan"))
                    rbPerempuanSiswaDetail.setChecked(true);

                setSelectedItemSpinner(siswa);
            }
        }
        if (action.equals("add")) {
            tvTitleSiswaDetail.setText(R.string.tambah_siswa);

            // set initial radio button jekel
            rbLakiLakiSiswaDetail.setChecked(true);
        }
    }

    private void setKelasSpinner() {
        // idKelas
        idKelas = new ArrayList<>();
        for (Kelas kelas : kelasList) {
            idKelas.add(kelas.getIdKelas());
        }

        // nama
        nama = new ArrayList<>();
        for (Kelas kelas : kelasList) {
            nama.add(kelas.getNama());
        }

        ArrayAdapter<String> kelasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nama);
        kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKelasSiswaDetail.setAdapter(kelasAdapter);
    }

    private void setSelectedItemSpinner(Siswa siswa) {
        spinKelasSiswaDetail.setSelection(idKelas.indexOf(siswa.getIdKelas()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tanggal_lahir_siswa_detail:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_save_siswa_detail:
                if (validateInput()) {
                    sendDataSiswa(action);
                    moveToSiswaActivity();
                }
                break;
        }
    }

    private void sendDataSiswa(String action) {
        String nisn = inputNisnSiswaDetail.getText().toString().trim();
        String firstName = inputFirstNameSiswaDetail.getText().toString().trim();
        String lastName = inputLastNameSiswaDetail.getText().toString().trim();
        String jekel = "";
        if (rbLakiLakiSiswaDetail.isChecked())
            jekel = rbLakiLakiSiswaDetail.getText().toString().trim();
        if (rbPerempuanSiswaDetail.isChecked())
            jekel = rbPerempuanSiswaDetail.getText().toString().trim();
        LocalDate tanggalLahir = LocalDate.parse(btnTanggalLahirSiswaDetail.getText().toString().trim());
        String idKelasData = idKelas.get(spinKelasSiswaDetail.getSelectedItemPosition());

        Siswa siswa = new Siswa(
                nisn, firstName, lastName, jekel, null,
                null, tanggalLahir, null, null,
                idKelasData, null
        );

        if (action.equals("add")) {
            siswaDao.postPartial(siswa);
        }

        if (action.equals("edit")) {
            siswaDao.putPartial(siswa);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = simpleDateFormat.format(calendar.getTime());

        btnTanggalLahirSiswaDetail.setText(dateString);
    }

    private void moveToSiswaActivity() {
        // membuat SiswaActivity baru dengan list data yang telah diupdate
        Intent intent = new Intent(DetailSiswaActivity.this, SiswaActivity.class);
        // menyelesaikan DetailSiswaActivty agar user tidak bisa kembali ke DetailSiswaActivty menggunakan tombol back
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // jika user tidak menekan tombol save atau melakukan perubahan,
        // maka buat SiswaActivity yang telah diselesaikan sebelumnya
        moveToSiswaActivity();
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNisnSiswaDetail.getText().toString().trim())) {
            inputLayoutNisnSiswaDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputFirstNameSiswaDetail.getText().toString().trim())) {
            inputLayoutFirstNameSiswaDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNameSiswaDetail.getText().toString().trim())) {
            inputLayoutLastNameSiswaDetail.setError(errorMsg);
            valid = false;
        }

        if (btnTanggalLahirSiswaDetail.getText().toString().equals("Tanggal")) {
            Toast.makeText(DetailSiswaActivity.this, "Pilih tanggal", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        // no need
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
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
