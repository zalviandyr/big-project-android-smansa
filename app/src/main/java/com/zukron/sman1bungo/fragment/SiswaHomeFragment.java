package com.zukron.sman1bungo.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.KelasDao;
import com.zukron.sman1bungo.model.dao.SiswaDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

public class SiswaHomeFragment extends Fragment implements SiswaDao.onListener, KelasDao.onListener {
    private TextView tvNamaSiswaHome, tvNisnSiswaHome, tvKelasSiswaHome;
    private ProgressDialog progressDialog;
    private ArrayList<Kelas> kelasList;
    private User userSession = Session.getSession();
    private SiswaDao siswaDao;
    private KelasDao kelasDao;

    public SiswaHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_siswa_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        siswaDao = new SiswaDao(getContext(), this);
        kelasDao = new KelasDao(getContext(), this);

        tvNamaSiswaHome = view.findViewById(R.id.tv_nama_siswa_home);
        tvNisnSiswaHome = view.findViewById(R.id.tv_nisn_siswa_home);
        tvKelasSiswaHome = view.findViewById(R.id.tv_kelas_siswa_home);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveSiswaData();
        retrieveKelasData();
    }

    private void retrieveSiswaData() {
        String username = userSession.getUsername();

        siswaDao.getUsername(username);
    }

    private void retrieveKelasData() {
        kelasDao.getAll();
    }

    @Override
    public void kelasResponse(Kelas kelas) {
        // no need
    }

    @Override
    public void kelasListResponse(ArrayList<Kelas> kelasList) {
        this.kelasList = kelasList;
    }

    @Override
    public void siswaResponse(Siswa siswa) {
        if (siswaDao.isRequestFinished() && kelasDao.isRequestFinished()) {
            progressDialog.dismiss();

            String nama = siswa.getFirstName() + " " + siswa.getLastName();
            String nisn = siswa.getNisn();
            String kelas = "";
            for (Kelas kelasData : kelasList) {
                if (kelasData.getIdKelas().equals(siswa.getIdKelas())) {
                    kelas = kelasData.getNama();
                }
            }

            tvNamaSiswaHome.setText(nama);
            tvNisnSiswaHome.setText(nisn);
            tvKelasSiswaHome.setText(kelas);
        } else {
            retrieveSiswaData();
            retrieveKelasData();
        }
    }

    @Override
    public void siswaListResponse(ArrayList<Siswa> siswaList) {
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
