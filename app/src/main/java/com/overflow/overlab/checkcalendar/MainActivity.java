package com.overflow.overlab.checkcalendar;

import android.app.DialogFragment;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.konifar.fab_transformation.FabTransformation;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarDayTextView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarRecyclerViewAdapter;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarUtils;
import com.overflow.overlab.checkcalendar.Check.AddCheckViewAsyncTask;
import com.overflow.overlab.checkcalendar.Check.CheckDialogFragment;
import com.overflow.overlab.checkcalendar.Goal.GoalActivity;
import com.overflow.overlab.checkcalendar.Goal.GoalSetup;
import com.overflow.overlab.checkcalendar.Goal.GoalSheetView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, View.OnTouchListener, CheckDialogFragment.DialogListener {

    public static final int CHECK_DIALOG_OK = 10004;

    /** Main UI **/
    @BindView(R.id.content_main_layout)
    LinearLayout content_main_layout;

    /** Toolbar **/
    Calendar current_calendar;
    String toolbar_goal;
    String toolBar_title;

    /** Calendar **/
    int month_position;
    RecyclerView calendarRecyclerView;
    RecyclerView.Adapter calendarRecyclerAdapter;
    RecyclerView.LayoutManager calendarLayoutManager;
    CalendarConstraintView currentCalendarConstraintView;

    /** Goal Setup Variables **/
    @BindView(R.id.goal_fab)
    FloatingActionButton goal_fab;
    @BindView(R.id.goal_overlay)
    View goal_overlay;
    @BindView(R.id.goal_fab_sheet)
    View goal_fab_sheet;
    @BindView(R.id.goal_fab_sheet_list_layout)
    ViewGroup goal_fab_sheet_list_layout;
    List<String> goal_id_list;

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

    }

    protected void initCheckView() {

    }

    /**
     * Calendar Goal FAB Initialize
     */
    @Override
    protected void initGoalFab() {
        super.initGoalFab();

        goal_id_list = new GoalSetup(getApplicationContext()).initGoalSetup();
        setToolBarTitle();

        goal_fab_sheet_list_layout.removeAllViews();

        for(int i = 0; i < goal_id_list.size(); i++) {

            if(goal_id_list.get(i) == null || goal_id_list.get(i).equals(applicationClass.NULL)) break;

            GoalSheetView goal_sheet = new GoalSheetView(this);
            goal_sheet.setGoalText(applicationClass.getGoalSummary(goal_id_list.get(i)));
            goal_sheet.setGoalId(goal_id_list.get(i));
            goal_fab_sheet_list_layout.addView(goal_sheet);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    /**
     * Click Listener
     **/
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.goal_fab_sheet_layout) {
            for(String id : goal_id_list) {
                if(((GoalSheetView) v.getParent()).getGoalId().equals(id)) {
                    applicationClass.setCurrentGoal(id, applicationClass.getGoalSummary(id));
                    FabTransformation.with(goal_fab)
                            .duration(250)
                            .setOverlay(goal_overlay)
                            .transformFrom(goal_fab_sheet);
                    setToolBarTitle();
                }
            }
        } else if(v instanceof CalendarDayTextView) {

            Bundle args = new Bundle();
            args.putLong(CALENDAR_LONG, ((CalendarDayTextView) v).calendar.getTimeInMillis());
            args.putString(GOAL_ID, applicationClass.getCurrentGoal()[0]);
            args.putString(GOAL_SUMMARY, applicationClass.getCurrentGoal()[1]);

            CheckDialogFragment checkDialogFragment = new CheckDialogFragment();
            checkDialogFragment.setArguments(args);
            checkDialogFragment.show(getFragmentManager(), "CalendarCheckDialog");

        }
    }
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
        FabTransformation.with(goal_fab)
                .duration(100)
                .setOverlay(goal_overlay)
                .transformFrom(goal_fab_sheet);
        Intent goalActivityIntent = new Intent(this, GoalActivity.class);
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

        switch (id) {
            case R.id.action_add_goal :
                Intent goalActivityIntent = new Intent(this, GoalActivity.class);
                startActivityForResult(goalActivityIntent, ADD_GOAL);
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
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(this, "Result : OK ", Toast.LENGTH_SHORT).show();
        new AddCheckViewAsyncTask(currentCalendarConstraintView).execute();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

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
        Picasso.with(getApplicationContext())
                .load(settings.getString(PREF_ACCOUNT_IMGURI, "NULL")).into(accountImageView);
    }

    /**
     * Add Vertical Calendar View
     */
    protected void addCalendarVerticalView() {

        appBarLayout.addView(CalendarConstraintView.linearLayoutCalendarWeekUI(this));

        calendarRecyclerView = (RecyclerView) LayoutInflater.from(this)
                .inflate(R.layout.calendar_vertical, content_main_layout, false);

        calendarRecyclerView.setHasFixedSize(true);

        calendarLayoutManager = new LinearLayoutManager(this);

        calendarRecyclerView.setLayoutManager(calendarLayoutManager);

        calendarRecyclerAdapter = new CalendarRecyclerViewAdapter();
        calendarRecyclerView.setAdapter(calendarRecyclerAdapter);

        content_main_layout.addView(calendarRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        calendarLayoutManager.scrollToPosition(CalendarUtils.POSITION_CURRENT_MONTH());
        calendarRecyclerView.addOnScrollListener(new calendarRecyclerViewScrollListener());

    }

    private class calendarRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        int state = 3;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            state = newState;
            LinearLayoutManager linearLayoutManager =
                    (LinearLayoutManager) recyclerView.getLayoutManager();
            if(newState == 0) {

                linearLayoutManager.smoothScrollToPosition(recyclerView, null, month_position);
                initCheckView();
//                linearLayoutManager.scrollToPositionWithOffset(month_position, 0);
//                recyclerView.smoothScrollToPosition(month_position);
                executeCheckTask(recyclerView);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager =
                    (LinearLayoutManager) recyclerView.getLayoutManager();
            int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if(position != -1 && position != month_position) {
                    Log.d("position", String.valueOf(position));
                    month_position = position;
                    current_calendar =
                            CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(month_position);
                    setToolBarTitle();
            }

            if(state == 3) {
                executeCheckTask(recyclerView);
            }

        }

        public void executeCheckTask(RecyclerView recyclerView) {
            CalendarRecyclerViewAdapter.ViewHolder viewHolder =
                    (CalendarRecyclerViewAdapter.ViewHolder) recyclerView
                            .findViewHolderForLayoutPosition(month_position);
            if(viewHolder.view != null) {
                currentCalendarConstraintView = viewHolder.view;
            }
        }

    }

    private void refreshCalendarVerticalView() {

    }

    public void setToolBarTitle() {

        if (current_calendar == null) {
            current_calendar = Calendar.getInstance();
        }

        if(! applicationClass.getCurrentGoal()[1].equals(applicationClass.NULL)) {
            toolbar_goal = applicationClass.getCurrentGoal()[1];
        }

        toolBar_title = getResources().getStringArray(R.array.calendar_month_short)
                [current_calendar.get(Calendar.MONTH)]
                +" "+ current_calendar.get(Calendar.YEAR);
        if (toolbar_goal != null) {
            toolBar_title = toolBar_title +", "+ toolbar_goal;
        }

        getSupportActionBar().setTitle(toolBar_title);
    }

}
