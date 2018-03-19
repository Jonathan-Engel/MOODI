package teammoodi.moodi;

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

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.FileWriter;
import java.io.IOException;

import teammoodi.moodi.SettingsFragment.OnSettingsFragmentInteractionListener;

public class Dashboard extends AppCompatActivity
        implements SettingsFragment.OnSettingsFragmentInteractionListener, StatisticsFragment.OnStatisticsFragmentInteractionListener, RecordFragment.OnRecordFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*
        Listen for wear sending a message
        */

        Wearable.getMessageClient(this).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent) {
                Log.d("MESSAGE_RECEIVED", "Got message");
                Log.d("MESSAGE_RECEIVED", "MessageEvent - getData() - " +
                        messageEvent.getData().toString());
                Log.d("MESSAGE_RECEIVED", "MessageEvent - getPath() - " +
                        messageEvent.getPath());
                Uri uri = Uri.parse(messageEvent.getPath());
                try
                {
                    MediaStore.Audio.Media.getContentUriForPath(messageEvent.getPath());
                    FileWriter fw = new FileWriter(
                            Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/WearAudio.mp4");
                    fw.write(uri.toString());
                    fw.close();
                }catch (IOException e)
                {
                    Log.e("FILE_WRITER", "IO Exception - " + e);
                }
            }
        })
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("MESSAGE_LISTENER", "Message listener complete");
            }
        });

        /* Done with wear messaging */

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

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch(menuItem.getItemId()) {
                            case R.id.nav_dash:
                                ChangeFragment(new RecordFragment());
                                break;
                            case R.id.nav_statistics:
                                ChangeFragment(new StatisticsFragment());
                                break;
                            case R.id.nav_settings:
                                ChangeFragment(new SettingsFragment());
                                break;
                            case R.id.nav_logout:
                                LogoutConfirmationFragment cf = new LogoutConfirmationFragment();
                                //
                                // This anon class implements the ILogoutListener
                                //
                                cf.SetListener(new LogoutConfirmationFragment.ILogoutListener() {
                                    @Override
                                    public void Logout() {
                                        //
                                        //The actual code to run given a positive dialog
                                        //
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
}