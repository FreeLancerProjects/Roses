package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_shop_profile.fragment_shop_data.fragment_shop_department;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Shop_Department_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Shop_Department extends Fragment {


    private HomeActivity activity;

    private ProgressBar progBar;
    private RecyclerView rec_depart;
    private List<Catogries_Model.Data> dataList;
    private Shop_Department_Adapter shop_department_adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout ll_no_store;
    private boolean isLoading = false;
    private int current_page_depart = 1;
    private int market_id;

    public static Fragment_Shop_Department newInstance() {
        Fragment_Shop_Department fragment = new Fragment_Shop_Department();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_departments, container, false);
        initview(view);
        getDepartments();
        return view;
    }


    private void initview(View view) {
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();

        market_id = Send_Data.getMarket_id();
        progBar = view.findViewById(R.id.progBar2);
        ll_no_store = view.findViewById(R.id.ll_no_store);
        rec_depart = view.findViewById(R.id.rec_departments);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager = new GridLayoutManager(activity, 2);
        rec_depart.setLayoutManager(gridLayoutManager);
        shop_department_adapter = new Shop_Department_Adapter(dataList, activity, this);
     /*   rec_depart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx> 0) {
                    int total_item = catogries_adapter.getItemCount();
                    int last_item_pos = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    //  Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 15) && !isLoading ) {
                        isLoading = true;
                        dataList.add(null);
                        catogries_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page_depart + 1;

                        loadMore(page);

                    }
                }
            }
        });*/

        rec_depart.setAdapter(shop_department_adapter);
    }

    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getDepartment(market_id)
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);
                                Log.e("sss", response.body().getData().get(0).getAr_title());
                                  ll_no_store.setVisibility(View.GONE);
                                shop_department_adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<Catogries_Model> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }
  /*  private void loadMore(int page) {
        Api.getService(Tags.base_url)
                .getDepartment(page,market_id)
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
    }
*/


}
