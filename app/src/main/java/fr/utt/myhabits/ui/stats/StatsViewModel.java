package fr.utt.myhabits.ui.stats;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.utt.myhabits.data.AppRepository;
import fr.utt.myhabits.data.entities.WeekHabits;

public class StatsViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<WeekHabits>> mAllWeekHabits;

    public StatsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllWeekHabits = mRepository.getAllWeekHabits();
    }

    public LiveData<List<WeekHabits>> getAllWeekHabits() { return mAllWeekHabits; }
}