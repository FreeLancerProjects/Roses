package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.PageAdapter;
import com.creativeshare.roses.adapter.Service_Adapter;
import com.creativeshare.roses.adapter.Service_Profile_Adapter;
import com.creativeshare.roses.models.Market_model;
import com.creativeshare.roses.models.Send_Data;
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

public class Fragment_Shop_profile extends Fragment {
    final static private String Tag = "market_id";
    private int market_id;
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;

    private Fragment_Shop_Department fragment_shop_department;
    private Fragment_Shop_Offers fragment_shop_offers;

    private List<Fragment> fragmentList;
    private PageAdapter pageAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_name, tv_address;
    private ImageView im_banner, im_back;
    private CircleImageView im_logo;
    private RecyclerView rec_service;
    private Service_Profile_Adapter service_profile_adapter;
    private List<Market_model.MarketService> marketServices;

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
        initview(view);
        getsinglemarket();
        return view;
    }

    private void initview(View view) {
        fragmentList = new ArrayList<>();
        marketServices = new ArrayList<>();

        market_id = getArguments().getInt(Tag);
        Send_Data.setMarket_id(this.market_id);

        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tabLayout = view.findViewById(R.id.tab_orders);
        viewPager = view.findViewById(R.id.pager);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        im_banner = view.findViewById(R.id.im_banner);
        im_logo = view.findViewById(R.id.image);
        im_back = view.findViewById(R.id.arrow);
        if (current_lang.equals("en")) {
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });

        rec_service = view.findViewById(R.id.rec_service);
        rec_service.setDrawingCacheEnabled(true);
        rec_service.setItemViewCacheSize(25);
        rec_service.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        service_profile_adapter = new Service_Profile_Adapter(marketServices, homeActivity, this);
        rec_service.setLayoutManager(new LinearLayoutManager(homeActivity, RecyclerView.HORIZONTAL, false));
        rec_service.setAdapter(service_profile_adapter);
        intitfragmentspage();
        pageAdapter = new PageAdapter(getChildFragmentManager());
        pageAdapter.addfragments(fragmentList);
        Log.e("lll", fragmentList.size() + "");
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                Log.e("kk",viewPager.getAdapter().getPageTitle(0).toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void intitfragmentspage() {
        fragment_shop_department = Fragment_Shop_Department.newInstance();
        fragment_shop_offers = Fragment_Shop_Offers.newInstance();
        fragmentList.add(fragment_shop_department);
        fragmentList.add(fragment_shop_offers);
    }

    private void getsinglemarket() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getsinglemarkey(market_id)
                .enqueue(new Callback<Market_model>() {
                    @Override
                    public void onResponse(Call<Market_model> call, Response<Market_model> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            updateprofile(response.body());

                        } else {
                            Common.CreateSignAlertDialog(homeActivity, getString(R.string.failed));

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Market_model> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateprofile(Market_model body) {
        tv_name.setText(body.getName());
        tv_address.setText(body.getAddress());
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + body.getBanner())).fit().placeholder(R.drawable.profile_client).into(im_banner);
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + body.getLogo())).fit().placeholder(R.drawable.logo).into(im_logo);
        if (body.getMarketServices() != null) {
            marketServices.clear();
            marketServices.addAll(body.getMarketServices());
            service_profile_adapter.notifyDataSetChanged();
        }
    }

}
