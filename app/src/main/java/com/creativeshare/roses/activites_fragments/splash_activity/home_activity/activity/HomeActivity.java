package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Catogry;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Home;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Main;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_More;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int fragment_count = 0;
    private Fragment_Home fragment_home;

    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private Fragment_More fragment_more;
    private Fragment_Main fragment_main;
    private Fragment_Catogry fragment_catogry;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        if (savedInstanceState == null) {
            //           CheckPermission();

            DisplayFragmentHome();
                     DisplayFragmentMain();
        }

        if (userModel != null) {
            //       updateToken();
        }

    }


    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();
        String visitTime = preferences.getVisitTime(this);
        Calendar calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(timeNow));

        if (!date.equals(visitTime)) {
            //   addVisit(date);
        }
    }


    public void DisplayFragmentHome() {
        fragment_count += 1;
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }

    }
    public void DisplayFragmentMain() {
        if (fragment_main == null) {
            fragment_main = Fragment_Main.newInstance();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_catogry != null && fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_catogry).commit();
        }


        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(0);
        }
    }
    public void DisplayFragmentCatogry() {
        if (fragment_catogry == null) {
            fragment_catogry = Fragment_Catogry.newInstance();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }


        if (fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_catogry).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_catogry, "fragment_catogry").addToBackStack("fragment_catogry").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(2);
        }
    }

    public void DisplayFragmentMore() {
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_catogry != null && fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_catogry).commit();
        }


        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(3);
        }
    }
}




  /*
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_count > 1) {
            fragment_count -= 1;
            super.onBackPressed();
        } else {

            if (fragment_home != null && fragment_home.isVisible()) {
                if (fragment_main != null && fragment_main.isVisible()) {
                    if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this);
                    } else {
                        finish();
                    }
                } else {
                    DisplayFragmentMain();
                }
            } else {
                DisplayFragmentHome();
            }
        }

    }

   */



