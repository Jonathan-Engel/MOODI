package teammoodi.moodi;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.Intent;

public class Dashboard extends AppCompatActivity
        implements SettingsFragment.OnSettingsFragmentInteractionListener,
        StatisticsFragment.OnStatisticsFragmentInteractionListener, HistoryFragment.OnHistoryFragmentInteractionListener,
        RecordFragment.OnRecordFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch(menuItem.getItemId()) {
                            case R.id.nav_dash:
                                ChangeFragment(new RecordFragment());
                                getSupportActionBar().setTitle("Record");
                                break;
                            case R.id.nav_history:
                                ChangeFragment(new HistoryFragment());
                                getSupportActionBar().setTitle("History");
                                break;
                            case R.id.nav_statistics:
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.moodi-app.com")));
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case R.id.nav_settings:
                                ChangeFragment(new SettingsFragment());
                                getSupportActionBar().setTitle("Settings");
                                break;
                            case R.id.nav_logout:
                                LogoutConfirmationFragment cf = new LogoutConfirmationFragment();
                                //
                                // This anon class implements the ILogoutListener
                                //
                                cf.SetListener(new LogoutConfirmationFragment.ILogoutListener() {
                                    @Override
                                    public void Logout() {
                                        //The actual code to run given a positive dialog
                                        PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
                                        cookieStore.removeAll();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {
                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);
                                cf.show(getFragmentManager(), "dialog");
                                break;

                        }
                        return true;
                    }});

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null)
            return;
        else {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RecordFragment()).commit();
            getSupportActionBar().setTitle("Record");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ChangeFragment(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, f).commit();
    }

    @Override
    public void onStatisticsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSettingsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onHistoryFragmentInteraction(Uri uri) {

    }
}