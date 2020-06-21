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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.model.dao.PegawaiDao;
import com.zukron.sman1bungo.util.Session;
import com.zukron.sman1bungo.util.api.PegawaiEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PegawaiHomeFragment extends Fragment implements PegawaiDao.onListener {
    private TextView tvNamaPegawaiHome, tvJabatanPegawaiHome;
    private User userSession = Session.getSession();
    private ProgressDialog progressDialog;
    private PegawaiDao pegawaiDao;

    public PegawaiHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pegawai_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pegawaiDao = new PegawaiDao(getContext(), this);

        tvNamaPegawaiHome = view.findViewById(R.id.tv_nama_pegawai_home);
        tvJabatanPegawaiHome = view.findViewById(R.id.tv_jabatan_pegawai_home);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses ambil data");
        progressDialog.show();

        retrieveDataPegawai();
    }

    private void retrieveDataPegawai() {
        String username = userSession.getUsername();

        pegawaiDao.getUsername(username);
    }

    @Override
    public void pegawaiResponse(Pegawai pegawai) {
        progressDialog.dismiss();

        String nama = pegawai.getFirstName() + " " + pegawai.getLastName();
        String jabatan = pegawai.getJabatan();

        tvNamaPegawaiHome.setText(nama);
        tvJabatanPegawaiHome.setText(jabatan);
    }

    @Override
    public void pegawaiListResponse(ArrayList<Pegawai> pegawaiList) {
        // no need
    }

    @Override
    public void defaultResponse(String response) {
        // no need
    }

    @Override
    public void errorResponse(VolleyError error) {
        // no need
    }
}
