package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Catogries_Adapter;
import com.creativeshare.roses.adapter.Market_Adapter;
import com.creativeshare.roses.adapter.SlidingImage_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.models.Slider_Model;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Main extends Fragment {

    private SlidingImage_Adapter slidingImage__adapter;
    private ViewPager mPager;
    private TabLayout indicator;
    private HomeActivity activity;
    private int current_page = 0,NUM_PAGES;
    private ProgressBar progBar, progBarAds;
    private RecyclerView rec_depart,rec_markets;
    private List<Catogries_Model.Data> dataList;
    private Catogries_Adapter catogries_adapter;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page_depart = 1;
    private List<Markets_Model.Data> dataListmarkets;
    private Market_Adapter market_adapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isLoadingmarket = false;
    private int current_page_market = 1;
    private LinearLayout ll_no_store;
    public static Fragment_Main newInstance() {
        Fragment_Main fragment = new Fragment_Main();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment__main, container, false);
    initview(view);
    get_slider();
    change_slide_image();
    getDepartments();
    getMarkets();
    return view;
    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page==NUM_PAGES){
                    current_page = 0;
                }
                mPager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
    private void initview(View view) {
        dataList=new ArrayList<>();
        dataListmarkets=new ArrayList<>();
        activity = (HomeActivity) getActivity();

        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.tablayout);
        progBar = view.findViewById(R.id.progBar2);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        rec_depart=view.findViewById(R.id.rec_depart);
        rec_markets=view.findViewById(R.id.rec_stores);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBarAds = view.findViewById(R.id.progBar);
        progBarAds.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);
        rec_markets.setDrawingCacheEnabled(true);
        rec_markets.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_markets.setItemViewCacheSize(25);

        manager=new LinearLayoutManager((activity), RecyclerView.HORIZONTAL, false);
        gridLayoutManager=new GridLayoutManager(activity,3);
        rec_depart.setLayoutManager(manager);
        rec_markets.setLayoutManager(gridLayoutManager);
        catogries_adapter=new Catogries_Adapter(dataList,activity,this);
        market_adapter=new Market_Adapter(dataListmarkets,activity,this);
      /*  rec_depart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx> 0) {
                    int total_item = catogries_adapter.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    //  Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 5) && !isLoading ) {
                        isLoading = true;
                        dataList.add(null);
                        catogries_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page_depart + 1;

                        loadMore(page);

                    }
                }
            }
        });*/
        rec_markets.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy> 0) {
                    int total_item = market_adapter.getItemCount();
                    int last_item_pos = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    //  Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 15) && !isLoading ) {
                        isLoading = true;
                        dataListmarkets.add(null);
                        market_adapter.notifyItemInserted(dataListmarkets.size() - 1);
                        int page = current_page_market + 1;

                        loadMoremarket(page);

                    }
                }
            }
        });
        rec_depart.setAdapter(catogries_adapter);
        rec_markets.setAdapter(market_adapter);
    }

    private void get_slider() {
        Api.getService(Tags.base_url).get_slider().enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                progBarAds.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getData().size()>0){
                        NUM_PAGES=response.body().getData().size();
                        slidingImage__adapter = new SlidingImage_Adapter(activity,response.body().getData());
                        mPager.setAdapter(slidingImage__adapter);
                        indicator.setupWithViewPager(mPager);
                    }
                    else {
                        //    error.setText("No data Found");
                        // recc.setVisibility(View.GONE);
                        mPager.setVisibility(View.GONE);
                    }
                }
                else if (response.code() == 404) {
                    //  error.setText(activity.getString(R.string.no_data));
                    //recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                } else {
                    // recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    Log.e("Error", t.getMessage());
                    progBarAds.setVisibility(View.GONE);
                    //error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                }
                catch (Exception e){

                }

            }
        });

    }
    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                     //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                             //   ll_no_order.setVisibility(View.GONE);
                                catogries_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                              //  ll_no_order.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
    /*
    private void loadMore(int page) {
        Api.getService(Tags.base_url)
                .getDepartment(page)
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        dataList.remove(dataList.size() - 1);
                        catogries_adapter.notifyItemRemoved(dataList.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            dataList.addAll(response.body().getData());
                            // categories.addAll(response.body().getCategories());
                            current_page_depart = response.body().getCurrent_page();
                            catogries_adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            dataList.remove(dataList.size() - 1);
                            catogries_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading = false;
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }*/
    public void getMarkets() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getMarkets(1)
                .enqueue(new Callback<Markets_Model>() {
                    @Override
                    public void onResponse(Call<Markets_Model> call, Response<Markets_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataListmarkets.clear();
                            dataListmarkets.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                  ll_no_store.setVisibility(View.GONE);
                                market_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                 ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Markets_Model> call, Throwable t) {
                        try {

progBar.setVisibility(View.GONE);
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }
    private void loadMoremarket(int page) {
        Api.getService(Tags.base_url)
                .getMarkets(page)
                .enqueue(new Callback<Markets_Model>() {
                    @Override
                    public void onResponse(Call<Markets_Model> call, Response<Markets_Model> response) {
                        dataList.remove(dataList.size() - 1);
                        catogries_adapter.notifyItemRemoved(dataList.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            dataListmarkets.addAll(response.body().getData());
                            // categories.addAll(response.body().getCategories());
                            current_page_market = response.body().getCurrent_page();
                            market_adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Markets_Model> call, Throwable t) {
                        try {
                            dataListmarkets.remove(dataList.size() - 1);
                            market_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading = false;
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void showmarket(int id) {
        activity.DisplayFragmentShopprofile(id);
    }
}
