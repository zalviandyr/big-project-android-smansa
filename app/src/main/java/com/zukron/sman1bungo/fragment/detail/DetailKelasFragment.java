package com.zukron.sman1bungo.fragment.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.KelasActivity;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.model.dao.KelasDao;
import com.zukron.sman1bungo.util.Tools;
import com.zukron.sman1bungo.util.api.KelasEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailKelasFragment extends DialogFragment implements View.OnClickListener, KelasDao.onListener {
    private TextView tvTitleKelasDetail, tvIdKelasDetail;
    private TextInputLayout inputLayoutNamaKelasDetail;
    private TextInputEditText inputNamaKelasDetail;
    private Spinner spinWaliKelasDetail;
    private Button btnSaveKelasDetail;
    private String action;
    private ArrayList<Guru> guruList;
    private ArrayList<String> nipList;
    private KelasDao kelasDao;

    public DetailKelasFragment() {
        // Required empty public constructor
    }

    /***
     * jika action add
     */
    public static DetailKelasFragment newInstance(String action, Kelas kelas, ArrayList<Guru> guruList) {
        DetailKelasFragment detailKelasFragment = new DetailKelasFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putParcelable("kelas", kelas);
        bundle.putParcelableArrayList("guruList", guruList);
        detailKelasFragment.setArguments(bundle);

        return detailKelasFragment;
    }

    /***
     * jika action edit
     */
    public static DetailKelasFragment newInstance(String action, ArrayList<Guru> guruList) {
        DetailKelasFragment detailKelasFragment = new DetailKelasFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putParcelableArrayList("guruList", guruList);
        detailKelasFragment.setArguments(bundle);

        return detailKelasFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_kelas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kelasDao = new KelasDao(getContext(), this);

        // set transparent fragment
        // default background is white
        assert getDialog() != null;
        assert getDialog().getWindow() != null;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        tvTitleKelasDetail = view.findViewById(R.id.tv_title_kelas_detail);
        tvIdKelasDetail = view.findViewById(R.id.tv_id_kelas_detail);
        inputLayoutNamaKelasDetail = view.findViewById(R.id.input_layout_nama_kelas_detail);
        inputNamaKelasDetail = view.findViewById(R.id.input_nama_kelas_detail);
        spinWaliKelasDetail = view.findViewById(R.id.spin_wali_kelas_detail);

        btnSaveKelasDetail = view.findViewById(R.id.btn_save_kelas_detail);
        btnSaveKelasDetail.setOnClickListener(this);

        assert getArguments() != null;
        guruList = getArguments().getParcelableArrayList("guruList");
        setNipListData();
        setSpinnerWaliKelas();

        if (getArguments() != null) {
            action = getArguments().getString("action");

            assert action != null;
            if (action.equals("edit")) {
                tvTitleKelasDetail.setText(R.string.edit_kelas);
                Kelas kelas = getArguments().getParcelable("kelas");

                assert kelas != null;
                tvIdKelasDetail.setText(kelas.getIdKelas());
                inputNamaKelasDetail.setText(kelas.getNama());
                spinWaliKelasDetail.setSelection(nipList.indexOf(kelas.getWaliKelas()));
            }

            if (action.equals("add")) {
                tvTitleKelasDetail.setText(R.string.tambah_kelas);
                tvIdKelasDetail.setText("-");
            }
        }
    }

    /***
     * set nip list data ke dalam array list
     */
    private void setNipListData() {
        nipList = new ArrayList<>();
        for (Guru guru : guruList) {
            nipList.add(guru.getNip());
        }
    }

    /***
     * set spinner yang berupa nama dari guru yang diambil dari data guruList
     */
    private void setSpinnerWaliKelas() {
        ArrayList<String> namaGuru = new ArrayList<>();
        for (Guru guru : guruList) {
            namaGuru.add(guru.getFirstName() + " " + guru.getLastName());
        }

        ArrayAdapter<String> arrayAdapterWaliKelas = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, namaGuru);
        arrayAdapterWaliKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinWaliKelasDetail.setAdapter(arrayAdapterWaliKelas);
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData(action);
            moveToKelasActivity();
        }
    }

    /***
     * send data to server
     */
    private void sendData(String action) {
        String idKelas = tvIdKelasDetail.getText().toString().trim();
        String nama = inputNamaKelasDetail.getText().toString().trim();
        String waliKelas = nipList.get(spinWaliKelasDetail.getSelectedItemPosition());

        if (action.equals("edit")) {
            Kelas kelas = new Kelas(idKelas, nama, waliKelas);
            kelasDao.put(kelas);
        }

        if (action.equals("add")) {
            Kelas kelas = new Kelas(null, nama, waliKelas);
            kelasDao.post(kelas);
        }
    }

    /***
     * move to activity sebelumnya
     */
    private void moveToKelasActivity() {
        assert getActivity() != null;
        // menyelesaikan atau menghapus activity dengan list data lama
        getActivity().finish();

        // membuat activity baru dengan list data terbaru
        Intent intent = new Intent(getContext(), KelasActivity.class);
        getActivity().startActivity(intent);
    }

    /***
     * validasi input jika ada yang kosong
     */
    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNamaKelasDetail.getText().toString().trim())) {
            inputLayoutNamaKelasDetail.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    @Override
    public void kelasResponse(Kelas kelas) {

    }

    @Override
    public void kelasListResponse(ArrayList<Kelas> kelasList) {

    }

    @Override
    public void defaultResponse(String response) {
        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
