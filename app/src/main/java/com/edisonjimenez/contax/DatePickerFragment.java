package com.edisonjimenez.contax;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener{
	
	private TextView dateTextView;
	private boolean newContact;
	private Calendar c;
	
	public DatePickerFragment(TextView v, boolean newContact) {
		super();
		dateTextView = v;
		this.newContact = newContact;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year;
		int month;
    	int day;

    	if (newContact) {
    		c = Calendar.getInstance();
    		year = c.get((Calendar.YEAR));
    		month = c.get((Calendar.MONTH));
    		day = c.get((Calendar.DAY_OF_MONTH));
    		
    		return new DatePickerDialog(getActivity(), 0, this, year, month, day);
    	}
    	else {
    		String[] date = dateTextView.getText().toString().split("/");
    		month = Integer.parseInt(date[0]) - 1;
    		day = Integer.parseInt(date[1]);
    		year = Integer.parseInt(date[2]);
    		
    		return new DatePickerDialog(getActivity(), 0, this, year, month, day);
    	}
    	
    }

	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			dateTextView.setText((monthOfYear +1) + "/" + dayOfMonth + "/" + year);
	}

}
