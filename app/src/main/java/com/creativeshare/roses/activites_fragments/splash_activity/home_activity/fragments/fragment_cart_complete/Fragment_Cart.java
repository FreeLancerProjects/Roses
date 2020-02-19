package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_cart_complete;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Data_Cart_Adapter;
import com.creativeshare.roses.adapter.Fav_Products_Adapter;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.Product_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Fragment_Cart extends Fragment {
    private RecyclerView cart;

    private LinearLayout linearLayout;
    private TextView error;
    private Data_Cart_Adapter cart_adpter;
    private List<Add_Order_Model >add_order_model;
    private List<Product_Model.Data> dataList;
    private Fav_Products_Adapter fav_products_adapter;
    private Preferences preferences;
    private UserModel userModel;
    private HomeActivity activity;
    private String [] type_array;
    private String cuurent_language;
private Button bt_com;
private ImageView im_back;
    private double total_cost;
    private TextView tv_total;
    private ProgressBar progBar;

    public static Fragment_Cart newInstance() {
        return new Fragment_Cart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        intitview(view);
        getFav();
       // gettotal();

        return view;
    }
    public void gettotal() {
        total_cost=0.0;
        if(preferences.getUserOrder(activity)!=null){
            for(int j=0;j<preferences.getUserOrder(activity).size();j++){
        for (int i = 0; i < preferences.getUserOrder(activity).get(j).getOrder_details().size(); i++) {
            total_cost += preferences.getUserOrder(activity).get(j).getOrder_details().get(i).getTotal_price();
        }}
        total_cost=Math.round(total_cost);}
        else {
            total_cost=0.0;
        }
        tv_total.setText(activity.getResources().getString(R.string.price)+":"+total_cost);
        if(total_cost==0.0){
            tv_total.setVisibility(View.GONE);
        }
        else {
            tv_total.setVisibility(View.GONE);

        }
        activity.getamount();
    }
    private void intitview(View view) {
      //  item_cart_model = new Item_Cart_Model();
        dataList=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        linearLayout = view.findViewById(R.id.ll_no_store);
        im_back=view.findViewById(R.id.arrow_back);
        type_array = new String[]{getResources().getString(R.string.Ocasion),getResources().getString(R.string.defaults)};
        tv_total=view.findViewById(R.id.tv_total);
        progBar = view.findViewById(R.id.progBar2);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        cart=view.findViewById(R.id.rec_cart);
        bt_com=view.findViewById(R.id.bt_com);
        add_order_model = preferences.getUserOrder(activity);
       cart.setItemViewCacheSize(25);
       cart.setDrawingCacheEnabled(true);
       cart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
       if(cuurent_language.equals("en")){
           im_back.setRotation(180.0f);
       }
//        if (add_order_model != null) {
//
//
//            linearLayout.setVisibility(View.GONE);
//            cart.setVisibility(View.VISIBLE);
//        } else {
//          //  error.setText(activity.getString(R.string.no_data));
//            //linearLayout.setVisibility(View.VISIBLE);
//            cart.setVisibility(View.GONE);
//        }
//if(preferences.getUserOrder(activity)==null){
//    bt_com.setVisibility(View.INVISIBLE);
//}
        cart_adpter = new Data_Cart_Adapter(add_order_model, activity,this);
            fav_products_adapter=new Fav_Products_Adapter(dataList,activity);
            cart.setLayoutManager(new GridLayoutManager(activity, 1));
            cart.setAdapter(fav_products_adapter);
bt_com.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(userModel!=null){
            if(preferences.getUserOrder(activity)!=null)
        CreateTypeDialog();}
        else {
            Common.CreateUserNotSignInAlertDialog(activity);
        }
    }
});
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });

    }

    public void CreateTypeDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(activity).inflate(R.layout.dialog_type,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(type_array.length-1);
        numberPicker.setDisplayedValues(type_array);
        numberPicker.setWrapSelectorWheel(false);
       numberPicker.setValue(0);
        btn_select.setOnClickListener(v -> {
            dialog.dismiss();
            int pos = numberPicker.getValue();
            if (pos == 0)
            {
                Send_Data.setType(2);
                //activity.RefreshActivity("en");
            }else
            {
                Send_Data.setType(1);
                //activity.RefreshActivity("ar");

            }
            activity.DisplayFragmentComplete();

        });




        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        //dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }
    public void getFav() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        progBar.setVisibility(View.VISIBLE);
        // rec_sent.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getproducts(userModel.getId())
                .enqueue(new Callback<Product_Model>() {
                    @Override
                    public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);
                                Log.e("data", response.body().getData().get(0).getAr_title());

                                linearLayout.setVisibility(View.GONE);
                                fav_products_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                linearLayout.setVisibility(View.VISIBLE);

                            }
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);

                            //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product_Model> call, Throwable t) {
                        try {


                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

}
