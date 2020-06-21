package com.zukron.sman1bungo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinStatusRegister;
    private Button btnOkRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinStatusRegister = findViewById(R.id.spin_status_register);
        setSpinnerStatusRegiter();

        btnOkRegister = findViewById(R.id.btn_ok_register);
        btnOkRegister.setOnClickListener(this);
    }

    private void setSpinnerStatusRegiter() {
        String[] status = {"Guru", "Pegawai", "Siswa"};
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStatusRegister.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

        RegisterFragment registerFragment = new RegisterFragment();
        bundle.putString("status", spinStatusRegister.getSelectedItem().toString().trim());
        registerFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_register_container, registerFragment, RegisterFragment.class.getSimpleName()).commit();
    }
}
