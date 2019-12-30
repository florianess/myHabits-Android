package fr.utt.myhabits.data.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import fr.utt.myhabits.data.entities.Habit;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HabitDao_Impl implements HabitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Habit> __insertionAdapterOfHabit;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HabitDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHabit = new EntityInsertionAdapter<Habit>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `habits` (`name`,`description`,`category`,`repetition`,`repetition_label`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Habit value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescription());
        }
        if (value.getCategory() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getCategory());
        }
        if (value.getRepetition() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getRepetition());
        }
        if (value.getRepetitionLabel() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getRepetitionLabel());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM habits";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Habit habit) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHabit.insert(habit);
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
  public LiveData<List<Habit>> getAll() {
    final String _sql = "SELECT * from habits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"habits"}, false, new Callable<List<Habit>>() {
      @Override
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfRepetition = CursorUtil.getColumnIndexOrThrow(_cursor, "repetition");
          final int _cursorIndexOfRepetitionLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "repetition_label");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Habit _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpRepetition;
            _tmpRepetition = _cursor.getString(_cursorIndexOfRepetition);
            final String _tmpRepetitionLabel;
            _tmpRepetitionLabel = _cursor.getString(_cursorIndexOfRepetitionLabel);
            _item = new Habit(_tmpName,_tmpDescription,_tmpCategory,_tmpRepetitionLabel,_tmpRepetition);
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
