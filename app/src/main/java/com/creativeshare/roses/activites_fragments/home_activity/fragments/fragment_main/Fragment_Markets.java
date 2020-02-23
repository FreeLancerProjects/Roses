package com.creativeshare.roses.activites_fragments.home_activity.fragments.fragment_main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Market_Adapter;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Markets extends Fragment {


    private ProgressBar progBar;
    private RecyclerView rec_markets;
private HomeActivity activity;
    private List<Markets_Model.Data> dataListmarkets;
    private Market_Adapter market_adapter;
    private GridLayoutManager gridLayoutManager;
    private int current_page_market = 1;
    private LinearLayout ll_no_store;
    private ImageView im_back;
    private String cuurent_language;

    private Preferences preferences;
    public static Fragment_Markets newInstance() {
        Fragment_Markets fragment = new Fragment_Markets();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_markets, container, false);
    initview(view);
    getMarkets();
    return view;
    }

    private void initview(View view) {
        dataListmarkets=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        progBar = view.findViewById(R.id.progBar2);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        rec_markets=view.findViewById(R.id.rec_stores);
        im_back=view.findViewById(R.id.arrow_back);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        progBar.setVisibility(View.GONE);
if(cuurent_language.equals("en")){
    im_back.setRotation(180.0f);
}
        rec_markets.setDrawingCacheEnabled(true);
        rec_markets.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_markets.setItemViewCacheSize(25);

        gridLayoutManager=new GridLayoutManager(activity,3);
        rec_markets.setLayoutManager(gridLayoutManager);
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

        rec_markets.setAdapter(market_adapter);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
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
progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getmarketsbycat(Send_Data.getCat_id())
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
                            ll_no_store.setVisibility(View.VISIBLE);

                          //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    public void showmarket(int id) {
        activity.DisplayFragmentShopprofile(id);
    }
}
