package com.zukron.sman1bungo.fragment.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.activities.GajiActivity;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.model.dao.GajiDao;

import java.util.ArrayList;

public class DetailGajiFragment extends DialogFragment implements View.OnClickListener, GajiDao.onListener {
    private TextView tvTitleGajiDetail, tvIdGajiDetail;
    private TextInputLayout inputLayoutGajiPokokDetail;
    private TextInputEditText inputGajiPokokDetail;
    private Button btnSaveGajiDetail;
    private String action;
    private Gaji gaji;
    private GajiDao gajiDao;

    public DetailGajiFragment() {
        // Required empty public constructor
    }

    /***
     * jika action adalah edit
     */
    public static DetailGajiFragment newInstance(String action, Gaji gaji) {
        DetailGajiFragment detailGajiFragment = new DetailGajiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putParcelable("gaji", gaji);
        detailGajiFragment.setArguments(bundle);

        return detailGajiFragment;
    }

    /***
     * jika action adalah add
     */
    public static DetailGajiFragment newInstance(String action) {
        DetailGajiFragment detailGajiFragment = new DetailGajiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        detailGajiFragment.setArguments(bundle);

        return detailGajiFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_gaji, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gajiDao = new GajiDao(getContext(), this);

        // set transparent fragment
        // default background is white
        assert getDialog() != null;
        assert getDialog().getWindow() != null;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        tvTitleGajiDetail = view.findViewById(R.id.tv_title_gaji_detail);
        tvIdGajiDetail = view.findViewById(R.id.tv_id_gaji_detail);
        inputLayoutGajiPokokDetail = view.findViewById(R.id.input_layout_gaji_pokok_detail);
        inputGajiPokokDetail = view.findViewById(R.id.input_gaji_pokok_detail);

        btnSaveGajiDetail = view.findViewById(R.id.btn_save_gaji_detail);
        btnSaveGajiDetail.setOnClickListener(this);

        if (getArguments() != null) {
            action = getArguments().getString("action");

            assert action != null;
            if (action.equals("edit")) {
                tvTitleGajiDetail.setText(R.string.edit_gaji);
                gaji = getArguments().getParcelable("gaji");

                assert gaji != null;
                tvIdGajiDetail.setText(gaji.getIdGaji());
                inputGajiPokokDetail.setText(String.valueOf(gaji.getGajiPokok()));
            }

            if (action.equals("add")) {
                tvTitleGajiDetail.setText(R.string.tambah_gaji);
                tvIdGajiDetail.setText("-");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (validateInput()) {
            sendData(action);
            moveToGajiActivity();
        }
    }

    /***
     * send data to server when save input, sesuai action edit atau add
     */
    private void sendData(String action) {
        if (action.equals("edit")) {
            String idGaji = tvIdGajiDetail.getText().toString().trim();
            int gajiPokok = Integer.parseInt(inputGajiPokokDetail.getText().toString());
            Gaji gaji = new Gaji(idGaji, gajiPokok);

            gajiDao.put(gaji);
        }

        if (action.equals("add")) {
            int gajiPokok = Integer.parseInt(inputGajiPokokDetail.getText().toString());
            Gaji gaji = new Gaji(null, gajiPokok);

            gajiDao.post(gaji);
        }
    }

    /***
     * validate input jika ada yang kosong
     * @return boolean
     */
    private boolean validateInput() {
        boolean valid = true;
        String errorMsg = "Harus Diisi";

        if (TextUtils.isEmpty(inputGajiPokokDetail.getText().toString().trim())) {
            inputLayoutGajiPokokDetail.setError(errorMsg);
            valid = false;
        }
        return valid;
    }

    /***
     * move to activity sebelumnya dengan men-finishkan DetailGajiFragment
     */
    private void moveToGajiActivity() {
        assert getActivity() != null;
        // menyelesaikan atau menghapus activity dengan list data lama
        getActivity().finish();

        // membuat activity baru dengan list data terbaru
        Intent intent = new Intent(getContext(), GajiActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void gajiResponse(Gaji gaji) {
        // no need
    }

    @Override
    public void gajiListResponse(ArrayList<Gaji> gajiList) {
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
