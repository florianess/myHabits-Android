package fr.utt.myhabits.ui.stats;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import fr.utt.myhabits.data.AppRepository;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class StatsViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<WeekHabits>> mAllWeekHabits;
    private LiveData<List<Habit>> mAllHabits;

    public StatsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllHabits = mRepository.getAllHabits();
        mAllWeekHabits = mRepository.getAllWeekHabits();


    }

    public LiveData<List<WeekHabits>> getAllWeekHabits() { return mAllWeekHabits; }

    public LiveData<List<Habit>> getAllHabits() { return mAllHabits; }

    public void createWeek(int weekNumber, List<Habit> allHabits) {
        List<String> totalHabits = new ArrayList<>();
        List<String> habitsDone = new ArrayList<>();
        int dailyHabits = 0;
        for (Habit dhabit : allHabits) {
            if (dhabit.getRepetition().equals("every")) {
                dailyHabits ++;
            }
        }
        for (int i = 0; i < 7; i++) {
            totalHabits.add(String.valueOf(dailyHabits));
            habitsDone.add("");
        }
        WeekHabits weekHabits = new WeekHabits(
                weekNumber,
                TextUtils.join(",", totalHabits),
                TextUtils.join(",", habitsDone)
        );
        mRepository.insertWeekHabits(weekHabits);
    }
}