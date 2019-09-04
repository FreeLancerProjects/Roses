package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.AppDataModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class Fragment_About extends Fragment {
    private Preferences preferences;
    private ImageView back;
    private TextView tv_content;
    private HomeActivity activity;
    private String cuurent_language;

    public static Fragment_About newInstance() {

Fragment_About about=new Fragment_About();
    return about;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        intitview(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void intitview(View view) {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences=Preferences.getInstance();
        tv_content =view.findViewById(R.id.tv_content);
        back = (ImageView) view.findViewById(R.id.arrow_back);
        preferences = Preferences.getInstance();

        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        getAppData(cuurent_language);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
    }
    private void getAppData(String cuurent_language) {

        Api.getService(Tags.base_url)
                .getabout()
                .enqueue(new Callback<AppDataModel>() {
                    @Override
                    public void onResponse(Call<AppDataModel> call, Response<AppDataModel> response) {
                      //  smoothprogressbar.setVisibility(View.GONE);

                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            updateTermsContent(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppDataModel> call, Throwable t) {
                        try {
                           // smoothprogressbar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void updateTermsContent(AppDataModel appDataModel) {

        if(cuurent_language.equals("ar")){
            tv_content.setText(appDataModel.getData().getConditions().getAr_title());}
        else {
            tv_content.setText(appDataModel.getData().getConditions().getEn_content());
        }

    }
}
