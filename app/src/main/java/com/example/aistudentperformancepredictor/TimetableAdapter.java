package com.example.aistudentperformancepredictor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private List<ScheduleItem> scheduleList;

    public TimetableAdapter(List<ScheduleItem> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleItem item = scheduleList.get(position);
        holder.tvTime.setText(item.time);
        holder.tvTask.setText(item.task);
        holder.tvDuration.setText(item.duration);

        // AI Visualization: Agar weak subject hai to text color red/purple karein
        if (item.isWeakSubject) {
            holder.tvTask.setTextColor(Color.parseColor("#E91E63")); // Pinkish Red for warning
            holder.indicator.setBackgroundColor(Color.parseColor("#E91E63"));
        } else {
            holder.tvTask.setTextColor(Color.parseColor("#22C55E")); // Green for safe
            holder.indicator.setBackgroundColor(Color.parseColor("#22C55E"));
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTask, tvDuration;
        View indicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            indicator = itemView.findViewById(R.id.indicatorView);
        }
    }
}