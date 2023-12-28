package com.example.assigment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private OnTimeSelectedListener listener;

    public static TimePickerFragment newInstance() {
        return new TimePickerFragment();
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {
            // Create a Date object with the selected time
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            Date selectedTime = calendar.getTime();

            // Use DateFormat.format with the Date object
            String formattedTime = DateFormat.format("hh:mm aa", selectedTime).toString();
            listener.onTimeSelected(formattedTime);
        }, hour, minute, false);
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }
}
