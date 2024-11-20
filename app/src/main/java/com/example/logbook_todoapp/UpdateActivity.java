package com.example.logbook_todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    TextView datePicker, timePicker, showDate, showTime;
    EditText taskName;
    Button updateBtn, deleteBtn;

    String id, name, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        datePicker = findViewById(R.id.date_picker2);
        showDate = findViewById(R.id.show_date2);
        timePicker = findViewById(R.id.time_picker2);
        showTime = findViewById(R.id.show_time2);
        taskName = findViewById(R.id.task_name2);
        updateBtn = findViewById(R.id.update_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        //Call this First
        getandSetIntentData();

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }

        //Click listener for date and time picker
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openDatePicker();}
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openTimePicker();}
        });

        //Click listener for update and delete button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDiaglog();
            }
        });


    }

    //Open date picker dialog
    private void openDatePicker(){
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDate.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    //Open time picker dialog
    private void openTimePicker(){
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                showTime.setText(hourOfDay + ":" + minute);
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    //Get and set intent data
    void getandSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name")
        && getIntent().hasExtra("date") && getIntent().hasExtra("time")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");

            //Setting Intent Data
            taskName.setText(name);
            showDate.setText(date);
            showTime.setText(time);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    //Update task to the database
    private void updateTask(){
        name = taskName.getText().toString().trim();
        date = showDate.getText().toString().trim();
        time = showTime.getText().toString().trim();

        //Call After Update
        DatabaseHelper dbHelper = new DatabaseHelper(UpdateActivity.this);
        dbHelper.updateData(id, name, date, time);
        setResult(RESULT_OK);
        finish();
    }

    //Show diaglog when user click delete button
    void confirmDiaglog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure want to delete " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper dbHelper = new DatabaseHelper(UpdateActivity.this);
                dbHelper.deleteOneRow(id);
                setResult(RESULT_OK);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show(); //Show confirm dialog
    }
}