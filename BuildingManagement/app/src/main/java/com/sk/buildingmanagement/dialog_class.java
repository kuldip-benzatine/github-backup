package com.sk.buildingmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ISKM on 05/05/2016.
 */
public class dialog_class
{
    Calendar date;
    public static String date_picker_dialog(final Context mcontext)
    {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                date.set(year, monthOfYear, dayOfMonth);

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        return  Calendar.YEAR+"-"+Calendar.MONTH+"-"+Calendar.DATE;
    }
    public static  String time_picker_dialog(final Context mcontext)
    {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
            }
        },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        return Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE;
    }

    public static void date_time_picker_dialog(Context mcontext, final TextView editText)
    {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        // time picker dialog show and get time
        final TimePickerDialog timePickerDialog =new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                editText.append(" "+hourOfDay+":"+minute+":00");
            }
        },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
        // date picker dialog show and get date
        // Get Current Date



        DatePickerDialog datePickerDialog = new DatePickerDialog(mcontext,new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth)
            {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                timePickerDialog.show();
                // birthdate=update_profile_date_txt.getText().toString();

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


}
