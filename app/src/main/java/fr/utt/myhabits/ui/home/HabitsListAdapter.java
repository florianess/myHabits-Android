package fr.utt.myhabits.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HabitsListAdapter extends RecyclerView.Adapter<HabitsListAdapter.HabitsViewHolder> {

    class HabitsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleText;
        private final TextView descText;
        private final TextView categoryText;
        private final TextView repetitionText;
        private final CheckBox doneCheckBox;

        public HabitsViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleHabit);
            descText = itemView.findViewById(R.id.descHabit);
            categoryText = itemView.findViewById(R.id.categoryHabit);
            repetitionText = itemView.findViewById(R.id.repetitionHabit);
            doneCheckBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            doneCheckBox.setChecked(!doneCheckBox.isChecked());
            HomeViewModel homeViewModel = ViewModelProviders.of(mFragment).get(HomeViewModel.class);
            if (doneCheckBox.isChecked()) {
                todayHabitsDone.add(titleText.getText().toString());
            } else {
                List<String> updatedTodayHabits = new ArrayList<>();
                for (String title: todayHabitsDone) {
                    if (!title.equals(titleText.getText().toString())) {
                        updatedTodayHabits.add(title);
                    }
                }
            }
            String todayHabitsDoneStr = TextUtils.join("&", todayHabitsDone);
            int currentDay = cursor.get(Calendar.DAY_OF_WEEK);
            currentWeekHabits.set(currentDay-1, todayHabitsDoneStr);
            String currentWeekHabitsStr = TextUtils.join(",", currentWeekHabits);
            mWeekHabits.setHabitsDone(currentWeekHabitsStr);
            homeViewModel.updateWeekHabits(mWeekHabits);
        }
    }

    private final LayoutInflater mInflater;
    private List<Habit> mHabits;
    private List<String> currentWeekHabits;
    private List<String> todayHabitsDone;
    private Fragment mFragment;
    private WeekHabits mWeekHabits;
    private Calendar cursor;

    public HabitsListAdapter(Context context, Fragment fragment) {
        mFragment = fragment;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HabitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.habit_item, parent, false);
        return new HabitsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HabitsViewHolder holder, int position) {
        if (mHabits != null && currentWeekHabits != null) {
            Habit current = mHabits.get(position);
            holder.doneCheckBox.setChecked(false);
            if (todayHabitsDone != null && todayHabitsDone.contains(current.getName())) {
                holder.doneCheckBox.setChecked(true);
            }
            holder.titleText.setText(current.getName());
            holder.descText.setText(current.getDescription());
            holder.categoryText.setText(current.getCategory());
            holder.repetitionText.setText(current.getRepetitionLabel());
        } else {
            holder.titleText.setText("No Habit");
        }
    }

    void setHabits(List<Habit> habits){
        mHabits = habits;
        notifyDataSetChanged();
    }

    void filterHabits(List<Habit> habits, Calendar date) {
        cursor = date;
        List<Habit> filterHabits = new ArrayList<>();
        int currentDay = date.get(Calendar.DAY_OF_WEEK);
        int exactDay = date.get(Calendar.DAY_OF_YEAR);
        for (Habit habit : habits) {
            String habitRep = habit.getRepetition();
            if (habitRep.equals("every") || habitRep.equals(String.valueOf(currentDay)) || habitRep.equals(String.valueOf(exactDay))) {
                filterHabits.add(habit);
            }
        }
        Log.d("HAB", String.valueOf(habits.size()));
        setHabits(filterHabits);
    }

    void setWeekHabits(WeekHabits weekHabits) {
        weekHabits.display();
        int currentDay = cursor.get(Calendar.DAY_OF_WEEK);
        List<String>  allHabitsDone =  new ArrayList<>(Arrays.asList(TextUtils.split(weekHabits.getHabitsDone(), ",")));
        Log.d("all", allHabitsDone.toString());
        String habitsDoneStr = allHabitsDone.get(currentDay-1);
        Log.d("today", habitsDoneStr);
        List<String>  habitsDone =  new ArrayList<>(Arrays.asList(TextUtils.split(habitsDoneStr, "&")));
        currentWeekHabits = allHabitsDone;
        todayHabitsDone = habitsDone;
        mWeekHabits = weekHabits;
        Log.d("DONE", habitsDone.toString());
        notifyDataSetChanged();
    }

    WeekHabits filterWeekHabits(List<WeekHabits> weekHabitsList, Calendar date) {
        Log.d("WEEK", String.valueOf(weekHabitsList.size()));
        cursor = date;
        int weekNumber = date.get(Calendar.WEEK_OF_YEAR);
        for (WeekHabits week : weekHabitsList) {
            if (week.getWeekNumber() == weekNumber) {
                setWeekHabits(week);
                return week;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mHabits != null) {
            return mHabits.size();
        }
        else return 0;
    }
}
