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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Service_Adapter;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.AppDataModel;
import com.creativeshare.roses.models.Market_model;
import com.creativeshare.roses.models.One_Order_Model;
import com.creativeshare.roses.models.PlaceGeocodeData;
import com.creativeshare.roses.models.PlaceMapDetailsData;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.services.Service;
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
import com.google.maps.android.ui.IconGenerator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class Fragment_Complete extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback, DatePickerDialog.OnDateSetListener {
    private Preferences preferences;
    private ImageView back;
    private HomeActivity activity;
    private String cuurent_language;
    private UserModel userModel;
    private Add_Order_Model add_order_model;
    private EditText edt_title, edt_address;
    private LinearLayout ll_date;
    private TextView tv_date, tv1, tv2;
    private Button bt_send;
    private String formated_address;
    private double lat, lang;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private boolean stop = false;
    private float zoom = 15.6f;
    private Marker marker;
    private GoogleMap mMap;
    private double total_cost = 0;
    private DatePickerDialog datePickerDialog;
    private RecyclerView rec_service;
    private Service_Adapter service_adapter;
    private List<Market_model.MarketService> marketServices;
    private Long date;
    private Add_Order_Model.Services ser;

    public static Fragment_Complete newInstance() {

        Fragment_Complete about = new Fragment_Complete();
        return about;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        updateUI();
        intitview(view);
        gettotal();
        getsinglemarket();
        CheckPermission();
        // Inflate the layout for this fragment

        return view;
    }

    private void gettotal() {
        for (int i = 0; i < add_order_model.getOrder_details().size(); i++) {
            total_cost += add_order_model.getOrder_details().get(i).getTotal_price();
        }
    }

    private void intitview(View view) {
        marketServices = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        add_order_model = preferences.getUserOrder(activity);

        edt_title = view.findViewById(R.id.edtTitle);
        rec_service = view.findViewById(R.id.rec_service);
        edt_address = view.findViewById(R.id.edtAddress);
        ll_date = view.findViewById(R.id.llStartdate);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        rec_service.setDrawingCacheEnabled(true);
        rec_service.setItemViewCacheSize(25);
        rec_service.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        service_adapter = new Service_Adapter(marketServices, activity, this);
        rec_service.setLayoutManager(new GridLayoutManager(activity, 1));
        rec_service.setAdapter(service_adapter);
        tv_date = view.findViewById(R.id.tvStartDate);
        edt_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String query = edt_address.getText().toString();
                    if (!TextUtils.isEmpty(query)) {
                        Search(query);
                        return true;
                    }
                }
                return false;
            }
        });
        if (Send_Data.getType() == 1) {
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            edt_title.setVisibility(View.GONE);
        }
        back = view.findViewById(R.id.arrow);
        bt_send = view.findViewById(R.id.btn_send);
        preferences = Preferences.getInstance();

        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });
        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(activity.getFragmentManager(), "");

            }
        });
        createDatePickerDialog();

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(cuurent_language));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        //Log.e("kkkk", calendar.getTime().getMonth() + "");

        tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        date = calendar.getTimeInMillis() / 1000;
        // date = calendar.get(Calendar.YEAR) + "-" + (calendar.getTime().getMonth()+calendar.getTime().getMonth():calendar.getTime().getMonth()) + "-" + (calendar.getTime().getDay()<10?"0"+calendar.getTime().getDay():calendar.getTime().getDay());
        //Log.e("kkk", date);

    }

    private void checkdata() {
        Common.CloseKeyBoard(activity,edt_address);
        String title = edt_title.getText().toString();
        String dated = tv_date.getText().toString();
        if (Send_Data.getType() == 1) {
            if (!TextUtils.isEmpty(dated) && !TextUtils.isEmpty(formated_address)) {

                List<Add_Order_Model.Services> services = new ArrayList<>();
                Add_Order_Model.Services ser = new Add_Order_Model.Services();
                ser.setService_id(1);
                services.add(ser);
                add_order_model.setAddress(formated_address);
                add_order_model.setLatitude(lat);
                add_order_model.setLongitude(lang);
                add_order_model.setNext_date(date);
                add_order_model.setTitle(null);
                add_order_model.setType(Send_Data.getType());
                add_order_model.setServices(services);
                add_order_model.setUser_id(userModel.getId());
                add_order_model.setTotal_cost(total_cost);
                accept_order();
            }
        } else {
            if (!TextUtils.isEmpty(dated) && !TextUtils.isEmpty(formated_address) && !TextUtils.isEmpty(title)) {

                List<Add_Order_Model.Services> services = new ArrayList<>();
                for (int i = 0; i < services.size(); i++) {
                    View view = rec_service.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.chec_service);
                    if (checkBox.isChecked()) {
                        ser = new Add_Order_Model.Services();
                        ser.setService_id(services.get(0).getService_id());
                        services.add(ser);
                    }
                }
                add_order_model.setAddress(formated_address);
                add_order_model.setLatitude(lat);
                add_order_model.setLongitude(lang);
                add_order_model.setNext_date(date);
                add_order_model.setTitle(title);
                add_order_model.setType(Send_Data.getType());
                add_order_model.setServices(services);
                add_order_model.setUser_id(userModel.getId());
                add_order_model.setTotal_cost(total_cost);
                accept_order();
            }
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
        getGeoData(lat, lang);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
//    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lang = latLng.longitude;
                    //  Log.e("nnn",lat+"  "+lng);
                    getGeoData(lat, lang);
                    // AddMarker(lat, lang);
                }
            });

        }
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void AddMarker(double lat, double lang) {
        this.lat = lat;
        this.lang = lang;
        if (marker == null) {

            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lang)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lang));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), zoom));
        }
    }

    private void Search(String query) {

        // image_pin.setVisibility(View.GONE);
        //progBar.setVisibility(View.VISIBLE);
        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, cuurent_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            /*image_pin.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
*/
                            //    Fragment_Add_Order_To_Cart.placeMapDetailsData = response.body();

                            if (response.body().getCandidates().size() > 0) {

                                formated_address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                edt_address.setText(formated_address);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getGeoData(final double lat, final double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, cuurent_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                formated_address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                // address.setText(formatedaddress);
                                edt_address.setText(formated_address);
                                AddMarker(lat, lng);
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                //   Log.e("kkk", formatedaddress);
                            }
                        } else {
                            Log.e("error_code", response.errorBody() + " " + response.code());

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void accept_order() {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(add_order_model).enqueue(new Callback<One_Order_Model>() {
            @Override
            public void onResponse(Call<One_Order_Model> call, Response<One_Order_Model> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    preferences.create_update_order(activity, null);
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));
                    activity.Back();
                    activity.Back();
                    activity.DisplayFragmentclientprofile();
                } else {
                    Common.CreateSignAlertDialog(activity, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<One_Order_Model> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void getsinglemarket() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getsinglemarkey(add_order_model.getMarket_id())
                .enqueue(new Callback<Market_model>() {
                    @Override
                    public void onResponse(Call<Market_model> call, Response<Market_model> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            updateprofile(response.body());

                        } else {
                            Common.CreateSignAlertDialog(activity, getString(R.string.failed));

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Market_model> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateprofile(Market_model body) {
        marketServices.clear();
        if (body.getMarketServices() != null) {
            marketServices.addAll(body.getMarketServices());
        }
        service_adapter.notifyDataSetChanged();
    }

}
