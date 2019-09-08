package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Catogries_Adapter;
import com.creativeshare.roses.adapter.Catogries_Text_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Catogry extends Fragment implements OnMapReadyCallback {

    private RecyclerView rec_depart, rec_markets;
    private List<Catogries_Model.Data> dataList;
    private List<Markets_Model.Data> mDataList;
    private Catogries_Text_Adapter catogries_adapter;
    private LinearLayoutManager manager;
    private HomeActivity activity;
    private float zoom = 15.6f;
    private GoogleMap mMap;
    private double lat, lang;

    public static Fragment_Catogry newInstance() {
        Fragment_Catogry fragment = new Fragment_Catogry();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catogry, container, false);
        initview(view);
        updateUI();
        getDepartments();
        return view;
    }

    private void initview(View view) {
        rec_depart = view.findViewById(R.id.rec_depart);
        dataList = new ArrayList<>();
        mDataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);
        manager = new LinearLayoutManager((activity), RecyclerView.HORIZONTAL, false);
        rec_depart.setLayoutManager(manager);
        catogries_adapter = new Catogries_Text_Adapter(dataList, activity, this);
        rec_depart.setAdapter(catogries_adapter);

    }

    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        dialog.dismiss();
                        //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                catogries_adapter.notifyDataSetChanged();
                                getMarkets(response.body().getData().get(0).getId());
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

                            dialog.dismiss();

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    public void getMarkets(int cat_id) {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getmarkets(cat_id)
                .enqueue(new Callback<Markets_Model>() {
                    @Override
                    public void onResponse(Call<Markets_Model> call, Response<Markets_Model> response) {
                        dialog.dismiss();
                        mDataList.clear();
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                mDataList.addAll(response.body().getData());
                                // rec_sent.setVisibility(View.VISIBLE);
                                addmarkres(response.body());
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {

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
                            dialog.dismiss();
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void addmarkres(Markets_Model body) {
        Log.e("msg", body.getData().size() + "");
        mMap.clear();
        for (int i = 0; i < body.getData().size(); i++) {
            Markets_Model.Data data = body.getData().get(i);
            AddMarker(data.getLatitude(), data.getLongitude(), i, data.getName());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
//    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);


        }
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void AddMarker(double lat, double lang, int index, String title) {
        Marker marker;

        this.lat = lat;
        this.lang = lang;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lang));
        marker = mMap.addMarker(markerOptions.title(title));
        marker.setTag(index);

        marker.showInfoWindow();

        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));

    }


}
