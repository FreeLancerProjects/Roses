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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.PageAdapter;
import com.creativeshare.roses.adapter.Service_Profile_Adapter;
import com.creativeshare.roses.models.Market_model;
import com.creativeshare.roses.models.Order_Model;
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

public class Fragment_Product_Details extends Fragment {
    private HomeActivity homeActivity;
    private Preferences preferences;
    private String current_lang;
    private UserModel userModel;


    private TextView tv_name,tv_address;
    private ImageView im_banner;
    private CircleImageView im_logo;
    private RecyclerView rec_service;
    private Service_Profile_Adapter service_profile_adapter;
    private List<Market_model.MarketService> marketServices;
    public static Fragment_Product_Details newInstance() {
        Fragment_Product_Details fragment_shop_profile = new Fragment_Product_Details();

        return fragment_shop_profile;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detials, container, false);
        initview(view);
        updateprofile(Send_Data.getData());
        return view;
    }

    private void initview(View view) {
        marketServices=new ArrayList<>();
rec_service.setVisibility(View.GONE);

        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_name=view.findViewById(R.id.tv_name);
        tv_address=view.findViewById(R.id.tv_address);
        im_banner=view.findViewById(R.id.im_banner);
        im_logo=view.findViewById(R.id.image);
        rec_service=view.findViewById(R.id.rec_service);
        rec_service.setDrawingCacheEnabled(true);
        rec_service.setItemViewCacheSize(25);
        rec_service.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        service_profile_adapter=new Service_Profile_Adapter(marketServices,homeActivity,this);
        rec_service.setLayoutManager(new LinearLayoutManager(homeActivity,RecyclerView.HORIZONTAL,false));
        rec_service.setAdapter(service_profile_adapter);

    }



    private void updateprofile(Order_Model.Data body) {
tv_name.setText(body.getMarket_name());
tv_address.setText(body.getAddress());
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL+body.getMarket_image())).fit().placeholder(R.drawable.profile_client).into(im_banner);
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL+body.getUser_image())).fit().placeholder(R.drawable.logo).into(im_logo);
if(body.getServices()!=null){
    marketServices.clear();
    //marketServices.addAll(body.getServices());
    service_profile_adapter.notifyDataSetChanged();
}
    }

}
