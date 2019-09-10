package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Client_Order_Detials_Adapter;
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


    private TextView tv_name, tv_address;
    private ImageView im_banner,im_back;
    private CircleImageView im_logo;
    private RecyclerView rec_service;
    private Service_Profile_Adapter service_profile_adapter;
    private List<Market_model.MarketService> marketServices;
    private List<Order_Model.Data.OrderDetails> orderDetails;
    private Client_Order_Detials_Adapter client_order_detials_adapter;
    private RecyclerView rec_orders;

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
        marketServices = new ArrayList<>();
        orderDetails = new ArrayList<>();

        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        im_banner = view.findViewById(R.id.im_banner);
        im_logo = view.findViewById(R.id.image);
        im_back=view.findViewById(R.id.arrow);

        rec_service = view.findViewById(R.id.rec_service);
        rec_service.setVisibility(View.GONE);

        rec_orders = view.findViewById(R.id.rec_orders);
        rec_service.setDrawingCacheEnabled(true);
        rec_service.setItemViewCacheSize(25);
        rec_service.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        service_profile_adapter = new Service_Profile_Adapter(marketServices, homeActivity, this);
        rec_service.setLayoutManager(new LinearLayoutManager(homeActivity, RecyclerView.HORIZONTAL, false));
        rec_service.setAdapter(service_profile_adapter);
        rec_orders.setDrawingCacheEnabled(true);
        rec_orders.setItemViewCacheSize(25);
        rec_orders.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_orders.setLayoutManager(new GridLayoutManager(homeActivity, 1));
        client_order_detials_adapter = new Client_Order_Detials_Adapter(orderDetails, homeActivity, this);
        rec_orders.setAdapter(client_order_detials_adapter);
        if(current_lang.equals("en")){
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });

    }


    private void updateprofile(Order_Model.Data body) {
        tv_name.setText(body.getMarket_name());
        tv_address.setText(body.getAddress());
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + body.getMarket_image())).fit().placeholder(R.drawable.profile_client).into(im_banner);
        Picasso.with(homeActivity).load(Uri.parse(Tags.IMAGE_URL + body.getUser_image())).fit().placeholder(R.drawable.logo).into(im_logo);
        if (body.getServices() != null) {
            marketServices.clear();
            //marketServices.addAll(body.getServices());
            service_profile_adapter.notifyDataSetChanged();
        }
        orderDetails.clear();
        orderDetails.addAll(body.getOrderDetails());
        client_order_detials_adapter.notifyDataSetChanged();

    }


}
