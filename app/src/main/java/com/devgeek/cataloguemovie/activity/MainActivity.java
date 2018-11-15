package com.devgeek.cataloguemovie.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.devgeek.cataloguemovie.AppPreferences;
import com.devgeek.cataloguemovie.R;
import com.devgeek.cataloguemovie.adapter.PagerAdapter;
import com.devgeek.cataloguemovie.alarmmanager.AlarmPreference;
import com.devgeek.cataloguemovie.alarmmanager.DailyReminderReceiver;
import com.devgeek.cataloguemovie.alarmmanager.ReleaseReminderReceiver;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = MainActivity.class.getSimpleName();

    CircleImageView profileCircleImageView;

    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;

    private AlarmPreference dailyReminderPreference, releaseReminderPreference;
    private DailyReminderReceiver dailyReminderReceiver;
    private ReleaseReminderReceiver releaseReminderReceiver;

    private AppPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.title_activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tl_main);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.home));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.now_playing));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.upcoming));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.favourite));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileCircleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);

        Picasso.with(MainActivity.this)
                .load(R.drawable.profile)
                .into(profileCircleImageView);

        navigationView.setNavigationItemSelectedListener(this);

        preferences = new AppPreferences(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        boolean firstRun = preferences.getFirstTime("first_run");

        if (firstRun) {
            // Launch Daily Reminder Alarm Manager
            dailyReminderPreference = new AlarmPreference(this);
            dailyReminderReceiver = new DailyReminderReceiver();

            dailyReminderPreference.setRepeatingTime("07:00");
            dailyReminderPreference.setRepeatingMessage("Check new movies in Catalogue Movie, don't miss them!");

            dailyReminderReceiver.setRepeatingAlarm(this, DailyReminderReceiver.TYPE_REPEATING, dailyReminderPreference.getRepeatingTime(), dailyReminderPreference.getRepeatingMessage());

            // Launch Upcoming Movie Scheduler
            releaseReminderPreference = new AlarmPreference(this);
            releaseReminderReceiver = new ReleaseReminderReceiver();

            releaseReminderPreference.setRepeatingTime("08:00");
            releaseReminderPreference.setRepeatingMessage("Release Today Reminder");

            releaseReminderReceiver.setRepeatingAlarm(this, ReleaseReminderReceiver.TYPE_REPEATING, releaseReminderPreference.getRepeatingTime(), releaseReminderPreference.getRepeatingMessage());
            preferences.setFirtTime("first_run", false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        drawer.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent mIntent = new Intent(this, SettingsActivity.class);

            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int tab = 0;

        if (id == R.id.nav_home) {
            tab = 0;
        } else if (id == R.id.nav_now_playing) {
            tab = 1;
        } else if (id == R.id.nav_upcoming) {
            tab = 2;
        } else if (id == R.id.nav_favourite) {
            tab = 3;
        }

        viewPager.setCurrentItem(tab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
