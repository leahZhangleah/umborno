package com.example.umborno.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umborno.R;
import com.example.umborno.model.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    List<Reminder> reminderList;

    public ReminderAdapter() {
        this.reminderList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.description.setText(String.format(Locale.CHINA,"%d,%s,%s",reminder.getId(),reminder.getDateTime(),reminder.getDescription()));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void setReminderList(List<Reminder> reminders){
        if(this.reminderList!=reminders){
            this.reminderList = reminders;
            notifyDataSetChanged();
        }
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.reminder_description);
        }
    }
}
