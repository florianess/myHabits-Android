package fr.utt.myhabits.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HabitsListAdapter adapter;
    private TextInputEditText title;
    private TextInputEditText desc;
    private RadioGroup categoryGroup;
    private RadioGroup repetitionGroup;
    private WeekHabits currentWeekHabits;
    private List<Habit> mAllHabits;
    private LocalDate cursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final GestureDetectorCompat mDetector = new GestureDetectorCompat(getActivity(), new CustomGestureListener());

        title = root.findViewById(R.id.title);
        desc = root.findViewById(R.id.desc);
        categoryGroup = root.findViewById(R.id.category);
        repetitionGroup = root.findViewById(R.id.repetition);
        RecyclerView recyclerView = root.findViewById(R.id.habitsList);
        cursor = LocalDate.now();
        getActivity().setTitle("Aujourd'hui");

        adapter = new HabitsListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final FloatingActionButton fab = root.findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setExpanded(true);
            }
        });
        homeViewModel.currentWeek().observe(this, new Observer<WeekHabits>() {
            @Override
            public void onChanged(WeekHabits weekHabits) {
                currentWeekHabits = weekHabits;
                adapter.setWeekHabits(weekHabits);
            }
        });
        homeViewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                mAllHabits = habits;
                adapter.filterHabits(habits, cursor);
            }
        });

        MaterialButton addButton = root.findViewById(R.id.button_add);
        addButton.setOnClickListener(view -> {
            RadioButton categoryRadio = root.findViewById(categoryGroup.getCheckedRadioButtonId());
            RadioButton repetitionRadio = root.findViewById(repetitionGroup.getCheckedRadioButtonId());
            Calendar calendar = Calendar.getInstance();
            String repetitionLabel = repetitionRadio.getText().toString();
            String category = categoryRadio.getText().toString();
            String repetition = "every";
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (repetitionLabel.equals("Hebdomadaire")) {
                repetition = String.valueOf(currentDay);
            } else if (repetitionLabel.equals("Unique")) {
                repetition = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
            }
            Habit habit = new Habit(
                    title.getText().toString(),
                    desc.getText().toString(),
                    category,
                    repetitionLabel,
                    repetition
            );
            if (currentWeekHabits == null) {
                int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                String[] totalHabits = new String[] {"0", "0", "0", "0", "0", "0", "0"};
                String[] habitsDone = new String[] {"", "", "", "", "", "", ""};
                totalHabits[currentDay-1] = "1";
                WeekHabits weekHabits = new WeekHabits(
                        weekNumber,
                        TextUtils.join(",", totalHabits),
                        TextUtils.join(",", habitsDone)
                );
                homeViewModel.insertWeekHabits(weekHabits);
            }
            homeViewModel.insertHabit(habit);
            Log.d("NEW HABIT", title.getText() + " " + desc.getText() + " " + category + " " + repetitionLabel + " " + repetition);
            fab.setExpanded(false);
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        return root;
    }

    static public void filterHabits(List<Habit> habits) {

    }

    class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
        private int MIN_SWIPE = 100;
        private int MAX_SWIPE = 1000;

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float deltaX = event1.getX() - event2.getX();
            float deltaXabs = Math.abs(deltaX);
            if (deltaXabs >= MIN_SWIPE && deltaXabs < MAX_SWIPE) {
                if (deltaX > 0) {
                    cursor = cursor.plusDays(1);
                } else {
                    cursor = cursor.minusDays(1);
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM");
            String title = formatter.format(cursor);
            if (cursor.equals(LocalDate.now())) {
                title = "Aujourd'hui";
            }
            getActivity().setTitle(title);
            adapter.filterHabits(mAllHabits, cursor);

            return super.onFling(event1, event2, velocityX, velocityY);
        }
    }
}