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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.MainActivity;
import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HomeFragment extends Fragment implements MainActivity.OnBackPressedListener {

    private HomeViewModel homeViewModel;
    private HabitsListAdapter adapter;
    private TextInputEditText title;
    private TextInputEditText desc;
    private RadioGroup categoryGroup;
    private RadioGroup repetitionGroup;
    private FloatingActionButton fab;
    private WeekHabits currentWeekHabits;
    private List<Habit> mAllHabits;
    private List<WeekHabits> mAllWeekHabits;
    private Calendar cursor;

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
        cursor = Calendar.getInstance();
        getActivity().setTitle("Aujourd'hui");

        adapter = new HabitsListAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = root.findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setTitle("Ajouter une habitude");
                fab.setExpanded(true);
            }
        });
        homeViewModel.getAllWeekHabits().observe(this, new Observer<List<WeekHabits>>() {
            @Override
            public void onChanged(List<WeekHabits> listWeekHabits) {
                mAllWeekHabits = listWeekHabits;
                currentWeekHabits = adapter.filterWeekHabits(listWeekHabits, cursor);
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
                List<String> totalHabits = new ArrayList<>();
                List<String> habitsDone = new ArrayList<>();
                Integer dailyHabits = 0;
                for (Habit dhabit : mAllHabits) {
                    if (dhabit.getRepetition().equals("every")) {
                        dailyHabits ++;
                    }
                }
                if (repetition.equals("every")) {
                    dailyHabits ++;
                }
                for (int i = 0; i < 7; i++) {
                    totalHabits.add(String.valueOf(dailyHabits));
                    habitsDone.add("");
                }
                if (!repetition.equals("every")) {
                    totalHabits.set(currentDay, String.valueOf(dailyHabits+1));
                }
                WeekHabits weekHabits = new WeekHabits(
                        weekNumber,
                        TextUtils.join(",", totalHabits),
                        TextUtils.join(",", habitsDone)
                );
                homeViewModel.insertWeekHabits(weekHabits);
            }
            homeViewModel.insertHabit(habit);
            Log.d("NEW HABIT", title.getText() + " " + desc.getText() + " " + category + " " + repetitionLabel + " " + repetition);
            setTitle();
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

    public void setTitle() {
        SimpleDateFormat formatter = new SimpleDateFormat("E dd MMMM");
        String title = formatter.format(cursor.getTime());
        if (cursor.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            title = "Aujourd'hui";
        }
        getActivity().setTitle(title);
    }

    @Override
    public boolean onBackPressed() {
        fab.setExpanded(false);
        setTitle();
        return true;
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
                    cursor.add(Calendar.DAY_OF_MONTH, 1);
                } else {
                    cursor.add(Calendar.DAY_OF_MONTH, -1);
                }
            }
            setTitle();
            adapter.filterHabits(mAllHabits, cursor);
            adapter.filterWeekHabits(mAllWeekHabits, cursor);

            return super.onFling(event1, event2, velocityX, velocityY);
        }
    }
}