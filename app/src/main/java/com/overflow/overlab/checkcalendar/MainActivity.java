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
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

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

    /** Calendar UI Variables **/
//    Calendar currentCalendar;
//    CalendarPagesAdapter calendarPagesAdapter;
//    ViewPager calendarViewPager;

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

        toolbar_Goal = goal_subjectList.get(0);
        setToolBarTitle();

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
            Intent activityGoalIntent = new Intent(this, GoalActivity.class);
        }
    }
    @OnClick(R.id.goal_fab_add_layout)
    void onClickGoalFabSheetAdd() {
        Log.d("goal add", "click");
        Intent activityGoalIntent = new Intent(this, GoalActivity.class);
        FabTransformation.with(goal_fab)
                .duration(100)
                .setOverlay(goal_overlay)
                .transformFrom(goal_fab_sheet);
        startActivity(activityGoalIntent);

    }
    /**
     * BackKey Press Listener
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
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Option Menu Item Listener
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Navigation Menu Item Listener
     **/
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return super.onNavigationItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                            CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(
                                    position
                            );
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

        if (toolbar_Goal != null && !Objects.equals(toolbar_Goal, GoalSetup.EMPTY)) {
            toolBar_Title = toolBar_Title +", "+ toolbar_Goal;
        }

        getSupportActionBar().setTitle(toolBar_Title);
    }


//    /**
//     * Add Calendar View
//     **/
//    protected void addCalendarView() {
//
//        currentCalendar = Calendar.getInstance(); //현재 날짜
//        int YEAR = currentCalendar.get(Calendar.YEAR) - 1900; // 1900년부터 시작해서 현재 년도까지
//        int MONTH = currentCalendar.get(Calendar.MONTH) + 1; // 0부터 시작되는 월
//        int position = (YEAR * 12) + MONTH; // 1900년 ~ 2200년 사이에 현재 월 위치
//
//        //현재 날짜의 월을 타이틀에 초기화
////        getDelegate().getSupportActionBar().setTitle(
////                getApplicationContext().getResources().getStringArray(R.array.calendar_month)[
////                        currentCalendar.get(Calendar.MONTH)]
////        );
//
////        month_Text.setText(
////                getApplicationContext().getResources()
////                        .getTextArray(R.array.calendar_month)
////                        [currentCalendar.get(Calendar.MONTH)]
////        );
//
//        calendarPagesAdapter = new CalendarPagesAdapter(
//                getSupportFragmentManager(), getBaseContext());
//        calendarViewPager = (ViewPager) findViewById(R.id.calendar_pager);
//        calendarViewPager.setAdapter(calendarPagesAdapter);
//
//        calendarViewPager.setCurrentItem(position);
//        calendarViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                getDelegate().getSupportActionBar()
////                        .setTitle(calendarPagesAdapter.getPageTitle(position));
////                month_Text.setText(
////                        calendarPagesAdapter.getPageTitle(position)
////                );
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
}
