package fr.utt.myhabits.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
        }
    }

    private final LayoutInflater mInflater;
    private List<Habit> mHabits;
    private String[] currentWeekHabits;
    private String[] todayHabitsDone;

    public HabitsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public HabitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.habit_item, parent, false);
        return new HabitsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HabitsViewHolder holder, int position) {
        if (mHabits != null && currentWeekHabits != null) {
            Habit current = mHabits.get(position);
            if (todayHabitsDone != null && Arrays.asList(todayHabitsDone).contains(current.getName())) {
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

    void setWeekHabits(WeekHabits weekHabits) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekHabits != null) {
            String[] allHabitsDone = TextUtils.split(weekHabits.getHabitsDone(), ",");
            String habitsDoneStr = allHabitsDone[currentDay-1];
            String[] habitsDone = TextUtils.split(habitsDoneStr, "|");
            currentWeekHabits = allHabitsDone;
            todayHabitsDone = habitsDone;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mHabits != null) {
            return mHabits.size();
        }
        else return 0;
    }
}
