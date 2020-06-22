package com.zukron.sman1bungo.activities.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.fragment.tools.DatePickerFragment;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Session;
import com.zukron.sman1bungo.util.Tools;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/***
 * edit born date hanya untuk guru dan siswa
 */
public class EditBornDateActivity extends AppCompatActivity implements View.OnClickListener, GuruDao.onListener, SiswaDao.onListener, DatePickerDialog.OnDateSetListener {
    private TextInputLayout inputLayoutKotaLahirEditBornDate;
    private TextInputEditText inputKotaLahirEditBornDate;
    private Spinner spinProvinsiLahirEditBornDate;
    private Button btnTanggalLahirEditBornDate, btnOkEditBornDate;
    private GuruDao guruDao;
    private Guru guru;
    private SiswaDao siswaDao;
    private Siswa siswa;
    private ArrayAdapter<String> arrayAdapter;
    private ProgressDialog progressDialog;
    private User userSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_born_date);

        guruDao = new GuruDao(this, this);
        siswaDao = new SiswaDao(this, this);

        spinProvinsiLahirEditBornDate = findViewById(R.id.spin_provinsi_lahir_edit_born_date);
        inputLayoutKotaLahirEditBornDate = findViewById(R.id.input_layout_kota_lahir_edit_born_date);
        inputKotaLahirEditBornDate = findViewById(R.id.input_kota_lahir_edit_born_date);
        btnTanggalLahirEditBornDate = findViewById(R.id.btn_tanggal_lahir_edit_born_date);
        btnTanggalLahirEditBornDate.setOnClickListener(this);
        btnOkEditBornDate = findViewById(R.id.btn_ok_edit_born_date);
        btnOkEditBornDate.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        setSpinnerProvinsi();
        retrieveData();
    }

    private void setSpinnerProvinsi() {
        ArrayList<String> provinsi = Tools.provinsiList();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinsi);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProvinsiLahirEditBornDate.setAdapter(arrayAdapter);
    }

    private void retrieveData() {
        String username = userSession.getUsername();
        if (userSession.getLevel().equals("Guru"))
            guruDao.getUsername(username);
        if (userSession.getLevel().equals("Siswa"))
            siswaDao.getUsername(username);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tanggal_lahir_edit_born_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.btn_ok_edit_born_date:
                if (validateInput()) {
                    sendData();
                }
                break;
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputKotaLahirEditBornDate.getText().toString().trim())) {
            inputLayoutKotaLahirEditBornDate.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    private void sendData() {
        LocalDate tanggalLahir = LocalDate.parse(btnTanggalLahirEditBornDate.getText().toString().trim());
        String provinsiLahir = spinProvinsiLahirEditBornDate.getSelectedItem().toString().trim();
        String kotaLahir = inputKotaLahirEditBornDate.getText().toString().trim();

        if (userSession.getLevel().equals("Guru")) {
            Guru guruData = new Guru(
                    guru.getNip(), guru.getFirstName(), guru.getLastName(), guru.getEmail(),
                    guru.getNoHp(), guru.getJekel(), tanggalLahir,
                    provinsiLahir, kotaLahir, guru.getGolongan(),
                    guru.getIdPelajaran(), guru.getUsername(), guru.getIdGaji()
            );

            guruDao.putFull(guruData);
        }

        if (userSession.getLevel().equals("Siswa")) {
            Siswa siswaData = new Siswa(
                    siswa.getNisn(), siswa.getFirstName(), siswa.getLastName(), siswa.getJekel(), siswa.getEmail(),
                    siswa.getNoHp(), tanggalLahir, kotaLahir,
                    provinsiLahir, siswa.getIdKelas(), siswa.getUsername()
            );

            siswaDao.putFull(siswaData);
        }
    }

    @Override
    public void guruResponse(Guru guru) {
        if (userSession.getLevel().equals("Guru")) {
            progressDialog.dismiss();
            this.guru = guru;

            String tanggalLahir = guru.getTanggalLahir().toString();
            String provinsiLahir = guru.getProvinsiLahir();
            String kotaLahir = (guru.getKotaLahir().equals("null")) ? "" : guru.getKotaLahir();

            btnTanggalLahirEditBornDate.setText(tanggalLahir);
            spinProvinsiLahirEditBornDate.setSelection(arrayAdapter.getPosition(provinsiLahir));
            inputKotaLahirEditBornDate.setText(kotaLahir);
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

            String tanggalLahir = siswa.getTanggalLahir().toString();
            String provinsiLahir = siswa.getProvinsiLahir();
            String kotaLahir = (siswa.getKotaLahir().equals("null")) ? "" : siswa.getKotaLahir();

            btnTanggalLahirEditBornDate.setText(tanggalLahir);
            spinProvinsiLahirEditBornDate.setSelection(arrayAdapter.getPosition(provinsiLahir));
            inputKotaLahirEditBornDate.setText(kotaLahir);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = simpleDateFormat.format(calendar.getTime());

        btnTanggalLahirEditBornDate.setText(dateString);
    }
}