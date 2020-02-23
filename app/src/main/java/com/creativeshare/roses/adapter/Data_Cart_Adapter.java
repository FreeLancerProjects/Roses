package com.creativeshare.roses.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.home_activity.fragments.fragment_cart_complete.Fragment_Cart;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Data_Cart_Adapter  extends RecyclerView.Adapter<Data_Cart_Adapter.Eyas_Holder> {
    List<Add_Order_Model> list;
    Context context;
    HomeActivity activity;
    Preferences preferences;
    private String current_lang;
    private Fragment_Cart fragment_cart;
private Cart_Adpter cart_adpter;
    public Data_Cart_Adapter(List<Add_Order_Model> list, Context context, Fragment_Cart fragment_cart) {
        this.list = list;
        this.context = context;
        activity = (HomeActivity) context;
        preferences = Preferences.getInstance();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment_cart = fragment_cart;
    }

    @Override
    public Data_Cart_Adapter.Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_home_row, viewGroup, false);
        Data_Cart_Adapter.Eyas_Holder eas = new Data_Cart_Adapter.Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Data_Cart_Adapter.Eyas_Holder viewHolder, int i) {
        Add_Order_Model model = list.get(i);
            viewHolder.tv_name.setText(model.getName() + "");
            Log.e("ll",model.getMarket_id()+"");
        viewHolder. tv_total.setText(activity.getResources().getString(R.string.price)+":"+gettotal(model.getOrder_details()));

        cart_adpter = new Cart_Adpter(list.get(i).getOrder_details(), context,fragment_cart,i,this);
        viewHolder.rec_data.setAdapter(cart_adpter);
        viewHolder.bt_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getUserData(activity)!=null){
                    Send_Data.setindex(i);
                    fragment_cart.CreateTypeDialog();}
                else {
                    Common.CreateUserNotSignInAlertDialog(activity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_total;
private RecyclerView rec_data;
        private Button bt_com;

        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_total=itemView.findViewById(R.id.tv_total);
            bt_com=itemView.findViewById(R.id.bt_com);

            rec_data=itemView.findViewById(R.id.rec_cart);
rec_data.setLayoutManager(new GridLayoutManager(activity,1));
rec_data.setItemViewCacheSize(25);
rec_data.setDrawingCacheEnabled(true);
rec_data.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }
}
    private double gettotal(List<Add_Order_Model.Order_details> order_details) {
        double total_cost=0.0;
        for (int i = 0; i < order_details.size(); i++) {
            total_cost += order_details.get(i).getTotal_price();
        }
        total_cost=Math.round(total_cost);
        return total_cost;
    }
}