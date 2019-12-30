package fr.utt.myhabits.ui.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import fr.utt.myhabits.R;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statsViewModel =
                ViewModelProviders.of(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        final GestureDetectorCompat mDetector = new GestureDetectorCompat(getActivity(), new StatsFragment.CustomGestureListener());



        LineChartView lineChartView = root.findViewById(R.id.chart);
        String[] axisData = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        int[] yAxisData = {50, 20, 15, 30, 20, 60, 15};
        List yAxisValues = new ArrayList();

        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));


        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Réussite en pourcentage");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        return root;
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
                    System.out.println("TO LEFT");
                } else {
                    System.out.println("TO RIGHT");
                }
            }
            return super.onFling(event1, event2, velocityX, velocityY);
        }
    }
}