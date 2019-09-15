package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_cart_complete;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.adapter.Data_Cart_Adapter;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


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
    private Preferences preferences;
    private UserModel userModel;
    private HomeActivity activity;
    private String [] type_array;
    private String cuurent_language;
private Button bt_com;
private ImageView im_back;
    private double total_cost;
    private TextView tv_total;
    public static Fragment_Cart newInstance() {
        return new Fragment_Cart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        intitview(view);
        gettotal();

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
            tv_total.setVisibility(View.INVISIBLE);
        }
        else {
            tv_total.setVisibility(View.VISIBLE);

        }
        activity.getamount();
    }
    private void intitview(View view) {
      //  item_cart_model = new Item_Cart_Model();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        linearLayout = view.findViewById(R.id.ll_no_store);
        im_back=view.findViewById(R.id.arrow_back);
        type_array = new String[]{getResources().getString(R.string.Ocasion),getResources().getString(R.string.defaults)};
        tv_total=view.findViewById(R.id.tv_total);

cart=view.findViewById(R.id.rec_cart);
        bt_com=view.findViewById(R.id.bt_com);
        add_order_model = preferences.getUserOrder(activity);
       cart.setItemViewCacheSize(25);
       cart.setDrawingCacheEnabled(true);
       cart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
       if(cuurent_language.equals("en")){
           im_back.setRotation(180.0f);
       }
        if (add_order_model != null) {

            cart_adpter = new Data_Cart_Adapter(add_order_model, activity,this);
            cart.setLayoutManager(new GridLayoutManager(activity, 1));
            cart.setAdapter(cart_adpter);
            linearLayout.setVisibility(View.GONE);
            cart.setVisibility(View.VISIBLE);
        } else {
          //  error.setText(activity.getString(R.string.no_data));
            linearLayout.setVisibility(View.VISIBLE);
            cart.setVisibility(View.GONE);
        }
if(preferences.getUserOrder(activity)==null){
    bt_com.setVisibility(View.INVISIBLE);
}
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

}
