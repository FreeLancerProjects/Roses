package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Catogries_Text_Adapter;
import com.creativeshare.roses.adapter.Infowindows_Adapter;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Catogry extends Fragment implements  GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback {
    private ViewGroup infoWindow;
    private RecyclerView rec_depart, rec_markets;
    private List<Catogries_Model.Data> dataList;
    private List<Markets_Model.Data> mDataList;
    private Catogries_Text_Adapter catogries_adapter;
    private LinearLayoutManager manager;
    private HomeActivity activity;
    private float zoom = 5f;
    private GoogleMap mMap;
    private double lat, lang;
    private String current_lang;
    List<Marker> list = new ArrayList<>();
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private static final int MAP_ZOOM_LEVEL = 4;
    private Marker marker;

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
        CheckPermission();
        getDepartments();
        return view;
    }

    private void initview(View view) {
        rec_depart = view.findViewById(R.id.rec_depart);
        dataList = new ArrayList<>();
        mDataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

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
                            dataList.add(new Catogries_Model.Data(0,"الكل","all"));
                            dataList.addAll(response.body().getData());
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                catogries_adapter.notifyDataSetChanged();
                                getMarkets(dataList.get(0).getId());
                                //   total_page = response.body().getMeta().getLast_page();


                        } else {

                        //    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                .getmarketsbycat(cat_id)
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
        mMap.setInfoWindowAdapter(null);
        list.clear();
        for (int i = 0; i < body.getData().size(); i++) {
            Markets_Model.Data data = body.getData().get(i);
            AddMarker(data.getLatitude(), data.getLongitude(), i, data.getName());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(body.getData().get(0).getLatitude(), body.getData().get(0).getLongitude()), zoom));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
//    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);


mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!marker.getTag().equals("t")){
        Infowindows_Adapter adapter = new Infowindows_Adapter(activity,mDataList);

        mMap.setInfoWindowAdapter(adapter);
        adapter.getInfoContents(marker);

        marker.showInfoWindow();}
        return false;
    }
});
mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {
        activity.DisplayFragmentShopprofile(mDataList.get((Integer) marker.getTag()).getId());
    }
});

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
        if(index==0) {
          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));
        }
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{gps_perm}, gps_req);
        } else {
            initGoogleApiClient();

        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.
                Builder(getActivity()).
                addOnConnectionFailedListener(this).
                addConnectionCallbacks(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1255) {
            if (requestCode == Activity.RESULT_OK) {
                startLocationUpdate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGoogleApiClient();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        lat = location.getLatitude();
        lang = location.getLongitude();
      //  getGeoData(lat, lang);
        //AddMarker(lat, lang);

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        if (locationRequest != null) ;
        {
            LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(locationCallback);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), 1255);
                        } catch (Exception e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

}
