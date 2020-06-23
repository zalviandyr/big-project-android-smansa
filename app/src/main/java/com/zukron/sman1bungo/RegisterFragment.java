package com.zukron.sman1bungo;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.AdminDao;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.LoginRegisterDao;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Tools;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class RegisterFragment extends Fragment implements View.OnClickListener, LoginRegisterDao.onListener, AdminDao.onListener, GuruDao.onListener, PegawaiDao.onListener, SiswaDao.onListener {
    private TextInputLayout inputLayoutIdRegister, inputLayoutUsernameRegister, inputLayoutPasswordRegister;
    private TextInputEditText inputIdRegister, inputUsernameRegister, inputPasswordRegister;
    private Button btnRegister;
    private String status;
    private ProgressDialog progressDialog;
    private LoginRegisterDao loginRegisterDao;
    private AdminDao adminDao;
    private GuruDao guruDao;
    private PegawaiDao pegawaiDao;
    private SiswaDao siswaDao;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginRegisterDao = new LoginRegisterDao(getContext(), this);
        adminDao = new AdminDao(getContext(), this);
        guruDao = new GuruDao(getContext(), this);
        pegawaiDao = new PegawaiDao(getContext(), this);
        siswaDao = new SiswaDao(getContext(), this);

        assert getArguments() != null;
        status = getArguments().getString("status");

        inputLayoutIdRegister = view.findViewById(R.id.input_layout_id_register);
        inputIdRegister = view.findViewById(R.id.input_id_register);
        inputLayoutUsernameRegister = view.findViewById(R.id.input_layout_username_register);
        inputUsernameRegister = view.findViewById(R.id.input_username_register);
        inputLayoutPasswordRegister = view.findViewById(R.id.input_layout_password_register);
        inputPasswordRegister = view.findViewById(R.id.input_password_register);

        if (status.equals("Admin")) {
            inputIdRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            inputLayoutIdRegister.setHint("ID Admin");
        }

        if (status.equals("Guru")) {
            inputIdRegister.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputLayoutIdRegister.setHint("NIP");
        }
        if (status.equals("Pegawai")) {
            inputIdRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            inputLayoutIdRegister.setHint("ID Pegawai");
        }
        if (status.equals("Siswa")) {
            inputIdRegister.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputLayoutIdRegister.setHint("NISN");
        }

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (validateInput()) {
            registerAction();

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Proses membuat user");
            progressDialog.show();
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputIdRegister.getText().toString().trim())) {
            inputLayoutIdRegister.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputUsernameRegister.getText().toString().trim())) {
            inputLayoutUsernameRegister.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputPasswordRegister.getText().toString().trim())) {
            inputLayoutPasswordRegister.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    private void registerAction() {
        if (status.equals("Admin")) {
            String idAdmin = inputIdRegister.getText().toString().trim();

            adminDao.getId(idAdmin);
        }

        if (status.equals("Guru")) {
            String nip = inputIdRegister.getText().toString().trim();

            guruDao.getNip(nip);
        }

        if (status.equals("Pegawai")) {
            String idPegawai = inputIdRegister.getText().toString().trim();

            pegawaiDao.getId(idPegawai);
        }

        if (status.equals("Siswa")) {
            String nisn = inputIdRegister.getText().toString().trim();

            siswaDao.getNisn(nisn);
        }
    }

    private void moveToLoginActivity() {
        getActivity().onBackPressed();
    }

    @Override
    public void userResponse(User user) {
        // no need
    }

    @Override
    public void guruResponse(Guru guru) {
        String username = inputUsernameRegister.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordRegister.getText().toString().trim());
        String nip = guru.getNip();

        loginRegisterDao.postGuru(username, password, nip);
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        String username = inputUsernameRegister.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordRegister.getText().toString().trim());
        String idPegawai = pegawai.getIdPegawai();

        loginRegisterDao.postPegawai(username, password, idPegawai);
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        String username = inputUsernameRegister.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordRegister.getText().toString().trim());
        String nisn = siswa.getNisn();

        loginRegisterDao.postSiswa(username, password, nisn);
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
        // no need
    }

    @Override
    public void adminResponse(Admin admin) {
        String username = inputUsernameRegister.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordRegister.getText().toString().trim());
        String idAdmin = admin.getIdAdmin();

        loginRegisterDao.postAdmin(username, password, idAdmin);
    }

    @Override
    public void adminListResponse(ArrayList<Admin> adminList) {
        // no need
    }

    @Override
    public void defaultResponse(String response) {
        if (response.equals("Berhasil Register")) {
            progressDialog.dismiss();
            moveToLoginActivity();
            Toast.makeText(getContext(), "Berhasil Register", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        progressDialog.dismiss();

        if (status.equals("Admin")) {
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                inputLayoutIdRegister.setError("ID tidak ada");
                Toast.makeText(getContext(), "ID tidak ada", Toast.LENGTH_SHORT).show();
            }

            // jika id sudah terdaftar
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                inputLayoutIdRegister.setError("ID telah terdaftar");
                Toast.makeText(getContext(), "ID tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }

            // jika terjadi duplikasi username
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                inputLayoutUsernameRegister.setError("Username telah terdaftar");
                Toast.makeText(getContext(), "Username tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }
        }

        if (status.equals("Guru")) {
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                inputLayoutIdRegister.setError("NIP tidak ada");
                Toast.makeText(getContext(), "NIP tidak ada", Toast.LENGTH_SHORT).show();
            }

            // jika id sudah terdaftar
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                inputLayoutIdRegister.setError("NIP telah terdaftar");
                Toast.makeText(getContext(), "NIP tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }

            // jika terjadi duplikasi username
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                inputLayoutUsernameRegister.setError("Username telah terdaftar");
                Toast.makeText(getContext(), "Username tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }
        }

        if (status.equals("Pegawai")) {
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                inputLayoutIdRegister.setError("ID tidak ada");
                Toast.makeText(getContext(), "ID tidak ada", Toast.LENGTH_SHORT).show();
            }

            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                inputLayoutIdRegister.setError("ID telah terdaftar");
                Toast.makeText(getContext(), "ID tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }

            // jika terjadi duplikasi username
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                inputLayoutUsernameRegister.setError("Username telah terdaftar");
                Toast.makeText(getContext(), "Username tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }
        }

        if (status.equals("Siswa")) {
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                inputLayoutIdRegister.setError("NISN tidak ada");
                Toast.makeText(getContext(), "NISN tidak ada", Toast.LENGTH_SHORT).show();
            }

            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                inputLayoutIdRegister.setError("NISN telah terdaftar");
                Toast.makeText(getContext(), "NISN tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }

            // jika terjadi duplikasi username
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                inputLayoutUsernameRegister.setError("Username telah terdaftar");
                Toast.makeText(getContext(), "Username tersebut telah terdaftar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
