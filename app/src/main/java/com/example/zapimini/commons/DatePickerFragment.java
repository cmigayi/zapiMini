package com.example.zapimini.commons;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Class mActivity;

    public DatePickerFragment(Class mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month + 1;

        String date;
        String monthStr = ""+month;
        String dayStr = ""+day;

        if(String.valueOf(month).length() == 1){
            monthStr = "0"+month;
        }

        if(String.valueOf(day).length() == 1){
            dayStr = "0"+day;
        }

        date = year+"-"+monthStr+"-"+dayStr;

        Log.d("DateItem",date);
        Intent intent = new Intent(
                getActivity(), mActivity);
        intent.putExtra("date", date);
        intent.putExtra("action", "datePicker");
        startActivity(intent);
        getActivity().finish();
    }
}

