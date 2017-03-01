package in.avanti.app.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.reflect.Field;

import in.avanti.app.R;
import in.avanti.app.learn.LearnFragment;
import in.avanti.app.mission.MissionsFragment;
import in.avanti.app.more.MoreFragment;
import in.avanti.app.progress.ProgressFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //Switching Top Level Fragments upon Bottom Navigation Item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(getNavItemsListener());


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_content, new MissionsFragment());
        transaction.commit();
    }

    /**
     * Get NavigationItemSelecter Listener for Bottom Navigation bar
     * @return OnNavigationItemSelectedListener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener getNavItemsListener() {
        return (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.missions:
                        selectedFragment = new MissionsFragment();
                        break;
                    case R.id.learn:
                        selectedFragment = new LearnFragment();
                        break;
                    case R.id.progress:
                        selectedFragment = new ProgressFragment();
                        break;
                    case R.id.more:
                        selectedFragment = new MoreFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_activity_content, selectedFragment);
                transaction.commit();
                return true;
            }
        });
    }

    public void logout(MenuItem item) {

    }

    public static class BottomNavigationViewHelper {
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", Resources.getSystem().getString(R.string.errorNoShiftMode), e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", Resources.getSystem().getString(R.string.errorCannotChangeShiftMode), e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

}
