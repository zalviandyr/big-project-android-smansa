package com.zukron.sman1bungo.activities.about;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zukron.sman1bungo.R;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOkAboutApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        btnOkAboutApp = findViewById(R.id.btn_ok_about_app);
        btnOkAboutApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}