package com.zukron.sman1bungo.fragment.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.zukron.sman1bungo.activities.PelajaranActivity;
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.model.dao.PelajaranDao;
import com.zukron.sman1bungo.util.Tools;
import com.zukron.sman1bungo.util.api.PelajaranEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailPelajaranFragment extends DialogFragment implements View.OnClickListener, PelajaranDao.onListener {
    private TextInputLayout inputLayoutNamaPelajaranDetail;
    private TextInputEditText inputNamaPelajaranDetail;
    private TextView tvTitlePelajaranDetail, tvIdPelajaranDetail;
    private Button btnSavePelajaranDetail;
    private String action;
    private PelajaranDao pelajaranDao;

    public DetailPelajaranFragment() {
        // Required empty public constructor
    }

    /***
     * jika action edit
     */
    public static DetailPelajaranFragment newInstance(String action, Pelajaran pelajaran) {
        DetailPelajaranFragment detailPelajaranFragment = new DetailPelajaranFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putParcelable("pelajaran", pelajaran);
        detailPelajaranFragment.setArguments(bundle);

        return detailPelajaranFragment;
    }

    /***
     * jika action add
     */
    public static DetailPelajaranFragment newInstance(String action) {
        DetailPelajaranFragment detailPelajaranFragment = new DetailPelajaranFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        detailPelajaranFragment.setArguments(bundle);

        return detailPelajaranFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pelajaran, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set transparent fragment
        // default background is white
        assert getDialog() != null;
        assert getDialog().getWindow() != null;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        pelajaranDao = new PelajaranDao(getContext(), this);

        tvTitlePelajaranDetail = view.findViewById(R.id.tv_title_pelajaran_detail);
        tvIdPelajaranDetail = view.findViewById(R.id.tv_id_pelajaran_detail);
        inputLayoutNamaPelajaranDetail = view.findViewById(R.id.input_layout_nama_pelajaran_detail);
        inputNamaPelajaranDetail = view.findViewById(R.id.input_nama_pelajaran_detail);

        btnSavePelajaranDetail = view.findViewById(R.id.btn_save_pelajaran_detail);
        btnSavePelajaranDetail.setOnClickListener(this);


        if (getArguments() != null) {
            action = getArguments().getString("action");

            assert action != null;
            if (action.equals("edit")) {
                Pelajaran pelajaran = getArguments().getParcelable("pelajaran");
                tvTitlePelajaranDetail.setText(R.string.edit_pelajaran);

                assert pelajaran != null;
                tvIdPelajaranDetail.setText(pelajaran.getIdPelajaran());
                inputNamaPelajaranDetail.setText(pelajaran.getNama());
            }

            if (action.equals("add")) {
                tvTitlePelajaranDetail.setText(R.string.tambah_pelajaran);
                tvIdPelajaranDetail.setText("-");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData(action);
            moveToPelajaranActivity();
        }
    }

    /***
     * send to server jika ada perubahan atau penambahan
     */
    private void sendData(String action) {
        if (action.equals("edit")) {
            String idPelajaran = tvIdPelajaranDetail.getText().toString().trim();
            String nama = inputNamaPelajaranDetail.getText().toString().trim();

            Pelajaran pelajaran = new Pelajaran(idPelajaran, nama);
            pelajaranDao.put(pelajaran);
        }

        if (action.equals("add")) {
            String nama = inputNamaPelajaranDetail.getText().toString().trim();

            Pelajaran pelajaran = new Pelajaran(null, nama);
            pelajaranDao.post(pelajaran);
        }
    }

    /**
     * validate input jika ada yang null
     */
    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus diisi";

        if (TextUtils.isEmpty(inputNamaPelajaranDetail.getText().toString().trim())) {
            inputLayoutNamaPelajaranDetail.setError(errorMsg);
            valid = false;
        }

        return valid;
    }

    /***
     * move to activity sebelumnya dengan men-finishkan DetailPelajaranFragment
     */
    private void moveToPelajaranActivity() {
        assert getActivity() != null;
        // menyelesaikan atau menghapus activity dengan list data lama
        getActivity().finish();

        // membuat activity baru dengan list data terbaru
        Intent intent = new Intent(getContext(), PelajaranActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void pelajaranResponse(Pelajaran pelajaran) {
        // no need
    }

    @Override
    public void pelajaranListResponse(ArrayList<Pelajaran> pelajaranList) {
        // no need
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
