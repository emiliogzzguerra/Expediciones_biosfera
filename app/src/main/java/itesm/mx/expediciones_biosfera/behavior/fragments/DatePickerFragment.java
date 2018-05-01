package itesm.mx.expediciones_biosfera.behavior.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import itesm.mx.expediciones_biosfera.R;

/**
 * Created by emiliogonzalez on 4/30/18.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        TextView tvDate = getActivity().findViewById(R.id.tv_date);
        int day = Integer.valueOf((String) tvDate.getText().subSequence(0,2));
        int month = Integer.valueOf((String) tvDate.getText().subSequence(3,5));
        int year  = Integer.valueOf((String) tvDate.getText().subSequence(6,10));

        System.out.println(day);
        System.out.println(month);
        System.out.println(year);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        TextView tvDate = getActivity().findViewById(R.id.tv_date);
        TextView tvSummaryPt2 = getActivity().findViewById(R.id.tv_summary_pt2);
        tvDate.setText(getResources().getString(R.string.reservation_date,
                String.format("%02d", day),
                String.format("%02d", month),
                String.format("%02d", year)));

        tvSummaryPt2.setText(getResources().getString(R.string.reservation_summary_part_two,
                        String.format("%02d", day),
                        String.format("%02d", month),
                        String.format("%02d", year)));
    }
}