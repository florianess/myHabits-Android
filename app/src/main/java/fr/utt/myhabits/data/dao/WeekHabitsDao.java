package fr.utt.myhabits.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.utt.myhabits.data.entities.WeekHabits;

@Dao
public interface WeekHabitsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WeekHabits weekHabits);

    @Query("DELETE FROM week_habits")
    void deleteAll();

    @Query("SELECT * from week_habits")
    LiveData<List<WeekHabits>> getAll();
}
