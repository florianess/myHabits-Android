package fr.utt.myhabits.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.data.dao.HabitDao;
import fr.utt.myhabits.data.dao.WeekHabitsDao;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class AppRepository {

    private HabitDao mHabitDao;
    private WeekHabitsDao mWeekHabitsDao;
    private LiveData<List<Habit>> mAllHabits;
    private LiveData<List<WeekHabits>> mAllWeekHabits;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mHabitDao = db.habitDao();
        mWeekHabitsDao = db.weekHabitsDao();
        mAllHabits = mHabitDao.getAll();
        mAllWeekHabits = mWeekHabitsDao.getAll();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return mAllHabits;
    }

    public LiveData<List<WeekHabits>> getAllWeekHabits() { return  mAllWeekHabits; }

    public void insertHabit(final Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHabitDao.insert(habit);
        });
    }

    public void insertWeekHabits(final WeekHabits weekHabits) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mWeekHabitsDao.insert(weekHabits);
        });
    }
}
