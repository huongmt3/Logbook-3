package com.example.logbook_todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskCheckedChangeListener  {

    RecyclerView recyclerView;
    FloatingActionButton add_btn;
    ImageView empty_image_view;
    TextView no_data;

    DatabaseHelper dbHelper;
    ArrayList<String> task_id, task_name, task_date, task_time, is_checked;
    TaskAdapter taskAdapter;

    CheckBox checkBox;

    //Call this when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_btn = findViewById(R.id.add_btn);
        empty_image_view = findViewById(R.id.empty_image_view);
        no_data = findViewById(R.id.no_data);
//        checkBox = findViewById(R.id.checkBox);

        //Listener for add button
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(intent, 1);
//            startActivity(intent);
            //startActivity(intent);
            }
        });

//        checkBox.setOnClickListener(new View.OnClickListener){
//            @Override
//            public void onClick(View view){
//
//            }
//        }

//        boolean isChecked = dbHelper.getBoolean("is Checked", false);
//        checkBox.setChecked(isChecked);

        //Initialise database helper and array lists
        dbHelper = new DatabaseHelper(this);
        task_id = new ArrayList<>();
        task_name = new ArrayList<>();
        task_date = new ArrayList<>();
        task_time = new ArrayList<>();
        is_checked = new ArrayList<>();

        //Store data from the database into arrays
        storeDataInArrays();

        //Set up RecyclerView
        taskAdapter = new TaskAdapter(MainActivity.this, this, task_id, task_name, task_date, task_time, is_checked, this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Called when an activity user launched exits
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1) {
//            recreate();
//        }
        if(requestCode == 1 && resultCode == RESULT_OK){

            //Clear Old Data
            task_id.clear();
            task_name.clear();
            task_date.clear();
            task_time.clear();
            is_checked.clear();

            //Reload Data from the Database
            storeDataInArrays();

            //Notify
            taskAdapter.notifyDataSetChanged();
        }
    }

    //Store data from the database into arrays
    void storeDataInArrays(){
        Cursor cursor = dbHelper.readAllData();
        if(cursor.getCount() == 0){
            empty_image_view.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()){
                task_id.add(cursor.getString(0));
                task_name.add(cursor.getString(1));
                task_date.add(cursor.getString(2));
                task_time.add(cursor.getString(3));
                is_checked.add(cursor.getString(4));

            }
            empty_image_view.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    //Create menu to delete all tasks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handle item selections in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all) {
            confirmDiaglog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Confirm diaglog for delete all tasks
    void confirmDiaglog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Tasks?");
        builder.setMessage("Are you sure want to delete all the tasks?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                dbHelper.deleteAllData();

                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    //Handle task checked change events
    @Override
    public void onTaskCheckedChange(int position, boolean isChecked) {
        //Update the status in the database
        dbHelper.updateTaskStatus(String.valueOf(task_id.get(position)), isChecked ? "1" : "0");

        //Optionally update the local list
        is_checked.set(position, isChecked ? "1" : "0");
    }

//    @Override
//    public void onTaskCheckedChange(String taskId, boolean isChecked) {
//        dbHelper.checkStatus(taskId, isChecked);
//    }
}