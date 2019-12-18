package fr.utt.myhabits;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import fr.utt.myhabits.ui.home.HomeFragment;
import fr.utt.myhabits.ui.stats.StatsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavItem);
            showFragment(new HomeFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
                .addToBackStack(null)
            .commit();
    }

    @Override
    public void onBackPressed() {
        FloatingActionButton fab = findViewById(R.id.floating_add);
        if (fab != null) {
            fab.setExpanded(false);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavItem
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new HomeFragment());
                    return true;
                case R.id.navigation_stats:
                    showFragment(new StatsFragment());
                    return true;
            }
            return false;
        }

    };

}
