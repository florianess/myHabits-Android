package fr.utt.myhabits.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

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

    public LiveData<List<WeekHabits>> getAllWeekHabits() { return mAllWeekHabits; }

    public void insertHabit(Habit habit) { mRepository.insertHabit(habit); }

    public void insertWeekHabits(WeekHabits weekHabits) { mRepository.insertWeekHabits(weekHabits); }

    public void updateWeekHabits(WeekHabits weekHabits) { mRepository.updateWeekHabits(weekHabits); }
}