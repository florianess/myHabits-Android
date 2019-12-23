package fr.utt.myhabits.ui.stats;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import fr.utt.myhabits.R;

public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statsViewModel =
                ViewModelProviders.of(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final GestureDetectorCompat mDetector = new GestureDetectorCompat(getActivity(), new StatsFragment.CustomGestureListener());
        statsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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