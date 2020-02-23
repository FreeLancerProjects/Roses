package com.creativeshare.roses.activites_fragments.home_activity.fragments.fragmnet_client_profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.home_activity.fragments.fragmnet_client_profile.fragmnet_client_order.Fragment_Client_Ocasions;
import com.creativeshare.roses.activites_fragments.home_activity.fragments.fragmnet_client_profile.fragmnet_client_order.Fragment_Client_Orders;
import com.creativeshare.roses.adapter.PageAdapter;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.models.SocialDataModel;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_profile extends Fragment {
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;
    private TextView tv_name, tv_phone;
    private CircleImageView im_profile;
    private ImageView imageInstagram,imageTwitter,im_snapchat;
    private SocialDataModel socialDataModel;

    private Fragment_Client_Orders fragment_client_orders;
    private Fragment_Client_Ocasions fragment_client_ocasions;
    private List<Fragment> fragmentList;
    private PageAdapter pageAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        fragmentList = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        im_profile = view.findViewById(R.id.image);
        tabLayout = view.findViewById(R.id.tab_orders);
        viewPager = view.findViewById(R.id.pager);
        imageInstagram=view.findViewById(R.id.image_instagram);
        imageTwitter = view.findViewById(R.id.image_twitter);
        im_snapchat=view.findViewById(R.id.image_facebook);
        intitfragmentspage();
        pageAdapter = new PageAdapter(getChildFragmentManager());
        pageAdapter.addfragments(fragmentList);
        viewPager.setAdapter(pageAdapter);
        if(Send_Data.getType()==2){
            viewPager.setCurrentItem(1);
        }
        else {
            viewPager.setCurrentItem(0);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        imageInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socialDataModel!=null)
                {
                    if (socialDataModel.getInstagram()!=null&&!TextUtils.isEmpty(socialDataModel.getInstagram()) &&!socialDataModel.getInstagram().equals("0"))
                    {
                        createSocialIntent(socialDataModel.getInstagram());
                    }
                    else
                    {
                        Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));
                    }
                }else {
                    Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));

                }
            }
        });

imageTwitter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (socialDataModel!=null)
        {
            if (socialDataModel.getTwitter()!=null&&!TextUtils.isEmpty(socialDataModel.getTwitter()) &&!socialDataModel.getTwitter().equals("0"))
            {
                createSocialIntent(socialDataModel.getTwitter());
            }
            else
            {
                Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));
            }
        }else {
            Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));

        }
    }
});
im_snapchat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (socialDataModel!=null)
        {
            if (socialDataModel.getSnapchat()!=null&&!TextUtils.isEmpty(socialDataModel.getSnapchat()) &&!socialDataModel.getSnapchat().equals("0"))
            {
                createSocialIntent(socialDataModel.getSnapchat());
            }
            else
            {
                Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));
            }
        }else {
            Common.CreateSignAlertDialog(homeActivity,getString(R.string.not_avail));

        }
    }
});

        getSocialMedia();
    }
    private void intitfragmentspage() {
        fragment_client_orders = Fragment_Client_Orders.newInstance();
        fragment_client_ocasions = Fragment_Client_Ocasions.newInstance();
        fragmentList.add(fragment_client_orders);
        fragmentList.add(fragment_client_ocasions);

    }

    private void getSocialMedia() {
        ProgressDialog dialog = Common.createProgressDialog(homeActivity,getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .getSocial()
                .enqueue(new Callback<SocialDataModel>() {
                    @Override
                    public void onResponse(Call<SocialDataModel> call, Response<SocialDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            socialDataModel = response.body();
                        }else
                        {
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SocialDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }

    private void createSocialIntent(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void refresh(int type) {
        if(type==2){
        if(fragment_client_ocasions!=null){
            fragment_client_ocasions.getOrders();
        }
        viewPager.setCurrentItem(1);
        }
        else {
        if(fragment_client_orders!=null){
            fragment_client_orders.getOrders();
        }
            viewPager.setCurrentItem(0);

        }
    }
}
