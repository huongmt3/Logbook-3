package com.example.logbook_todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    TextView showDate, showTime, datePicker, timePicker;
    EditText taskName;
    Button addBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        showDate = findViewById(R.id.show_date);
        datePicker = findViewById(R.id.date_picker);
        showTime = findViewById(R.id.show_time);
        timePicker = findViewById(R.id.time_picker);
        taskName = findViewById(R.id.task_name);
        addBtn = findViewById(R.id.add_btn);

        dbHelper = new DatabaseHelper(this);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void openDatePicker(){
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDate.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void openTimePicker(){
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                showTime.setText(hourOfDay + ":" + minute);
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private void saveTask() {
        String task_name = taskName.getText().toString().trim();
        String task_date = showDate.getText().toString().trim();
        String task_time = showTime.getText().toString().trim();

        if (task_name.isEmpty() || task_date.isEmpty() || task_time.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        } else {

            dbHelper.addTask(task_name, task_date, task_time);
            setResult(RESULT_OK);
            finish();
        }
    }
}