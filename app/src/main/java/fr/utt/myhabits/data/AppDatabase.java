package fr.utt.myhabits.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.utt.myhabits.data.dao.HabitDao;
import fr.utt.myhabits.data.dao.WeekHabitsDao;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

@Database(entities = {Habit.class, WeekHabits.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WeekHabitsDao weekHabitsDao();
    public abstract HabitDao habitDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (AppDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "app_database")
                                .build();
                    }
                }
            }
            return INSTANCE;
    }
}
