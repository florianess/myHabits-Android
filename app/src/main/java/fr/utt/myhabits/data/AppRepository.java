package fr.utt.myhabits.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.utt.myhabits.data.dao.HabitDao;
import fr.utt.myhabits.data.dao.WeekHabitsDao;
import fr.utt.myhabits.data.entities.Habit;

public class AppRepository {

    private HabitDao mHabitDao;
    private WeekHabitsDao mWeekHabitsDao;
    private LiveData<List<Habit>> mAllHabits;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mHabitDao = db.habitDao();
        mWeekHabitsDao = db.weekHabitsDao();
        mAllHabits = mHabitDao.getAll();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return mAllHabits;
    }

    public void insert(final Habit habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHabitDao.insert(habit);
        });
    }
}
