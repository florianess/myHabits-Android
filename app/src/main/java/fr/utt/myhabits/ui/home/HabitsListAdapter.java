package fr.utt.myhabits.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HabitsListAdapter extends RecyclerView.Adapter<HabitsListAdapter.HabitsViewHolder> {

    class HabitsViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView descText;
        private final TextView categoryText;
        private final TextView repetitionText;

        public HabitsViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleHabit);
            descText = itemView.findViewById(R.id.descHabit);
            categoryText = itemView.findViewById(R.id.categoryHabit);
            repetitionText = itemView.findViewById(R.id.repetitionHabit);
        }
    }

    private final LayoutInflater mInflater;
    private List<Habit> mHabits;
    private WeekHabits mWeekHabits;

    public HabitsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public HabitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.habit_item, parent, false);
        return new HabitsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HabitsViewHolder holder, int position) {
        if (mHabits != null) {
            Habit current = mHabits.get(position);
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
        mWeekHabits = weekHabits;
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
