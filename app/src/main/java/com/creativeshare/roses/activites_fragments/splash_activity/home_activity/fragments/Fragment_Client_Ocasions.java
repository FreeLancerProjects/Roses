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
import com.creativeshare.roses.adapter.Client_Order_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Order_Model;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Client_Ocasions extends Fragment {


    private HomeActivity activity;

    private ProgressBar progBar;
    private RecyclerView rec_depart;
    private List<Order_Model.Data> dataList;
    private Client_Order_Adapter client_order_adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout ll_no_store;
    private boolean isLoading = false;
    private int current_page_depart = 1;
    private UserModel userModel;
    private Preferences preferences;
    public static Fragment_Client_Ocasions newInstance() {
        Fragment_Client_Ocasions fragment = new Fragment_Client_Ocasions();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_client_ocassion, container, false);
    initview(view);
  getOrders();
    return view;
    }


    private void initview(View view) {
        dataList=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);

        progBar = view.findViewById(R.id.progBar2);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        rec_depart=view.findViewById(R.id.rec_departments);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager=new GridLayoutManager(activity,1);
        rec_depart.setLayoutManager(gridLayoutManager);
        client_order_adapter=new Client_Order_Adapter(dataList,activity,this);
        /*rec_depart.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        rec_depart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx> 0) {
                    int total_item = client_order_adapter.getItemCount();
                    int last_item_pos = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    //  Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 5) && !isLoading ) {
                        isLoading = true;
                        dataList.add(null);
                        client_order_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page_depart + 1;

                        loadMore(page);

                    }
                }
            }
        });

        rec_depart.setAdapter(client_order_adapter);
    }

    public void getOrders() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getorders(1,userModel.getId(),2)
                .enqueue(new Callback<Order_Model>() {
                    @Override
                    public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                ll_no_store.setVisibility(View.GONE);
                                client_order_adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<Order_Model> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }
    private void loadMore(int page) {
        Api.getService(Tags.base_url)
                .getorders(page,userModel.getId(),2)
                .enqueue(new Callback<Order_Model>() {
                    @Override
                    public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                        dataList.remove(dataList.size() - 1);
                        client_order_adapter.notifyItemRemoved(dataList.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            dataList.addAll(response.body().getData());
                            // categories.addAll(response.body().getCategories());
                            current_page_depart = response.body().getCurrent_page();
                            client_order_adapter.notifyDataSetChanged();

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
                    public void onFailure(Call<Order_Model> call, Throwable t) {
                        try {
                            dataList.remove(dataList.size() - 1);
                            client_order_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading = false;
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }
}
