package com.example.logbook_todoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList task_id, task_name, task_date, task_time;

    Animation translate_anim;

    TaskAdapter(Activity activity,
                Context context,
                ArrayList task_id,
                ArrayList task_name,
                ArrayList task_date,
                ArrayList task_time){
            this.activity = activity;
            this.context = context;
            this.task_id = task_id;
            this.task_name = task_name;
            this.task_date = task_date;
            this.task_time = task_time;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new TaskViewHolder(view);
    }

    @RequiresApi (api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.TaskViewHolder holder, final int position) {
        holder.task_id_txt.setText(String.valueOf(task_id.get(position)));
        holder.task_name_txt.setText(String.valueOf(task_name.get(position)));
        holder.task_date_txt.setText(String.valueOf(task_date.get(position)));
        holder.task_time_txt.setText(String.valueOf(task_time.get(position)));
        holder.checkBox.setChecked(false);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(task_id.get(position)));
                intent.putExtra("name", String.valueOf(task_name.get(position)));
                intent.putExtra("date", String.valueOf(task_date.get(position)));
                intent.putExtra("time", String.valueOf(task_time.get(position)));

                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return task_id.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView task_id_txt, task_name_txt, task_date_txt, task_time_txt;
        CheckBox checkBox;
        LinearLayout main_layout;

        TaskViewHolder(@NonNull View itemView){
            super(itemView);
            task_id_txt = itemView.findViewById(R.id.task_id_txt);
            task_name_txt = itemView.findViewById(R.id.task_name_txt);
            task_date_txt = itemView.findViewById(R.id.task_date_txt);
            task_time_txt = itemView.findViewById(R.id.task_time_txt);
            checkBox = itemView.findViewById(R.id.checkBox);
            main_layout = itemView.findViewById(R.id.main_layout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            main_layout.setAnimation(translate_anim);
        }

    }
}
