package com.zukron.sman1bungo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zukron.sman1bungo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalPelajaranFragment extends Fragment {

    public JadwalPelajaranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jadwal_pelajaran, container, false);
    }
}
