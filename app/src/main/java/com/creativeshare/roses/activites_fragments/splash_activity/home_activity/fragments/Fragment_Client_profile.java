package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Fragment_Client_profile extends Fragment {
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;
    private TextView tv_name, tv_phone;
    private CircleImageView im_profile;

    public static Fragment_Client_profile newInstance() {
        Fragment_Client_profile fragment = new Fragment_Client_profile();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);
        initview(view);
        updateprofile();
        return view;
    }

    private void updateprofile() {
        if (userModel != null) {
            tv_name.setText(userModel.getName());
            tv_phone.setText(userModel.getPhone());
            Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + userModel.getLogo())).fit().placeholder(
                    R.drawable.logo
            ).into(im_profile);
        }
    }

    private void initview(View view) {
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        im_profile = view.findViewById(R.id.image);

    }


}
