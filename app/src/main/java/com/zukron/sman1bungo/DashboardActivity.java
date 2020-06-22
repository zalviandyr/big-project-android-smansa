package com.zukron.sman1bungo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zukron.sman1bungo.fragment.AdminHomeFragment;
import com.zukron.sman1bungo.fragment.GuruHomeFragment;
import com.zukron.sman1bungo.fragment.JadwalPelajaranFragment;
import com.zukron.sman1bungo.fragment.PegawaiHomeFragment;
import com.zukron.sman1bungo.fragment.ProfileFragment;
import com.zukron.sman1bungo.fragment.SiswaHomeFragment;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.util.Session;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private User userSession = Session.getSession();
    private BottomNavigationView btnvFooterButton;

    // TODO Clean Code
    // TODO Admin, Pegawai, Guru dan siswa fragment dalam satu DashboardActivity
    // TODO validate input
    // TODO make a class to make a simple request to server
    // TODO buat custom id diserver, jadi di android tidak ada custom id
    // TODO add check connection internet

    // TODO add this one to send twice request to server when connection is low
    // jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
    //                       0,
    //                       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    //                       DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // inflate footer_button
        FrameLayout frameLayout = findViewById(R.id.fl_footer_button);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = null;

        // separate part of user access
        String level = userSession.getLevel();

        if (level.equals("Administrator")) {
            AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
            fragmentTransaction.add(R.id.fl_main_container, adminHomeFragment, AdminHomeFragment.class.getSimpleName()).commit();
        }

        if (level.equals("Pegawai")) {
            PegawaiHomeFragment pegawaiHomeFragment = new PegawaiHomeFragment();
            fragmentTransaction.add(R.id.fl_main_container, pegawaiHomeFragment, PegawaiHomeFragment.class.getSimpleName()).commit();
        }

        if (level.equals("Guru")) {
            GuruHomeFragment guruHomeFragment = new GuruHomeFragment();
            fragmentTransaction.add(R.id.fl_main_container, guruHomeFragment, GuruHomeFragment.class.getSimpleName()).commit();
        }

        if (level.equals("Siswa")) {
            SiswaHomeFragment siswaHomeFragment = new SiswaHomeFragment();
            fragmentTransaction.add(R.id.fl_main_container, siswaHomeFragment, SiswaHomeFragment.class.getSimpleName()).commit();
        }

        // button group
        view = layoutInflater.inflate(R.layout.footer_button, null);
        btnvFooterButton = view.findViewById(R.id.bnv_footer_button);
        btnvFooterButton.setOnNavigationItemSelectedListener(this);
        btnvFooterButton.getMenu().findItem(R.id.home_item).setChecked(true);

        frameLayout.addView(view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // check selected item
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.home_item:
                String level = userSession.getLevel();
                if (level.equals("Administrator")) {
                    AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
                    fragmentTransaction.replace(R.id.fl_main_container, adminHomeFragment, AdminHomeFragment.class.getSimpleName()).commit();
                }

                if (level.equals("Pegawai")) {
                    PegawaiHomeFragment pegawaiHomeFragment = new PegawaiHomeFragment();
                    fragmentTransaction.replace(R.id.fl_main_container, pegawaiHomeFragment, PegawaiHomeFragment.class.getSimpleName()).commit();
                }

                if (level.equals("Guru")) {
                    GuruHomeFragment guruHomeFragment = new GuruHomeFragment();
                    fragmentTransaction.replace(R.id.fl_main_container, guruHomeFragment, GuruHomeFragment.class.getSimpleName()).commit();
                }

                if (level.equals("Siswa")) {
                    SiswaHomeFragment siswaHomeFragment = new SiswaHomeFragment();
                    fragmentTransaction.replace(R.id.fl_main_container, siswaHomeFragment, SiswaHomeFragment.class.getSimpleName()).commit();
                }

                break;

            case R.id.profile_item:
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.fl_main_container, profileFragment, ProfileFragment.class.getSimpleName()).commit();
                break;
        }
        return true;
    }
}