package fr.utt.myhabits.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.data.AppRepository;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HomeViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<Habit>> mAllHabits;
    private LiveData<List<WeekHabits>> mAllWeekHabits;
    private Calendar calendar;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllHabits = mRepository.getAllHabits();
        mAllWeekHabits = mRepository.getAllWeekHabits();
        calendar = Calendar.getInstance();
    }

    public LiveData<List<Habit>> getTodayHabits() {
        LiveData<List<Habit>> todayHabits = Transformations.map(mAllHabits, data -> filterHabits(data));
        return todayHabits;
    }

    List<Habit> filterHabits(List<Habit> list) {
        List<Habit> habits = new ArrayList<>();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int exactDay = calendar.get(Calendar.DAY_OF_YEAR);
        for (Habit habit : list) {
            String habitRep = habit.getRepetition();
            if (habitRep.equals("every") || habitRep.equals(String.valueOf(currentDay)) || habitRep.equals(String.valueOf(exactDay))) {
                habits.add(habit);
            }
        }
        return habits;
    }

    public LiveData<WeekHabits> currentWeek() {
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        LiveData<WeekHabits> weekHabits = Transformations.map(mAllWeekHabits, data -> selectWeek(data, weekNumber));
        return weekHabits;
    }

    WeekHabits selectWeek(List<WeekHabits> list, int weekNumber) {
        for (WeekHabits week : list) {
            if (week.getWeekNumber() == weekNumber) {
                return week;
            }
        }
        return null;
    }

    public void insertHabit(Habit habit) { mRepository.insertHabit(habit); }

    public void insertWeekHabits(WeekHabits weekHabits) { mRepository.insertWeekHabits(weekHabits); }
}