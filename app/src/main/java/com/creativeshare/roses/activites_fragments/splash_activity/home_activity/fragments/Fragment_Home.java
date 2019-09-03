package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;


import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {
    private HomeActivity homeActivity;
    private AHBottomNavigation ah_bottom_nav;
    private String cuurent_language;

    private TextView tv_title;
    private Preferences preferences;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        ah_bottom_nav = view.findViewById(R.id.ah_bottom_nav);
        tv_title = view.findViewById(R.id.tv_title);


        setUpBottomNavigation();
        updateBottomNavigationPosition(0);
        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        homeActivity.DisplayFragmentMain();
                        break;
                    case 1:
                        homeActivity.DisplayFragmentCatogry();

                        break;
                    case 2:
                        homeActivity.DisplayFragmentclientprofile();
                        break;
                    case 3:
                        homeActivity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.Shops), R.drawable.shops);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.department), R.drawable.department);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_profile), R.drawable.profile);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.more);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(homeActivity, R.color.white));
        ah_bottom_nav.setTitleTextSizeInSp(14, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(homeActivity, R.color.colorAccent));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(homeActivity, R.color.gray4));

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);


    }

    public void updateBottomNavigationPosition(int pos) {
        ah_bottom_nav.setCurrentItem(pos, false);
        if (pos == 0) {
            tv_title.setText(getResources().getString(R.string.Shops));
        } else if (pos == 1) {
            tv_title.setText(getResources().getString(R.string.department));
        } else if (pos == 2) {
            tv_title.setText(getResources().getString(R.string.my_profile));
        } else if (pos == 3) {
            tv_title.setText(getResources().getString(R.string.more));
        }

    }

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


}
