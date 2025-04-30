package com.syu.itzy_mayo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> goals;

    public GoalAdapter(List<Goal> goals) {
        this.goals = new ArrayList<>(goals);
    }

    public void setGoals(List<Goal> newGoals) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return goals != null ? goals.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newGoals != null ? newGoals.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                Goal oldGoal = goals.get(oldItemPosition);
                Goal newGoal = newGoals.get(newItemPosition);
                return oldGoal.title.equals(newGoal.title) && oldGoal.time.equals(newGoal.time);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return goals.get(oldItemPosition).equals(newGoals.get(newItemPosition));
            }
        });
        goals = new ArrayList<>(newGoals);
        diffResult.dispatchUpdatesTo(this);
    }

    public List<Goal> getGoals() {
        return goals;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal_card, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);

        holder.tvTitle.setText(goal.title);
        holder.tvTime.setText(goal.time);

        holder.cbGoalDone.setOnCheckedChangeListener(null);
        holder.cbGoalDone.setChecked(goal.isCompleted);
        holder.tvGoalStatus.setVisibility(goal.isCompleted ? View.VISIBLE : View.GONE);

        holder.itemView.setAlpha(1f);
        holder.cbGoalDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            goal.isCompleted = isChecked;
            if (isChecked) {
                holder.itemView.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(() -> {
                            int pos = holder.getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION && pos < goals.size()) {
                                List<Goal> newGoals = new ArrayList<>(goals);
                                newGoals.remove(pos);
                                setGoals(newGoals);
                            }
                        })
                        .start();
            } else {
                holder.itemView.setAlpha(1f);
                holder.tvGoalStatus.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goals != null ? goals.size() : 0;
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvGoalStatus;
        CheckBox cbGoalDone;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGoalTitle);
            tvTime = itemView.findViewById(R.id.tvGoalTime);
            cbGoalDone = itemView.findViewById(R.id.cbGoalDone);
            tvGoalStatus = itemView.findViewById(R.id.tvGoalStatus);
        }
    }
}