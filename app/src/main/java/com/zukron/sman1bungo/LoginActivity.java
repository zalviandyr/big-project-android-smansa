package com.zukron.sman1bungo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.LoginRegisterDao;
import com.zukron.sman1bungo.util.Session;
import com.zukron.sman1bungo.util.Tools;

import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginRegisterDao.onListener {
    private TextInputLayout inputLayoutUsernameLogin, inputLayoutPasswordLogin;
    private TextInputEditText inputUsernameLogin, inputPasswordLogin;
    private Button btnLogin, btnCreateAnAccountLogin;
    private ProgressDialog progressDialog;
    private LoginRegisterDao loginRegisterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginRegisterDao = new LoginRegisterDao(this, this);

        inputLayoutUsernameLogin = findViewById(R.id.input_layout_username_login);
        inputUsernameLogin = findViewById(R.id.input_username_login);
        inputLayoutPasswordLogin = findViewById(R.id.input_layout_password_login);
        inputPasswordLogin = findViewById(R.id.input_password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnCreateAnAccountLogin = findViewById(R.id.btn_create_an_account_login);
        btnCreateAnAccountLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (validateInput()) {
                    loginAction();

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Proses login");
                    progressDialog.show();
                }
                break;
            case R.id.btn_create_an_account_login:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputUsernameLogin.getText().toString().trim())) {
            inputLayoutUsernameLogin.setError(errorMsg);
            valid = false;
        }

        if (TextUtils.isEmpty(inputPasswordLogin.getText().toString().trim())) {
            inputLayoutPasswordLogin.setError(errorMsg);
            valid = false;
        }
        return valid;
    }

    private void loginAction() {
        String username = inputUsernameLogin.getText().toString().trim();
        String password = Tools.toMd5(inputPasswordLogin.getText().toString().trim());

        loginRegisterDao.get(username, password);
    }

    private void moveToDashboardActivity() {
        // clear all previous activity and move to dashboard
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void userResponse(User user) {
        if (user != null) {
            progressDialog.dismiss();

            // set session
            Session.setSession(user);

            // pastikan session tidak null
            if (Session.getSession() != null) {
                moveToDashboardActivity();
            }
        }
    }

    @Override
    public void messageResponse(boolean error, int method, String message) {
        progressDialog.dismiss();

        // jika ada error
        if (method == Request.Method.GET)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        if (error) {
            inputLayoutUsernameLogin.setError("Masukkan username yang valid");
            inputLayoutPasswordLogin.setError("Masukkan password yang valid");
        }
    }

    @Override
    public void errorResponse(VolleyError error) {
        progressDialog.dismiss();
    }
}
