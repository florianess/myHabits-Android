package fr.utt.myhabits.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "week_habits")
public class WeekHabits {

    @PrimaryKey
    @NonNull
    private int weekNumber;
    @ColumnInfo(name = "total_habits")
    private String totalHabits;
    @ColumnInfo(name = "habits_done")
    private String habitsDone;

    public WeekHabits(int weekNumber, String totalHabits, String habitsDone) {
        this.weekNumber = weekNumber;
        this.totalHabits = totalHabits;
        this.habitsDone = habitsDone;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public String getTotalHabits() {
        return totalHabits;
    }

    public String getHabitsDone() {
        return habitsDone;
    }
}
