package com.zukron.sman1bungo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zukron.sman1bungo.LoginActivity;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.about.AboutAppActivity;
import com.zukron.sman1bungo.activities.about.AboutSchoolActivity;
import com.zukron.sman1bungo.activities.edit.EditBornDateActivity;
import com.zukron.sman1bungo.activities.edit.EditEmailAndNoHpActivity;
import com.zukron.sman1bungo.activities.edit.EditNoHpActivity;
import com.zukron.sman1bungo.activities.edit.EditProfileGuruSiswaActivity;
import com.zukron.sman1bungo.activities.edit.EditProfileAdminPegawaiActivity;
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.AdminDao;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements View.OnClickListener, AdminDao.onListener, GuruDao.onListener, PegawaiDao.onListener, SiswaDao.onListener {
    private TextView tvNamaProfile, tvIdProfile, tvUsernameProfile;
    private Button btnEditProfile, btnEditBornDateProfile, btnEditNoHpProfile, btnEditEmailAndNoHpProfile, btnAboutSchoolProfile, btnAboutAppProfile, btnLogOutProfile;
    private User userSession = Session.getSession();
    private AdminDao adminDao;
    private GuruDao guruDao;
    private PegawaiDao pegawaiDao;
    private SiswaDao siswaDao;
    private ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminDao = new AdminDao(getContext(), this);
        guruDao = new GuruDao(getContext(), this);
        pegawaiDao = new PegawaiDao(getContext(), this);
        siswaDao = new SiswaDao(getContext(), this);

        tvNamaProfile = view.findViewById(R.id.tv_nama_profile);
        tvIdProfile = view.findViewById(R.id.tv_id_profile);
        tvUsernameProfile = view.findViewById(R.id.tv_username_profile);

        FrameLayout flButtonMenuProfile = view.findViewById(R.id.fl_button_menu_profile);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View viewButton = null;

        // separate button menu
        if (userSession.getLevel().equals("Guru") || userSession.getLevel().equals("Siswa")) {
            viewButton = layoutInflater.inflate(R.layout.button_menu_profile1, null);

            btnEditProfile = viewButton.findViewById(R.id.btn_edit_profile);
            btnEditProfile.setOnClickListener(this);
            btnEditBornDateProfile = viewButton.findViewById(R.id.btn_edit_born_date_profile);
            btnEditBornDateProfile.setOnClickListener(this);
            btnEditEmailAndNoHpProfile = viewButton.findViewById(R.id.btn_edit_email_and_no_hp_profile);
            btnEditEmailAndNoHpProfile.setOnClickListener(this);
            btnAboutSchoolProfile = viewButton.findViewById(R.id.btn_about_school_profile);
            btnAboutSchoolProfile.setOnClickListener(this);
            btnAboutAppProfile = viewButton.findViewById(R.id.btn_about_app_profile);
            btnAboutAppProfile.setOnClickListener(this);
            btnLogOutProfile = viewButton.findViewById(R.id.btn_log_out_profile);
            btnLogOutProfile.setOnClickListener(this);
        }

        if (userSession.getLevel().equals("Pegawai") || userSession.getLevel().equals("Admin")) {
            viewButton = layoutInflater.inflate(R.layout.button_menu_profile2, null);

            btnEditProfile = viewButton.findViewById(R.id.btn_edit_profile);
            btnEditProfile.setOnClickListener(this);
            btnEditNoHpProfile = viewButton.findViewById(R.id.btn_edit_no_hp_profile);
            btnEditNoHpProfile.setOnClickListener(this);
            btnAboutSchoolProfile = viewButton.findViewById(R.id.btn_about_school_profile);
            btnAboutSchoolProfile.setOnClickListener(this);
            btnAboutAppProfile = viewButton.findViewById(R.id.btn_about_app_profile);
            btnAboutAppProfile.setOnClickListener(this);
            btnLogOutProfile = viewButton.findViewById(R.id.btn_log_out_profile);
            btnLogOutProfile.setOnClickListener(this);
        }

        flButtonMenuProfile.addView(viewButton);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveData();

    }

    private void retrieveData() {
        if (userSession.getLevel().equals("Guru")) {
            guruDao.getUsername(userSession.getUsername());
        }

        if (userSession.getLevel().equals("Pegawai")) {
            pegawaiDao.getUsername(userSession.getUsername());
        }

        if (userSession.getLevel().equals("Siswa")) {
            siswaDao.getUsername(userSession.getUsername());
        }

        if (userSession.getLevel().equals("Admin")) {
            adminDao.getUsername(userSession.getUsername());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_profile:
                if (userSession.getLevel().equals("Guru") || userSession.getLevel().equals("Siswa")) {
                    Intent intent = new Intent(getContext(), EditProfileGuruSiswaActivity.class);
                    startActivity(intent);
                }

                if (userSession.getLevel().equals("Pegawai") || userSession.getLevel().equals("Admin")) {
                    Intent intent = new Intent(getContext(), EditProfileAdminPegawaiActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.btn_edit_born_date_profile:
                if (userSession.getLevel().equals("Guru") || userSession.getLevel().equals("Siswa")) {
                    Intent intent = new Intent(getContext(), EditBornDateActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.btn_edit_no_hp_profile:
                if (userSession.getLevel().equals("Pegawai") || userSession.getLevel().equals("Admin")) {
                    Intent intent = new Intent(getContext(), EditNoHpActivity.class);
                    startActivity(intent);
                }
            case R.id.btn_edit_email_and_no_hp_profile:
                if (userSession.getLevel().equals("Guru") || userSession.getLevel().equals("Siswa")) {
                    Intent intent = new Intent(getContext(), EditEmailAndNoHpActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.btn_about_school_profile:
                Intent intentAboutSchool = new Intent(getContext(), AboutSchoolActivity.class);
                startActivity(intentAboutSchool);
                break;

            case R.id.btn_about_app_profile:
                Intent intentAboutApp = new Intent(getContext(), AboutAppActivity.class);
                startActivity(intentAboutApp);
                break;

            case R.id.btn_log_out_profile:
                Intent intentLogOut = new Intent(getContext(), LoginActivity.class);
                intentLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogOut);
        }
    }

    @Override
    public void guruResponse(Guru guru) {
        progressDialog.dismiss();
        if (userSession.getLevel().equals("Guru")) {
            String nama = guru.getFirstName() + " " + guru.getLastName();
            String nip = guru.getNip();
            String username = userSession.getUsername();

            tvNamaProfile.setText(nama);
            tvIdProfile.setText(nip);
            tvUsernameProfile.setText(username);
        }
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        progressDialog.dismiss();
        if (userSession.getLevel().equals("Pegawai")) {
            String nama = pegawai.getFirstName() + " " + pegawai.getLastName();
            String idPegawai = pegawai.getIdPegawai();
            String username = userSession.getUsername();

            tvNamaProfile.setText(nama);
            tvIdProfile.setText(idPegawai);
            tvUsernameProfile.setText(username);
        }
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        progressDialog.dismiss();
        if (userSession.getLevel().equals("Siswa")) {
            String nama = siswa.getFirstName() + " " + siswa.getLastName();
            String nisn = siswa.getNisn();
            String username = userSession.getUsername();

            tvNamaProfile.setText(nama);
            tvIdProfile.setText(nisn);
            tvUsernameProfile.setText(username);
        }
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
        // no need
    }

    @Override
    public void adminResponse(Admin admin) {
        progressDialog.dismiss();
        if (userSession.getLevel().equals("Admin")) {
            String nama = admin.getFirstName() + " " + admin.getLastName();
            String nisn = admin.getIdAdmin();
            String username = userSession.getUsername();

            tvNamaProfile.setText(nama);
            tvIdProfile.setText(nisn);
            tvUsernameProfile.setText(username);
        }
    }

    @Override
    public void adminListResponse(ArrayList<Admin> adminList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        // no need
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
