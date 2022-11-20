package ru.spornov91.picker;


import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.util.*;
import android.graphics.drawable.*;

public class MainActivity extends Activity 
{
	final int DIALOG_DATE = 1;
	int myYear = 2011;
	int myMonth = 02;
	int myDay = 03;
	final int DIALOG_TIME = 2;
	int myHour = 14;
	int myMinute = 35;
	final int DIALOG_NUMBER = 3;
	TextView tvDate;
	
	//declaring OnClickListener as an object
	private OnClickListener btnDatePicker = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DIALOG_DATE);
		}
	};
	
	private OnClickListener btnTimePicker = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DIALOG_TIME);
		}
	};
	
	private OnClickListener btnNumberPicker = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DIALOG_NUMBER);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		tvDate = findViewById(R.id.tvDate);
		Button btn1 = findViewById(R.id.btnDatePicker);
		Button btn2 = findViewById(R.id.btnTimePicker);
		Button btn3 = findViewById(R.id.btnNumberPicker);
        //passing listener object to button
	    btn1.setOnClickListener(btnDatePicker);
		btn2.setOnClickListener(btnTimePicker);
		btn3.setOnClickListener(btnNumberPicker);
		
	};


    protected Dialog onCreateDialog(int id) {
	
		switch(id){
			case DIALOG_DATE: 
				DatePickerDialog dpd = new DatePickerDialog(this, 
				    android.R.style.Theme_Holo_Light_Dialog,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							myYear = year;
							myMonth = monthOfYear;
							myDay = dayOfMonth;
							tvDate.setText("Today is " + myDay + "/" + myMonth + "/" + myYear);
						}
					},
					myYear, myMonth, myDay);
				dpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				return dpd;
				
			case DIALOG_TIME: 
				TimePickerDialog tpd = new TimePickerDialog(this, 
					android.R.style.Theme_Holo_Light_Dialog,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							myHour = hourOfDay;
							myMinute = minute; 
							tvDate.setText("Time is " + myHour + " hours " + myMinute + " minutes");
						}
					}
				, myHour, myMinute, true);
				tpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				return tpd;
			case DIALOG_NUMBER: 
				final Dialog d = new Dialog(MainActivity.this);
				d.setTitle("NumberPicker");
				d.setContentView(R.layout.dialog_number_picker);
				Button b1 = d.findViewById(R.id.button1);
				Button b2 = d.findViewById(R.id.button2);
				
				final NumberPicker np = d.findViewById(R.id.numberPicker1);
				np.setMaxValue(100);
				np.setMinValue(0);
				np.setWrapSelectorWheel(false);
				np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
						@Override
						public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

							Toast.makeText(getApplicationContext(), ""+newVal, Toast.LENGTH_SHORT).show();

						}
				});
				b1.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							tvDate.setText(String.valueOf(np.getValue()));
							d.dismiss();
						}    
					});
				b2.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							d.dismiss();
						}    
					});
				d.show();
				
		}
		
		return super.onCreateDialog(id);
		
		};
	
}
