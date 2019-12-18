package fr.utt.myhabits.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.utt.myhabits.data.entities.Habit;

@Dao
public interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Habit habit);

    @Query("DELETE FROM habits")
    void deleteAll();

    @Query("SELECT * from habits")
    LiveData<List<Habit>> getAll();
}
