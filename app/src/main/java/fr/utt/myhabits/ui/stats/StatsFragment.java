package fr.utt.myhabits.ui.stats;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.utt.myhabits.R;
import fr.utt.myhabits.data.entities.Habit;
import fr.utt.myhabits.data.entities.WeekHabits;

public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView textWeekNumber;
    private TextView textDate;
    private TextView textTotal;
    private TextView textDone;
    private List<WeekHabits> mAllWeekHabits;
    private List<Habit> mAllHabits;
    private WeekHabits currentWeekHabits;
    private Calendar cursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        getActivity().setTitle("Statistique");
        pieChart = root.findViewById(R.id.pie_chart);
        textWeekNumber = root.findViewById(R.id.text_weeknumber);
        textDate = root.findViewById(R.id.text_date);
        textTotal = root.findViewById(R.id.text_total);
        textDone = root.findViewById(R.id.text_done);
        barChart = root.findViewById(R.id.bar_chart);
        cursor = Calendar.getInstance();

        barChart.setDrawBarShadow(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setPinchZoom(false);
        barChart.setDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getAxisLeft().setGranularity(1f);
        barChart.getAxisLeft().setAxisMinimum(0f);


        pieChart.setRotationEnabled(false);

        final GestureDetectorCompat mDetector = new GestureDetectorCompat(getActivity(), new StatsFragment.CustomGestureListener());
        statsViewModel.getAllWeekHabits().observe(this, new Observer<List<WeekHabits>>() {
            @Override
            public void onChanged(List<WeekHabits> listWeekHabits) {
                mAllWeekHabits = listWeekHabits;
                filterWeekHabits();
            }
        });

        statsViewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                mAllHabits = habits;
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        return root;
    }

    private void filterWeekHabits() {
        Log.d("WEEK", String.valueOf(mAllWeekHabits.size()));
        int weekNumber = cursor.get(Calendar.WEEK_OF_YEAR);
        Log.d("NUMBER", String.valueOf(weekNumber));
        WeekHabits newCurrentWeek = null;
        for (WeekHabits week : mAllWeekHabits) {
            if (week.getWeekNumber() == weekNumber) {
                week.display();
                newCurrentWeek = week;
            }
        }
        if (newCurrentWeek != null) {
            currentWeekHabits = newCurrentWeek;
            renderCharts();
        } else {
            statsViewModel.createWeek(weekNumber, mAllHabits);
        }
    }

    public void renderCharts() {
        Log.d("RENDER", "CHARTS");
        List<String>  allWeekHabits =  new ArrayList<>(Arrays.asList(TextUtils.split(currentWeekHabits.getTotalHabits(), ",")));
        List<String>  allWeekHabitsDone =  new ArrayList<>(Arrays.asList(TextUtils.split(currentWeekHabits.getHabitsDone(), ",")));
        Float totalHabits = 0f;
        for (String dayHabits : allWeekHabits) {
            totalHabits = totalHabits + Float.valueOf(dayHabits);
        }
        Float totalHabitsDone = 0f;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < allWeekHabits.size(); i++) {
            List<String> dayDone = new ArrayList<>(Arrays.asList(TextUtils.split(allWeekHabitsDone.get(i), "&")));
            totalHabitsDone = totalHabitsDone + dayDone.size();
            barEntries.add(new BarEntry(i, dayDone.size()));
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("total", String.valueOf(totalHabits));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar sun = Calendar.getInstance();
        sun.set(Calendar.WEEK_OF_YEAR, currentWeekHabits.getWeekNumber() - 1);
        sun.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Calendar sat = Calendar.getInstance();
        sat.set(Calendar.WEEK_OF_YEAR, currentWeekHabits.getWeekNumber());
        sat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        textWeekNumber.setText("Semaine " + currentWeekHabits.getWeekNumber());
        textTotal.setText("Total habitudes : " + Math.round(totalHabits));
        textDone.setText("Habitudes réalisées : " + Math.round(totalHabitsDone));
        textDate.setText("Du " + sdf.format(sun.getTime()) + " au " + sdf.format(sat.getTime()));

        entries.add(new PieEntry(totalHabitsDone, "Habitudes réalisées"));
        entries.add(new PieEntry(totalHabits - totalHabitsDone, "Habitudes non réalisées"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData((pieDataSet));
        pieData.setValueFormatter(new IntFormatter());
        pieChart.setData(pieData);
        pieChart.animateXY(1000, 1000, Easing.EaseInOutCubic);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Habitudes réalisées par jour");
        barDataSet.setColor(getResources().getColor(R.color.Sport));
        BarData barData = new BarData((barDataSet));
        barData.setValueFormatter(new IntFormatter());
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new CustomXAxis());
        barChart.animateY(1000, Easing.EaseInOutCubic);

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
                    cursor.add(Calendar.WEEK_OF_YEAR, 1);
                } else {
                    cursor.add(Calendar.WEEK_OF_YEAR, -1);
                }
            }
            filterWeekHabits();
            return super.onFling(event1, event2, velocityX, velocityY);
        }
    }

    class CustomXAxis extends IndexAxisValueFormatter {
        String[] days = {"Dim","Lun","Mar","Mer","Jeu","Ven","Sam"};
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return days[Math.round(value)];
        }
    }

    class IntFormatter extends ValueFormatter {
        @Override
        public String getBarLabel(BarEntry barEntry) {
            return String.valueOf(Math.round(barEntry.getY()));
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            return String.valueOf(Math.round(value));
        }
    }
}