package com.zukron.sman1bungo.fragment.tools;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.zukron.sman1bungo.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
//        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, year, month, day);
        return new DatePickerDialog(getActivity(), R.style.MyDatePicker, listener, year, month, day);
    }
}
