package fr.utt.myhabits.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.utt.myhabits.data.AppRepository;
import fr.utt.myhabits.data.entities.Habit;

public class HomeViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<Habit>> mAllHabits;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllHabits = mRepository.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() { return mAllHabits; }

    public void insert(Habit habit) { mRepository.insert(habit); }
}