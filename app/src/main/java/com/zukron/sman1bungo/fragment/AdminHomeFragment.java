package com.zukron.sman1bungo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.GajiActivity;
import com.zukron.sman1bungo.activities.GuruActivity;
import com.zukron.sman1bungo.activities.KelasActivity;
import com.zukron.sman1bungo.activities.PegawaiActivity;
import com.zukron.sman1bungo.activities.PelajaranActivity;
import com.zukron.sman1bungo.activities.detail.DetailSekolahActivity;
import com.zukron.sman1bungo.activities.SiswaActivity;

public class AdminHomeFragment extends Fragment implements View.OnClickListener {
    private CardView cvSekolahHomeAdmin, cvGajiHomeAdmin, cvPelajaranHomeAdmin, cvKelasHomeAdmin, cvGuruHomeAdmin, cvPegawaiHomeAdmin, cvSiswaHomeAdmin;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvSekolahHomeAdmin = view.findViewById(R.id.cv_sekolah_admin_home);
        cvSekolahHomeAdmin.setOnClickListener(this);
        cvGajiHomeAdmin = view.findViewById(R.id.cv_gaji_admin_home);
        cvGajiHomeAdmin.setOnClickListener(this);
        cvPelajaranHomeAdmin = view.findViewById(R.id.cv_pelajaran_admin_home);
        cvPelajaranHomeAdmin.setOnClickListener(this);
        cvKelasHomeAdmin = view.findViewById(R.id.cv_kelas_admin_home);
        cvKelasHomeAdmin.setOnClickListener(this);
        cvGuruHomeAdmin = view.findViewById(R.id.cv_guru_admin_home);
        cvGuruHomeAdmin.setOnClickListener(this);
        cvPegawaiHomeAdmin = view.findViewById(R.id.cv_pegawai_admin_home);
        cvPegawaiHomeAdmin.setOnClickListener(this);
        cvSiswaHomeAdmin = view.findViewById(R.id.cv_siswa_admin_home);
        cvSiswaHomeAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_sekolah_admin_home:
                Intent sekolahIntent = new Intent(getContext(), DetailSekolahActivity.class);
                startActivity(sekolahIntent);
                break;
            case R.id.cv_gaji_admin_home:
                Intent gajiIntent = new Intent(getContext(), GajiActivity.class);
                startActivity(gajiIntent);
                break;
            case R.id.cv_pelajaran_admin_home:
                Intent pelajaranIntent = new Intent(getContext(), PelajaranActivity.class);
                startActivity(pelajaranIntent);
                break;
            case R.id.cv_kelas_admin_home:
                Intent kelasIntent = new Intent(getContext(), KelasActivity.class);
                startActivity(kelasIntent);
                break;
            case R.id.cv_guru_admin_home:
                Intent guruIntent = new Intent(getContext(), GuruActivity.class);
                startActivity(guruIntent);
                break;
            case R.id.cv_pegawai_admin_home:
                Intent pegawaiIntent = new Intent(getContext(), PegawaiActivity.class);
                startActivity(pegawaiIntent);
                break;
            case R.id.cv_siswa_admin_home:
                Intent siswaIntent = new Intent(getContext(), SiswaActivity.class);
                startActivity(siswaIntent);
                break;
        }
    }
}
