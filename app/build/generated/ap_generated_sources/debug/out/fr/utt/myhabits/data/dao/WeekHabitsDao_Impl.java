package fr.utt.myhabits.data.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import fr.utt.myhabits.data.entities.WeekHabits;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WeekHabitsDao_Impl implements WeekHabitsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WeekHabits> __insertionAdapterOfWeekHabits;

  private final EntityDeletionOrUpdateAdapter<WeekHabits> __updateAdapterOfWeekHabits;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WeekHabitsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWeekHabits = new EntityInsertionAdapter<WeekHabits>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `week_habits` (`weekNumber`,`total_habits`,`habits_done`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WeekHabits value) {
        stmt.bindLong(1, value.getWeekNumber());
        if (value.getTotalHabits() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTotalHabits());
        }
        if (value.getHabitsDone() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getHabitsDone());
        }
      }
    };
    this.__updateAdapterOfWeekHabits = new EntityDeletionOrUpdateAdapter<WeekHabits>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `week_habits` SET `weekNumber` = ?,`total_habits` = ?,`habits_done` = ? WHERE `weekNumber` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WeekHabits value) {
        stmt.bindLong(1, value.getWeekNumber());
        if (value.getTotalHabits() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTotalHabits());
        }
        if (value.getHabitsDone() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getHabitsDone());
        }
        stmt.bindLong(4, value.getWeekNumber());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM week_habits";
        return _query;
      }
    };
  }

  @Override
  public void insert(final WeekHabits weekHabits) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfWeekHabits.insert(weekHabits);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final WeekHabits weekHabits) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfWeekHabits.handle(weekHabits);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<WeekHabits>> getAll() {
    final String _sql = "SELECT * from week_habits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"week_habits"}, false, new Callable<List<WeekHabits>>() {
      @Override
      public List<WeekHabits> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfTotalHabits = CursorUtil.getColumnIndexOrThrow(_cursor, "total_habits");
          final int _cursorIndexOfHabitsDone = CursorUtil.getColumnIndexOrThrow(_cursor, "habits_done");
          final List<WeekHabits> _result = new ArrayList<WeekHabits>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final WeekHabits _item;
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpTotalHabits;
            _tmpTotalHabits = _cursor.getString(_cursorIndexOfTotalHabits);
            final String _tmpHabitsDone;
            _tmpHabitsDone = _cursor.getString(_cursorIndexOfHabitsDone);
            _item = new WeekHabits(_tmpWeekNumber,_tmpTotalHabits,_tmpHabitsDone);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
