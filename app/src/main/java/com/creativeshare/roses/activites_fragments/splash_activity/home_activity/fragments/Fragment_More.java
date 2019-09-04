package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;

public class Fragment_More extends Fragment {
private LinearLayout ll_lang,ll_terms,ll_contact,ll_register_as_a_company,ll_logout;
private HomeActivity homeActivity;
private Preferences preferences;
private UserModel userModel;
    public static Fragment_More newInstance() {
        Fragment_More fragment = new Fragment_More();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_more, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        homeActivity=(HomeActivity)getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(homeActivity);
        ll_lang=view.findViewById(R.id.ll_lang);
        ll_terms=view.findViewById(R.id.ll_terms);
        ll_register_as_a_company=view.findViewById(R.id.ll_register_as_a_company);
        ll_logout=view.findViewById(R.id.ll_logout);
ll_terms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        homeActivity.DisplayFragmentTerms_Condition();
    }
});
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                } else {
                    homeActivity.Logout();
                }
            }
        });
    }


}
