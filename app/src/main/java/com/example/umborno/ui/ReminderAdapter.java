package com.example.umborno.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umborno.R;
import com.example.umborno.model.reminder_model.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private static final String TAG = "ReminderAdapter";
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
        //long press
        holder.description.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: called with v = ["+v+"]");
                return false;
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSwipeListener!=null){
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSwipeListener!=null){
                    mOnSwipeListener.onEdit(holder.getAdapterPosition());
                }
            }
        });
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
        Button editBtn,deleteBtn;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.reminder_description);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    //communicate with fragment
    private onSwipeListener mOnSwipeListener;
    public interface onSwipeListener{
        void onDel(int pos);
        void onEdit(int pos);
    }

    public onSwipeListener getmOnSwipeListener() {
        return mOnSwipeListener;
    }

    public void setmOnSwipeListener(onSwipeListener mOnSwipeListener) {
        this.mOnSwipeListener = mOnSwipeListener;
    }
}
