package fr.utt.myhabits.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextInputEditText title;
    private TextInputEditText desc;
    private RadioGroup categoryGroup;
    private RadioGroup repetitionGroup;
    private WeekHabits currentWeekHabits;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        title = root.findViewById(R.id.title);
        desc = root.findViewById(R.id.desc);
        categoryGroup = root.findViewById(R.id.category);
        repetitionGroup = root.findViewById(R.id.repetition);
        RecyclerView recyclerView = root.findViewById(R.id.habitsList);
        final HabitsListAdapter adapter = new HabitsListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final FloatingActionButton fab = root.findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setExpanded(true);
            }
        });

        homeViewModel.getTodayHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                adapter.setHabits(habits);
            }
        });
        homeViewModel.currentWeek().observe(this, new Observer<WeekHabits>() {
            @Override
            public void onChanged(WeekHabits weekHabits) {
                currentWeekHabits = weekHabits;
                adapter.setWeekHabits(weekHabits);
            }
        });
        MaterialButton addButton = root.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton categoryRadio = root.findViewById(categoryGroup.getCheckedRadioButtonId());
                RadioButton repetitionRadio = root.findViewById(repetitionGroup.getCheckedRadioButtonId());
                Calendar calendar = Calendar.getInstance();
                String repetitionLabel = repetitionRadio.getText().toString();
                String category = categoryRadio.getText().toString();
                String repetition = "every";
                int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
                if (repetitionLabel == "Hebdomadaire") {
                    repetition = String.valueOf(currentDay);
                } else if (repetitionLabel == "Unique") {
                    repetition = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
                }
                Habit habit = new Habit(
                        title.getText().toString(),
                        desc.getText().toString(),
                        category,
                        repetitionLabel,
                        repetition
                );
                homeViewModel.insertHabit(habit);
                System.out.println(title.getText() + " " + desc.getText() + " " + categoryRadio.getText() + " " + repetitionRadio.getText());
                fab.setExpanded(false);
            }
        });
        return root;
    }
}