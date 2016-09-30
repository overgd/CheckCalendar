package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konifar.fab_transformation.FabTransformation;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarUtils;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarVerticalView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarVerticalViewAdapter;
import com.overflow.overlab.checkcalendar.Goal.GoalActivity;
import com.overflow.overlab.checkcalendar.Goal.GoalSetup;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    /** Main UI **/
    @BindView(R.id.content_main_layout)
    RelativeLayout content_main_layout;

    /** Toolbar **/
    Calendar toolbar_Calendar;
    String toolbar_Goal;
    String toolBar_Title;

    /** Calendar **/
    RecyclerView calendarRecyclerView;
    LinearLayout calendarViewLayout;
    RecyclerView.Adapter calendarRecyclerAdapter;
    RecyclerView.LayoutManager calendarLayoutManager;

    /** Goal Setup Variables **/
    @BindView(R.id.goal_fab)
    FloatingActionButton goal_fab;
    @BindView(R.id.goal_overlay)
    View goal_overlay;
    @BindView(R.id.goal_fab_sheet) View goal_fab_sheet;
    @BindView(R.id.goal_fab_sheet_list_layout)
    ViewGroup goal_fab_sheet_list_layout;
    List<String> goal_subjectList;

    /** Navigation Drawer UI Variables **/
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView accountImageView;
    TextView accountNameView;

    /**
     * OnCreate
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        addCalendarVerticalView();
        initMainUi();
        initGoalFab();
//        addCalendarView();

    }

    /**
     * Calendar Goal FAB Initialize
     */
    @Override
    protected void initGoalFab() {
        super.initGoalFab();

        goal_subjectList =
                new GoalSetup(getApplicationContext()).initGoalSetup();
        if(! goal_subjectList.isEmpty()) {
            toolbar_Goal = goal_subjectList.get(0);
        }
        setToolBarTitle();

        goal_fab_sheet_list_layout.removeAllViews();
        for(int i = 0; i < goal_subjectList.size(); i++) {
            View goal_sheet =
                    LayoutInflater.from(this).inflate(R.layout.goal_sheet, null);
            TextView goal_sheet_text =
                    (TextView) goal_sheet.findViewById(R.id.goal_fab_sheet_list_text);
            goal_sheet_text.setText(goal_subjectList.get(i));
            goal_fab_sheet_list_layout.addView(goal_sheet);
        }

    }

    /**
     * Click Listener
     **/
    @OnClick(R.id.goal_overlay)
    void onClickOverlay() {
        if(goal_fab.getVisibility() != View.VISIBLE) {
            FabTransformation.with(goal_fab)
                    .duration(250)
                    .setOverlay(goal_overlay)
                    .transformFrom(goal_fab_sheet);
        }
    }
    @OnClick(R.id.goal_fab)
    void onClickGoalFab() {
        if(goal_fab.getVisibility() == View.VISIBLE) {
            FabTransformation.with(goal_fab)
                    .duration(250)
                    .setOverlay(goal_overlay)
                    .transformTo(goal_fab_sheet);
        }
    }
    @OnClick(R.id.goal_fab_add_layout)
    void onClickGoalFabSheetAdd() {
        Log.d("goal add", "click");
        Intent goalActivityIntent = new Intent(this, GoalActivity.class);
        FabTransformation.with(goal_fab)
                .duration(100)
                .setOverlay(goal_overlay)
                .transformFrom(goal_fab_sheet);
        startActivityForResult(goalActivityIntent, ADD_GOAL);
    }

    /**
     * Back Key Press Listener
     **/
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (goal_fab.getVisibility() != View.VISIBLE) {
            FabTransformation.with(goal_fab)
                    .setOverlay(goal_overlay)
                    .transformFrom(goal_fab_sheet);
            return;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Create Option Menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    /**
     * Option Menu Item Listener
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Navigation Menu Item Listener
     **/
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_GOAL:
                if(resultCode == RESULT_OK) {
                    initGoalFab();
                }
        }
    }

    /**
     * Initiate Main UI
     **/
    @Override
    protected void initMainUi() {

        super.initMainUi();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(View.VISIBLE);
        accountImageView = (ImageView) headerView.findViewById(R.id.accountImage);
        accountNameView = (TextView) headerView.findViewById(R.id.accountName);

        SharedPreferences settings =
                getPreferences(Context.MODE_PRIVATE);
        accountNameView.setText(settings.getString(PREF_ACCOUNT_NAME, "NULL"));
        Picasso.with(getApplicationContext()).load(settings.getString(PREF_ACCOUNT_IMGURI, "NULL")).into(accountImageView);
    }

    /**
     * Add Vertical Calendar View
     */
    protected void addCalendarVerticalView() {

        appBarLayout.addView(CalendarVerticalView.linearLayoutCalendarWeekUI(this));

        calendarViewLayout = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.calendar_vertical, null);
        calendarViewLayout.setId(View.generateViewId());

        calendarRecyclerView = (RecyclerView) calendarViewLayout
                        .findViewById(R.id.calendar_vertical_recyclerview);

        calendarRecyclerView.setHasFixedSize(true);

        calendarLayoutManager = new LinearLayoutManager(this);
        calendarRecyclerView.setLayoutManager(calendarLayoutManager);

        calendarRecyclerAdapter = new CalendarVerticalViewAdapter();
        calendarRecyclerView.setAdapter(calendarRecyclerAdapter);

        content_main_layout.addView(calendarViewLayout);
        calendarLayoutManager.scrollToPosition(CalendarUtils.POSITION_CURRENT_MONTH());
        calendarRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(position != -1) {
                    toolbar_Calendar =
                            CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(position);
                    setToolBarTitle();
                }
            }
        });
    }

    public void setToolBarTitle() {

        if (toolbar_Calendar == null) {
            toolbar_Calendar = Calendar.getInstance();
        }

        toolBar_Title = getResources().getStringArray(R.array.calendar_month_short)
                [toolbar_Calendar.get(Calendar.MONTH)]
                +" "+toolbar_Calendar.get(Calendar.YEAR);
        if (toolbar_Goal != null) {
            toolBar_Title = toolBar_Title +", "+ toolbar_Goal;
        }

        getSupportActionBar().setTitle(toolBar_Title);
    }

}
