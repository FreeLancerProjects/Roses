package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Shop_Department_Adapter;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.google.android.material.navigation.NavigationView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private HomeActivity homeActivity;
    private AHBottomNavigation ah_bottom_nav;
    private String cuurent_language;
    public DrawerLayout mDrawer;
    private NavigationView nvView;
    private TextView tv_title;
    private Preferences preferences;
    private UserModel userModel;
private ImageView im_cart,im_menu;
    private RecyclerView rec_depart;
    private List<Catogries_Model.Data> dataList;
    private Shop_Department_Adapter shop_department_adapter;
    private GridLayoutManager gridLayoutManager;
    private TextView textNotify;
    private int amount=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        getDepartments();
        gettotal();
        return view;
    }

    public void gettotal() {
        amount=0;
        if(preferences.getUserOrder(homeActivity)!=null){
        for (int i = 0; i < preferences.getUserOrder(homeActivity).size(); i++) {
            Add_Order_Model add_order_model = preferences.getUserOrder(homeActivity).get(i);
            for (int j = 0; j < add_order_model.getOrder_details().size(); j++) {
                amount += add_order_model.getOrder_details().get(j).getAmount();
            }
        }}
        addItemToCart();

    }

    private void addItemToCart() {
        if(amount>0){

            textNotify.setText(amount + "");
            textNotify.setVisibility(View.VISIBLE);

        }
        else {
            textNotify.setVisibility(View.GONE);
        }
    }
    private void initView(View view) {
dataList=new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        textNotify=view.findViewById(R.id.textNotify);
        ah_bottom_nav = view.findViewById(R.id.ah_bottom_nav);
        tv_title = view.findViewById(R.id.tv_title);
        rec_depart = view.findViewById(R.id.rec_depart);
        im_cart=view.findViewById(R.id.im_cart);
        im_menu=view.findViewById(R.id.im_menu);

        mDrawer = view.findViewById(R.id.drawer_layout);
        nvView = view.findViewById(R.id.nvView);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager = new GridLayoutManager(homeActivity, 2);
        rec_depart.setLayoutManager(gridLayoutManager);

        shop_department_adapter = new Shop_Department_Adapter(dataList, homeActivity, this);
        rec_depart.setAdapter(shop_department_adapter);


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
                        if(userModel!=null){
                        homeActivity.DisplayFragmentclientprofile();}
                        else {
                            Common.CreateUserNotSignInAlertDialog(homeActivity);
                        }
                        break;
                    case 3:
                        homeActivity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });
        im_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.display_Cart();

            }
        });
        im_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.END);

            }
        });
    }
    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

      //  progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                      //  progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);
                                Log.e("sss", response.body().getData().get(0).getAr_title());
                        //        ll_no_store.setVisibility(View.GONE);
                                shop_department_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                           //     ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {

                          //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Catogries_Model> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.shops);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.Search), R.drawable.ic_placeholder);
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
