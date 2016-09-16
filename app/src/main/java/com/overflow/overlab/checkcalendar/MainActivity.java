package com.overflow.overlab.checkcalendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.calendar.model.ColorDefinition;
import com.google.api.services.calendar.model.Colors;
import com.konifar.fab_transformation.FabTransformation;
import com.overflow.overlab.checkcalendar.Goal.GoalSetup;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        NavigationView.OnNavigationItemSelectedListener,
GoogleApiClient.OnConnectionFailedListener {

    CheckCalendarApplication applicationClass;

    /**
     * Request Code
     **/
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int RC_SIGN_IN = 1004;

    /**
     * Navigation Drawer UI Variables
     **/
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView accountImageView;
    private TextView accountNameView;

    /**
     * Goal Setup Variables
     **/
    @BindView(R.id.goal_fab) FloatingActionButton goal_fab;
    @BindView(R.id.goal_overlay) View goal_overlay;
    @BindView(R.id.goal_sheet) View goal_sheet;

    /**
     * Calendar UI Variables
     **/
    @BindView(R.id.month_textview) TextView month_Text;
    private Calendar currentCalendar;
    private CalendarPagesAdapter calendarPagesAdapter;
    private ViewPager calendarViewPager;

    /**
     * Google Sign-in Account API Variables
     **/
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Statement
     **/
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String PREF_ACCOUNT_IMGURI = "accountImgURI";

    /**
     * Test Variables
     **/
    private TextView mOutputText;

    /**
     * OnCreate
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        applicationClass = (CheckCalendarApplication) getApplicationContext();
        applicationClass.setMainActivity(this);
        applicationClass.setGoogleAccountCredential();
        applicationClass.setCalendarService(applicationClass.mCredential);

        initMainUi();
        setCalendarColors();
        addCalendarView();
        getResultsFromApi();
        initGoalFab();

        super.onCreate(savedInstanceState);


        //Status
        mOutputText = (TextView) findViewById(R.id.OutPutText);
    }

    /**
     * Calendar Goal FAB Initialize
     */
    private void initGoalFab() {

        getSupportActionBar().setTitle(
                new GoalSetup(getApplicationContext()).initGoalSetup()
        );

    }

    /**
     * Calendar Colors
     **/
    public void setCalendarColors() {

        AsyncTask<Void, Void, Void> colorTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Colors", Context.MODE_PRIVATE);

                if (!sharedPref.contains("Calendar.Color.1")) {
                    SharedPreferences.Editor spEditor = sharedPref.edit();
                    try {
                        Colors colors = applicationClass.mCalendarService.colors().get().execute();
                        // Print available calendar list entry colors
                        for (Map.Entry<String, ColorDefinition> color : colors.getCalendar().entrySet()) {
                            spEditor.putString("Calendar.Color." + color.getKey(), color.getValue().getBackground());
                        }
                        // Print available event colors
                        for (Map.Entry<String, ColorDefinition> color : colors.getEvent().entrySet()) {
                            spEditor.putString("Event.Color." + color.getKey(), color.getValue().getBackground());
                        }
                        spEditor.apply();

                    } catch (Exception e) {
                        Log.d("Colors Error", e.toString());
                    }

                }
                return null;
            }
        };

        colorTask.execute();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                    .transformFrom(goal_sheet);
        }
    }
    @OnClick(R.id.goal_fab)
    void onClickGoalFab() {
        if(goal_fab.getVisibility() == View.VISIBLE) {
            FabTransformation.with(goal_fab)
                    .duration(250)
                    .setOverlay(goal_overlay)
                    .transformTo(goal_sheet);
        }
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
            FabTransformation.with(goal_fab).setOverlay(goal_overlay).transformFrom(goal_sheet);
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

    /**
     * EasyPermission
     **/
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Using EasyPermission
     **/
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS) //Acccount Permission Get
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);

            if (accountName != null) {
                applicationClass.mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                startActivityForResult(applicationClass.mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    "", REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_GOOGLE_PLAY_SERVICES:

                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;

            case REQUEST_ACCOUNT_PICKER:

                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        applicationClass.mCredential.setSelectedAccountName(accountName);

                        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .setAccountName(accountName)
                                .build();

                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                                .build();

                        getResultsFromApi();
                    }
                }
                break;

            case RC_SIGN_IN:

                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = signInResult.getSignInAccount();

                SharedPreferences settings =
                        getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_IMGURI, acct.getPhotoUrl().toString());
                editor.apply();

                break;

            case REQUEST_AUTHORIZATION:

                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }

                break;

        }
    }

    /**
     * Get Result From Calendar API
     **/
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) { //플레이 서비스 사용권한 얻기
            acquireGooglePlayServices();
        } else if (applicationClass.mCredential.getSelectedAccountName() == null) { //계정 고르기
            chooseAccount();
        }
        else if (! isDeviceOnline()) { //네트워크 연결 확인
            mOutputText.setText("네트워크가 연결되지 않았습니다.");
        }
        else {
            try {
//                addCalendarView();
                signIn();
            } catch (Exception e) {
                startActivityForResult(getIntent(), REQUEST_AUTHORIZATION);
            }
        }
    }
    /**
     * Sign In
     */
    private void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }
    /**
     * Check Device Online
     **/
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check GooglePlayService
     **/
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Acquire GooglePlayService
     **/
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Show Error GooglePlayService
     **/
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(MainActivity.this, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Initiate Main UI
     **/
    private void initMainUi() {

        /* Toolbar Setup */
        setSupportActionBar(toolbar);

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
     * Add Calendar View
     **/
    private void addCalendarView() {

        currentCalendar = Calendar.getInstance(); //현재 날짜
        int YEAR = currentCalendar.get(Calendar.YEAR) - 1900; // 1900년부터 시작해서 현재 년도까지
        int MONTH = currentCalendar.get(Calendar.MONTH) + 1; // 0부터 시작되는 월
        int position = (YEAR * 12) + MONTH; // 1900년 ~ 2200년 사이에 현재 월 위치

        //현재 날짜의 월을 타이틀에 초기화
//        getDelegate().getSupportActionBar().setTitle(
//                getApplicationContext().getResources().getStringArray(R.array.calendar_month)[
//                        currentCalendar.get(Calendar.MONTH)]
//        );
        month_Text.setText(
                getApplicationContext().getResources()
                        .getTextArray(R.array.calendar_month)
                        [currentCalendar.get(Calendar.MONTH)]
        );

        calendarPagesAdapter = new CalendarPagesAdapter(
                getSupportFragmentManager(), getBaseContext());
        calendarViewPager = (ViewPager) findViewById(R.id.calendar_pager);
        calendarViewPager.setAdapter(calendarPagesAdapter);

        calendarViewPager.setCurrentItem(position);
        calendarViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                getDelegate().getSupportActionBar()
//                        .setTitle(calendarPagesAdapter.getPageTitle(position));
                month_Text.setText(
                        calendarPagesAdapter.getPageTitle(position)
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
