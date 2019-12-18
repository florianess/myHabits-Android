package fr.utt.myhabits.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

        homeViewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                adapter.setHabits(habits);
            }
        });
        MaterialButton addButton = root.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText title = root.findViewById(R.id.title);
                TextInputEditText desc = root.findViewById(R.id.desc);
                RadioGroup categoryGroup = root.findViewById(R.id.category);
                RadioButton categoryRadio = root.findViewById(categoryGroup.getCheckedRadioButtonId());
                RadioGroup repetitionGroup = root.findViewById(R.id.repetition);
                RadioButton repetitionRadio = root.findViewById(repetitionGroup.getCheckedRadioButtonId());
                Habit habit = new Habit(
                        title.getText().toString(),
                        desc.getText().toString(),
                        categoryRadio.getText().toString(),
                        repetitionRadio.getText().toString(),
                        repetitionRadio.getText().toString()
                );
                homeViewModel.insert(habit);
                System.out.println(title.getText() + " " + desc.getText() + " " + categoryRadio.getText() + " " + repetitionRadio.getText());
                fab.setExpanded(false);
            }
        });
        return root;
    }
}