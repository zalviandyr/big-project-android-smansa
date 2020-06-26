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

import com.android.volley.Request;
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

public class RegisterFragment extends Fragment implements View.OnClickListener, LoginRegisterDao.onListener {
    private TextInputLayout inputLayoutIdRegister, inputLayoutUsernameRegister, inputLayoutPasswordRegister;
    private TextInputEditText inputIdRegister, inputUsernameRegister, inputPasswordRegister;
    private Button btnRegister;
    private String status;
    private ProgressDialog progressDialog;
    private LoginRegisterDao loginRegisterDao;

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
        String id = inputIdRegister.getText().toString().trim();
        String username = inputUsernameRegister.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordRegister.getText().toString().trim());

        if (status.equals("Admin"))
            loginRegisterDao.postAdmin(username, password, id);

        if (status.equals("Guru"))
            loginRegisterDao.postGuru(username, password, id);

        if (status.equals("Pegawai"))
            loginRegisterDao.postPegawai(username, password, id);

        if (status.equals("Siswa"))
            loginRegisterDao.postSiswa(username, password, id);
    }

    private void moveToLoginActivity() {
        getActivity().onBackPressed();
    }

    @Override
    public void userResponse(User user) {
        // no need
    }

    @Override
    public void messageResponse(boolean error, int method, String message) {
        progressDialog.dismiss();

        if (error) {
            String errorMsg = "Gagal Registrasi";
            inputLayoutIdRegister.setError(errorMsg);
            inputLayoutUsernameRegister.setError(errorMsg);
            inputLayoutPasswordRegister.setError(errorMsg);
        } else {
            moveToLoginActivity();
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResponse(VolleyError error) {
        progressDialog.dismiss();
        error.printStackTrace();
    }
}
