package com.syu.itzy_mayo;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoalFragment extends Fragment {
    private RecyclerView recyclerView;
    private GoalAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        recyclerView = view.findViewById(R.id.goalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GoalAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddGoal);
        fab.setOnClickListener(v -> showAddGoalDialog());

        return view;
    }

    private void showAddGoalDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_goal, null);
        final EditText etTitle = dialogView.findViewById(R.id.etGoalTitle);
        final EditText etTime  = dialogView.findViewById(R.id.etGoalTime);

        etTime.setOnClickListener(v -> {
            View focusView = getActivity().getCurrentFocus();
            if (focusView != null) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                etTime.clearFocus();
            }

            etTime.postDelayed(() -> {
                int hour = 8;
                int minute = 0;
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), (pickerView, h, m) -> {
                    String timeStr = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                    etTime.setText(timeStr);
                }, hour, minute, true);
                timePicker.show();
            }, 150); // 0.15초 딜레이
        });

        new AlertDialog.Builder(getContext())
                .setTitle("새 목표 추가")
                .setView(dialogView)
                .setPositiveButton("추가", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String time  = etTime.getText().toString().trim();
                    if(!title.isEmpty() && !time.isEmpty()) {
                        List<Goal> newGoals = new ArrayList<>(adapter.getGoals());
                        newGoals.add(new Goal(title, time));
                        adapter.setGoals(newGoals);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
}