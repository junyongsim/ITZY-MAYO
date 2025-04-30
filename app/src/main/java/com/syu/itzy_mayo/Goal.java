package com.syu.itzy_mayo;

import java.util.Objects;

public class Goal {
    public String title;
    public String time;
    public boolean isCompleted;

    public Goal(String title, String time) {
        this.title = title;
        this.time = time;
        this.isCompleted = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Goal)) return false;
        Goal goal = (Goal) obj;
        return isCompleted == goal.isCompleted &&
                Objects.equals(title, goal.title) &&
                Objects.equals(time, goal.time);
    }
}
