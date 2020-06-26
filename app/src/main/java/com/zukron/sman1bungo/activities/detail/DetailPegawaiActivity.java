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
import com.zukron.sman1bungo.activities.PegawaiActivity;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.util.Tools;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailPegawaiActivity extends AppCompatActivity implements View.OnClickListener, PegawaiDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextView tvTitlePegawaiDetail;
    private TextInputLayout inputLayoutIdPegawaiDetail, inputLayoutFirstNamePegawaiDetail, inputLayoutLastNamePegawaiDetail;
    private TextInputEditText inputIdPegawaiDetail, inputFirstNamePegawaiDetail, inputLastNamePegawaiDetail;
    private RadioButton rbLakiLakiPegawaiDetail, rbPerempuanPegawaiDetail;
    private Spinner spinJabatanPegawaiDetail, spinGajiPegawaiDetail;
    private Button btnTanggalLahirPegawaiDetail, btnSavePegawaiDetail;

    private ArrayList<Gaji> gajiList;
    private ArrayList<String> idGaji, gajiPokok; // untuk gaji
    private ArrayList<String> jabatan;
    private String action;
    private PegawaiDao pegawaiDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pegawai);

        pegawaiDao = new PegawaiDao(this, this);

        tvTitlePegawaiDetail = findViewById(R.id.tv_title_pegawai_detail);
        inputLayoutIdPegawaiDetail = findViewById(R.id.input_layout_id_pegawai_detail);
        inputIdPegawaiDetail = findViewById(R.id.input_id_pegawai_detail);
        inputLayoutFirstNamePegawaiDetail = findViewById(R.id.input_layout_first_name_pegawai_detail);
        inputFirstNamePegawaiDetail = findViewById(R.id.input_first_name_pegawai_detail);
        inputLayoutLastNamePegawaiDetail = findViewById(R.id.input_layout_last_name_pegawai_detail);
        inputLastNamePegawaiDetail = findViewById(R.id.input_last_name_pegawai_detail);

        // set non editable input id pegawai
        inputIdPegawaiDetail.setEnabled(false);

        rbLakiLakiPegawaiDetail = findViewById(R.id.rb_laki_laki_pegawai_detail);
        rbPerempuanPegawaiDetail = findViewById(R.id.rb_perempuan_pegawai_detail);
        spinJabatanPegawaiDetail = findViewById(R.id.spin_jabatan_pegawai_detail);
        spinGajiPegawaiDetail = findViewById(R.id.spin_gaji_pegawai_detail);
        btnTanggalLahirPegawaiDetail = findViewById(R.id.btn_tanggal_lahir_pegawai_detail);
        btnSavePegawaiDetail = findViewById(R.id.btn_save_pegawai_detail);

        btnTanggalLahirPegawaiDetail.setOnClickListener(this);
        btnSavePegawaiDetail.setOnClickListener(this);

        // set action and title
        action = getIntent().getStringExtra("action");
        gajiList = getIntent().getParcelableArrayListExtra("gajiList");

        setGajiSpinner();
        setDataJabatanSpinner();

        assert action != null;
        if (action.equals("edit")) {
            Pegawai pegawai = getIntent().getParcelableExtra("pegawai");
            tvTitlePegawaiDetail.setText(R.string.edit_pegawai);

            // set edit text
            if (pegawai != null) {
                inputIdPegawaiDetail.setText(pegawai.getIdPegawai());
                inputFirstNamePegawaiDetail.setText(pegawai.getFirstName());
                inputLastNamePegawaiDetail.setText(pegawai.getLastName());
                btnTanggalLahirPegawaiDetail.setText(pegawai.getTanggalLahir().toString());

                // set radio button
                String jekel = pegawai.getJekel();
                if (jekel.equals("Laki laki"))
                    rbLakiLakiPegawaiDetail.setChecked(true);
                if (jekel.equals("Perempuan"))
                    rbPerempuanPegawaiDetail.setChecked(true);

                setSelectedItemSpinner(pegawai);
            }
        }
        if (action.equals("add")) {
            tvTitlePegawaiDetail.setText(R.string.tambah_pegawai);
            inputIdPegawaiDetail.setText("-");

            // set initial radio button jekel
            rbLakiLakiPegawaiDetail.setChecked(true);
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
            gajiPokok.add(Tools.toIdr(gaji.getGajiPokok()).toString());
        }

        ArrayAdapter<String> gajiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gajiPokok);
        gajiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGajiPegawaiDetail.setAdapter(gajiAdapter);
    }

    private void setDataJabatanSpinner() {
        jabatan = new ArrayList<>();
        jabatan.add("Satpam");
        jabatan.add("Tukang Bersih");
        jabatan.add("Penjaga Kantin");
        jabatan.add("Tukang Parkir");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jabatan);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJabatanPegawaiDetail.setAdapter(arrayAdapter);
    }

    private void setSelectedItemSpinner(Pegawai pegawai) {
        spinGajiPegawaiDetail.setSelection(idGaji.indexOf(pegawai.getIdGaji()));
        spinJabatanPegawaiDetail.setSelection(jabatan.indexOf(pegawai.getJabatan()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tanggal_lahir_pegawai_detail:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_save_pegawai_detail:
                if (validateInput()) {
                    sendDataPegawai(action);
                }
                break;
        }
    }

    private void sendDataPegawai(String action) {
        String idPegawai = inputIdPegawaiDetail.getText().toString().trim();
        String firstName = inputFirstNamePegawaiDetail.getText().toString().trim();
        String lastName = inputLastNamePegawaiDetail.getText().toString().trim();
        LocalDate tanggalLahir = LocalDate.parse(btnTanggalLahirPegawaiDetail.getText().toString().trim());
        String jekel = "";
        if (rbLakiLakiPegawaiDetail.isChecked())
            jekel = rbLakiLakiPegawaiDetail.getText().toString().trim();
        if (rbPerempuanPegawaiDetail.isChecked())
            jekel = rbPerempuanPegawaiDetail.getText().toString().trim();
        String jabatan = spinJabatanPegawaiDetail.getSelectedItem().toString().trim();
        String idGajiData = idGaji.get(spinGajiPegawaiDetail.getSelectedItemPosition());


        if (action.equals("add")) {
            Pegawai pegawai = new Pegawai(
                    null, firstName, lastName, tanggalLahir,
                    null, jekel, jabatan, idGajiData, null
            );

            pegawaiDao.postPartial(pegawai);
        }

        if (action.equals("edit")) {
            Pegawai pegawai = new Pegawai(
                    idPegawai, firstName, lastName, tanggalLahir,
                    null, jekel, jabatan, idGajiData, null
            );

            pegawaiDao.putPartial(pegawai);
        }
    }

    private void moveToPegawaiActivity() {
        // membuat PegawaiActivity baru dengan list data yang telah diupdate
        Intent intent = new Intent(DetailPegawaiActivity.this, PegawaiActivity.class);
        // menyelesaikan DetailPegawaiActivty agar user tidak bisa kembali ke DetailPegawaiActivty menggunakan tombol back
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // jika user tidak menekan tombol save atau melakukan perubahan,
        // maka buat PegawaiActivity yang telah diselesaikan sebelumnya
        moveToPegawaiActivity();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = simpleDateFormat.format(calendar.getTime());

        btnTanggalLahirPegawaiDetail.setText(dateString);
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputFirstNamePegawaiDetail.getText().toString().trim())) {
            inputLayoutFirstNamePegawaiDetail.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputLastNamePegawaiDetail.getText().toString().trim())) {
            inputLayoutLastNamePegawaiDetail.setError(errorMsg);
            valid = false;
        }

        if (btnTanggalLahirPegawaiDetail.getText().toString().equals("Tanggal")) {
            Toast.makeText(DetailPegawaiActivity.this, "Pilih tanggal", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        // no need
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        if (method == Request.Method.PUT || method == Request.Method.POST) {
            moveToPegawaiActivity();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
