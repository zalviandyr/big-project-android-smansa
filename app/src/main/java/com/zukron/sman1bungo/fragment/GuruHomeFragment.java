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
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.GuruDao;
import com.zukron.sman1bungo.model.dao.PelajaranDao;
import com.zukron.sman1bungo.util.Session;

import java.util.ArrayList;

public class GuruHomeFragment extends Fragment implements GuruDao.onListener, PelajaranDao.onListener {
    private TextView tvNamaGuruHome, tvNipGuruHome, tvMataPelajaranGuruHome, tvGolonganGuruHome;
    private ProgressDialog progressDialog;
    private ArrayList<Pelajaran> pelajaranList;
    private User userSession = Session.getSession();
    private GuruDao guruDao;
    private PelajaranDao pelajaranDao;

    public GuruHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guru_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guruDao = new GuruDao(getContext(), this);
        pelajaranDao = new PelajaranDao(getContext(), this);

        pelajaranList = new ArrayList<>();

        tvNamaGuruHome = view.findViewById(R.id.tv_nama_guru_home);
        tvNipGuruHome = view.findViewById(R.id.tv_nip_guru_home);
        tvMataPelajaranGuruHome = view.findViewById(R.id.tv_mata_pelajaran_guru_home);
        tvGolonganGuruHome = view.findViewById(R.id.tv_golongan_guru_home);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveGuruData();
        retrievePelajaranData();
    }

    private void retrieveGuruData() {
        String username = userSession.getUsername();
        guruDao.getUsername(username);
    }

    private void retrievePelajaranData() {
        pelajaranDao.getAll();
    }

    @Override
    public void guruResponse(Guru guru) {
        if (guruDao.isRequestFinished() && pelajaranDao.isRequestFinished()) {
            progressDialog.dismiss();

            String nama = guru.getFirstName() + " " + guru.getLastName();
            String nip = guru.getNip();
            String mataPelajaran = "";
            for (Pelajaran pelajaran : pelajaranList) {
                if (pelajaran.getIdPelajaran().equals(guru.getIdPelajaran())) {
                    mataPelajaran = pelajaran.getNama();
                }
            }
            String golongan = guru.getGolongan();

            tvNamaGuruHome.setText(nama);
            tvNipGuruHome.setText(nip);
            tvMataPelajaranGuruHome.setText(mataPelajaran);
            tvGolonganGuruHome.setText(golongan);
        } else {
            retrieveGuruData();
            retrievePelajaranData();
        }
    }

    @Override
    public void guruListResponse(ArrayList<Guru> guruList) {
        // no need
    }

    @Override
    public void messageResponse(int method, String message) {
        // no need
    }

    @Override
    public void pelajaranResponse(Pelajaran pelajaran) {
        // no need
    }

    @Override
    public void pelajaranListResponse(ArrayList<Pelajaran> pelajaranList) {
        this.pelajaranList = pelajaranList;
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
