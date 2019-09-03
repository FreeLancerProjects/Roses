package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Shop_profile extends Fragment {
    final static private String Tag = "market_id";
private int market_id;
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;
    public static Fragment_Shop_profile newInstance(int id) {
        Fragment_Shop_profile fragment_shop_profile = new Fragment_Shop_profile();
        Bundle bundle = new Bundle();
        bundle.putInt(Tag, id);
        fragment_shop_profile.setArguments(bundle);
        return fragment_shop_profile;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_profile, container, false);
        initview();
        return view;
    }

    private void initview() {
        market_id=getArguments().getInt(Tag);
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }


}
