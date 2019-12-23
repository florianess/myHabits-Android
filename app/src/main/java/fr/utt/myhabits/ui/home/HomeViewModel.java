package fr.utt.myhabits.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import fr.utt.myhabits.data.AppRepository;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HomeViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<Habit>> mAllHabits;
    private LiveData<List<WeekHabits>> mAllWeekHabits;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllHabits = mRepository.getAllHabits();
        mAllWeekHabits = mRepository.getAllWeekHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return mAllHabits;
    }

    public LiveData<WeekHabits> currentWeek() {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
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