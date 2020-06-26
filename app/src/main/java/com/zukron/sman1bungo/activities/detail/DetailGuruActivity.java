package com.zukron.sman1bungo.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.GuruActivity;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.util.Tools;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailGuruActivity extends AppCompatActivity implements View.OnClickListener, GuruDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextView tvTitleGuruDetail;
    private TextInputLayout inputLayoutNipGuruDetail, inputLayoutFirstNameGuruDetail, inputLayoutLastNameGuruDetail;
    private TextInputEditText inputNipGuruDetail, inputFirstNameGuruDetail, inputLastNameGuruDetail;
    private RadioButton rbLakiLakiGuruDetail, rbPerempuanGuruDetail;
    private Spinner spinGolonganGuruDetail, spinPelajaranGuruDetail, spinGajiGuruDetail;
    private Button btnTanggalLahirGuruDetail, btnSaveGuruDetail;
    private ArrayList<Pelajaran> pelajaranList;
    private ArrayList<Gaji> gajiList;
    private ArrayList<String> idPelajaran, nama; // untuk pelajaran
    private ArrayList<String> idGaji, gajiPokok; // untuk gaji
    private ArrayList<String> golongan; // untuk golongan
    private String action;
    private GuruDao guruDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_guru);

        guruDao = new GuruDao(this, this);

        tvTitleGuruDetail = findViewById(R.id.tv_title_guru_detail);
        inputLayoutNipGuruDetail = findViewById(R.id.input_layout_nip_guru_detail);
        inputNipGuruDetail = findViewById(R.id.input_nip_guru_detail);
        inputLayoutFirstNameGuruDetail = findViewById(R.id.input_layout_first_name_guru_detail);
        inputFirstNameGuruDetail = findViewById(R.id.input_first_name_guru_detail);
        inputLayoutLastNameGuruDetail = findViewById(R.id.input_layout_last_name_guru_detail);
        inputLastNameGuruDetail = findViewById(R.id.input_last_name_guru_detail);

        btnTanggalLahirGuruDetail = findViewById(R.id.btn_tanggal_lahir_guru_detail);
        rbLakiLakiGuruDetail = findViewById(R.id.rb_laki_laki_guru_detail);
        rbPerempuanGuruDetail = findViewById(R.id.rb_perempuan_guru_detail);
        spinGolonganGuruDetail = findViewById(R.id.spin_golongan_guru_detail);
        spinPelajaranGuruDetail = findViewById(R.id.spin_pelajaran_guru_detail);
        spinGajiGuruDetail = findViewById(R.id.spin_gaji_guru_detail);
        btnSaveGuruDetail = findViewById(R.id.btn_save_guru_detail);

        btnTanggalLahirGuruDetail.setOnClickListener(this);
        btnSaveGuruDetail.setOnClickListener(this);

        // set action and title
        pelajaranList = getIntent().getParcelableArrayListExtra("pelajaranList");
        gajiList = getIntent().getParcelableArrayListExtra("gajiList");
        action = getIntent().getStringExtra("action");

        setPelajaranSpinner();
        setGolonganSpinner();
        setGajiSpinner();

        assert action != null;
        if (action.equals("edit")) {
            Guru guru = getIntent().getParcelableExtra("guru");
            tvTitleGuruDetail.setText(R.string.edit_guru);

            // set edit text
            if (guru != null) {
                inputNipGuruDetail.setEnabled(false);
                inputNipGuruDetail.setText(guru.getNip());
                inputFirstNameGuruDetail.setText(guru.getFirstName());
                inputLastNameGuruDetail.setText(guru.getLastName());
                btnTanggalLahirGuruDetail.setText(guru.getTanggalLahir().toString());

                // set radio button
                String jekel = guru.getJekel();
                if (jekel.equals("Laki laki"))
                    rbLakiLakiGuruDetail.setChecked(true);
                if (jekel.equals("Perempuan"))
                    rbPerempuanGuruDetail.setChecked(true);

                // set selected item on spinner
                setSelectedItemSpinner(guru);
            }
        }

        if (action.equals("add")) {
            tvTitleGuruDetail.setText(R.string.tambah_guru);

            // set initial radio button jekel
            rbLakiLakiGuruDetail.setChecked(true);
        }
    }

    private void setPelajaranSpinner() {
        // idPelajaran
        idPelajaran = new ArrayList<>();
        for (Pelajaran pelajaran : pelajaranList) {
            idPelajaran.add(pelajaran.getIdPelajaran());
        }

        // nama
        nama = new ArrayList<>();
        for (Pelajaran pelajaran : pelajaranList) {
            nama.add(pelajaran.getNama());
        }

        ArrayAdapter<String> pelajaranAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nama);
        pelajaranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPelajaranGuruDetail.setAdapter(pelajaranAdapter);
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
            gajiPokok.add(Tools.toIdr(gaji.getGajiPokok()).toString());
        }

        ArrayAdapter<String> gajiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gajiPokok);
        gajiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGajiGuruDetail.setAdapter(gajiAdapter);
    }

    private void setGolonganSpinner() {
        golongan = new ArrayList<>();
        golongan.add("III A");
        golongan.add("III B");
        golongan.add("III C");
        golongan.add("III D");
        golongan.add("IV A");
        golongan.add("IV B");
        golongan.add("IV C");
        golongan.add("IV D");
        golongan.add("IV E");

        ArrayAdapter<String> golonganAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, golongan);
        golonganAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGolonganGuruDetail.setAdapter(golonganAdapter);
    }

    /***
     * if action condition is "edit" method dieksekusi
     */
    private void setSelectedItemSpinner(Guru guru) {
        // golongan spinner
        spinGolonganGuruDetail.setSelection(golongan.indexOf(guru.getGolongan()));

        // pelajaran spinner
        spinPelajaranGuruDetail.setSelection(idPelajaran.indexOf(guru.getIdPelajaran()));

        // gaji spinner
        spinGajiGuruDetail.setSelection(idGaji.indexOf(guru.getIdGaji()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tanggal_lahir_guru_detail:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_save_guru_detail:
                if (validateInput()) {
                    sendDataGuru(action);
                }
                break;
        }
    }

    /***
     * send data to server using PARTIAL value
     */
    private void sendDataGuru(String action) {
        String nip = inputNipGuruDetail.getText().toString().trim();
        String firstName = inputFirstNameGuruDetail.getText().toString().trim();
        String lastName = inputLastNameGuruDetail.getText().toString().trim();
        String jekel = "";
        if (rbLakiLakiGuruDetail.isChecked())
            jekel = rbLakiLakiGuruDetail.getText().toString().trim();
        if (rbPerempuanGuruDetail.isChecked())
            jekel = rbPerempuanGuruDetail.getText().toString().trim();
        LocalDate tanggalLahir = LocalDate.parse(btnTanggalLahirGuruDetail.getText().toString().trim());
        String golongan = spinGolonganGuruDetail.getSelectedItem().toString().trim();
        String idPelajaranData = idPelajaran.get(spinPelajaranGuruDetail.getSelectedItemPosition());
        String idGajiData = idGaji.get(spinGajiGuruDetail.getSelectedItemPosition());

        Guru guru = new Guru(
                nip, firstName, lastName, null, null,
                jekel, tanggalLahir, null, null,
                golongan, idPelajaranData, null, idGajiData
        );

        if (action.equals("add")) {
            guruDao.postPartial(guru);
        }

        if (action.equals("edit")) {
            guruDao.putPartial(guru);
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

        btnTanggalLahirGuruDetail.setText(dateString);
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNipGuruDetail.getText().toString().trim())) {
            inputLayoutNipGuruDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputFirstNameGuruDetail.getText().toString().trim())) {
            inputLayoutFirstNameGuruDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNameGuruDetail.getText().toString().trim())) {
            inputLayoutLastNameGuruDetail.setError(errorMsg);
            valid = false;
        }

        if (btnTanggalLahirGuruDetail.getText().toString().equals("Tanggal")) {
            Toast.makeText(DetailGuruActivity.this, "Pilih tanggal", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    private void moveToGuruActivity() {
        // membuat GuruActivity baru dengan list data yang telah diupdate
        Intent intent = new Intent(DetailGuruActivity.this, GuruActivity.class);
        // menyelesaikan DetailGuruActivty agar user tidak bisa kembali ke DetailGuruActivty menggunakan tombol back
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // jika user tidak menekan tombol save atau melakukan perubahan,
        // maka buat GuruActivity yang telah diselesaikan sebelumnya
        moveToGuruActivity();
    }

    @Override
    public void guruResponse(Guru guru) {
        // no need
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        if (method == Request.Method.PUT || method == Request.Method.POST) {
            moveToGuruActivity();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
