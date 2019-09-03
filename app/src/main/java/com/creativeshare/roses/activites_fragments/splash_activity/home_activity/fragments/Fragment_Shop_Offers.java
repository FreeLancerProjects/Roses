package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Catogries_Adapter;
import com.creativeshare.roses.adapter.Shop_Offers_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Offer_Model;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Shop_Offers extends Fragment {


    private HomeActivity activity;

    private ProgressBar progBar, progBarAds;
    private RecyclerView rec_depart;
    private List<Offer_Model.Data> dataList;
    private Shop_Offers_Adapter shop_offers_adapter;
    private GridLayoutManager gridLayoutManager;
private LinearLayout ll_no_store;
    private boolean isLoading = false;
    private int current_page_depart = 1;
private int market_id;
    public static Fragment_Shop_Offers newInstance() {
        Fragment_Shop_Offers fragment = new Fragment_Shop_Offers();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_shop_offers, container, false);
    initview(view);
    getDepartments();
    return view;
    }


    private void initview(View view) {
        dataList=new ArrayList<>();
        activity = (HomeActivity) getActivity();


        progBar = view.findViewById(R.id.progBar2);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        rec_depart=view.findViewById(R.id.rec_offers);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBarAds = view.findViewById(R.id.progBar);
        progBarAds.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager=new GridLayoutManager(activity,3);
        rec_depart.setLayoutManager(gridLayoutManager);
        shop_offers_adapter=new Shop_Offers_Adapter(dataList,activity,this);
        rec_depart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx> 0) {
                    int total_item = shop_offers_adapter.getItemCount();
                    int last_item_pos = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    //  Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 15) && !isLoading ) {
                        isLoading = true;
                        dataList.add(null);
                        shop_offers_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page_depart + 1;

                        loadMore(page);

                    }
                }
            }
        });

        rec_depart.setAdapter(shop_offers_adapter);
    }

    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getoffer(1,market_id)
                .enqueue(new Callback<Offer_Model>() {
                    @Override
                    public void onResponse(Call<Offer_Model> call, Response<Offer_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                             //   ll_no_order.setVisibility(View.GONE);
                                shop_offers_adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<Offer_Model> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }
    private void loadMore(int page) {
        Api.getService(Tags.base_url)
                .getoffer(page,market_id)
                .enqueue(new Callback<Offer_Model>() {
                    @Override
                    public void onResponse(Call<Offer_Model> call, Response<Offer_Model> response) {
                        dataList.remove(dataList.size() - 1);
                        shop_offers_adapter.notifyItemRemoved(dataList.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            dataList.addAll(response.body().getData());
                            // categories.addAll(response.body().getCategories());
                            current_page_depart = response.body().getCurrent_page();
                            shop_offers_adapter.notifyDataSetChanged();

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
                    public void onFailure(Call<Offer_Model> call, Throwable t) {
                        try {
                            dataList.remove(dataList.size() - 1);
                            shop_offers_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading = false;
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }



}
