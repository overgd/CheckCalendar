package com.overflow.overlab.checkcalendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.calendar.model.ColorDefinition;
import com.google.api.services.calendar.model.Colors;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by over on 9/25/2016.
 */

public class BaseActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /** Application Class */
    CheckCalendarApplication applicationClass;

    /** Request Code **/
    public static final String REQUEST_CODE = "requestCode";
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int RC_SIGN_IN = 1004;

    public static final int ADD_GOAL = 10000;
    public static final int EDIT_GOAL = 10001;

    /** Navigation Drawer UI Variables **/
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    public AppBarLayout appBarLayout;

    /** Google Sign-in Account API Variables **/
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    /** Statement **/
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String PREF_ACCOUNT_IMGURI = "accountImgURI";
    static final String CALENDAR_LONG = "calendar_long";
    static final String GOAL_ID = "goal_id";
    static final String GOAL_SUMMARY = "goal_summary";
    static final String PARENT_ID = "parent_id";

    /**
     * OnCreate
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ButterKnife.bind(this);

        applicationClass = (CheckCalendarApplication) getApplicationContext();
        applicationClass.setMainActivity(this);
        applicationClass.setGoogleAccountCredential();
        applicationClass.setCalendarService(applicationClass.mCredential);

//        getResultsFromApi();
//        setCalendarColors();
//        addCalendarView();

        super.onCreate(savedInstanceState);

    }

    /**
     * Calendar Goal FAB Initialize
     */
    protected void initGoalFab() {

    }

    /**
     * Calendar Colors
     **/
    protected void setCalendarColors() {

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
     * BackKey Press Listener
     **/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    protected void chooseAccount() {
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
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
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
                if(!acct.getPhotoUrl().toString().isEmpty()) {
                    editor.putString(PREF_ACCOUNT_IMGURI, acct.getPhotoUrl().toString());
                }
                editor.apply();

                break;

            case REQUEST_AUTHORIZATION:

                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }

                break;

        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * Get Result From Calendar API
     **/
    protected void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) { //플레이 서비스 사용권한 얻기
            acquireGooglePlayServices();
        } else if (applicationClass.mCredential.getSelectedAccountName() == null) { //계정 고르기
            chooseAccount();
        }
        else if (! applicationClass.isDeviceOnline()) { //네트워크 연결 확인
//            mOutputText.setText("네트워크가 연결되지 않았습니다.");
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
    protected void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    /**
     * Check GooglePlayService
     **/
    protected boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Acquire GooglePlayService
     **/
    protected void acquireGooglePlayServices() {
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
        Dialog dialog = apiAvailability.getErrorDialog(BaseActivity.this, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Initiate Main UI
     **/
    protected void initMainUi() {

        /* Toolbar Setup */
        setSupportActionBar(toolbar);

    }

}
