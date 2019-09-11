package com.creativeshare.roses.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Cart;
import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Cart_Adpter extends RecyclerView.Adapter<Cart_Adpter.Eyas_Holder> {
    List<Add_Order_Model.Order_details> list;
    Context context;
    HomeActivity activity;
    Preferences preferences;
    private String current_lang;
private Fragment_Cart fragment_cart;
int index;
Data_Cart_Adapter data_cart_adapter;
    public Cart_Adpter(List<Add_Order_Model.Order_details> list, Context context, Fragment_Cart fragment_cart, int index, Data_Cart_Adapter data_cart_adapter) {
        this.list = list;
        this.context = context;
        activity = (HomeActivity) context;
        preferences = Preferences.getInstance();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment_cart=fragment_cart;
        this.index=index;
        this.data_cart_adapter=data_cart_adapter;
    }

    @Override
    public Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row, viewGroup, false);
        Eyas_Holder eas = new Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Cart_Adpter.Eyas_Holder viewHolder, int i) {
        Add_Order_Model.Order_details model = list.get(i);
        if (current_lang.equals("ar")) {
            viewHolder.tv_name.setText(model.getAr_name() + "");

        } else {
            viewHolder.tv_name.setText(model.getEn_name() + "");

        }
        viewHolder.tv_price.setText(model.getTotal_price() + "");
        viewHolder.tv_amount.setText(model.getAmount() + "");
        Log.e("im", model.getImage());
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + model.getImage())).fit().into(viewHolder.im_produce);
        viewHolder.im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(viewHolder.getLayoutPosition());
            }
        });
viewHolder.im_increase.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        updateincrease(viewHolder.getLayoutPosition());
    }
});
        viewHolder.im_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedecrease(viewHolder.getLayoutPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_price, tv_amount;
        private ImageView im_produce, im_delete, im_increase, im_decrease;
        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            // frameLayout = itemView.findViewById(R.id.image_cart);
            im_delete = itemView.findViewById(R.id.image_delete);
            im_produce = itemView.findViewById(R.id.image);
            im_increase = itemView.findViewById(R.id.image_increase);
            im_decrease = itemView.findViewById(R.id.image_decrease);
        }

    }

    private void deleteItem(int position) {
        List<Add_Order_Model> add_order_model = preferences.getUserOrder(activity);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        add_order_model.get(index).setOrder_details(list);

        if(!list.isEmpty()){
        preferences.create_update_order(activity,add_order_model );

        }
        else {
            preferences.create_update_order(activity,null);
            data_cart_adapter.list.remove(index);
            data_cart_adapter.notifyDataSetChanged();
        }
        fragment_cart.gettotal();
data_cart_adapter.notifyDataSetChanged();
    }
    private void updateincrease(int position) {
        List<Add_Order_Model> add_order_model = preferences.getUserOrder(activity);
        Add_Order_Model.Order_details order_details=list.get(position);
        order_details.setTotal_price(order_details.getTotal_price()+(order_details.getTotal_price()/order_details.getAmount()));
        order_details.setAmount(order_details.getAmount()+1);

        list.set(position,order_details);
       notifyDataSetChanged();
        add_order_model.get(index).setOrder_details(list);
        preferences.create_update_order(activity, add_order_model);
        fragment_cart.gettotal();
        data_cart_adapter.notifyDataSetChanged();

    }
    private void updatedecrease(int position) {
       List< Add_Order_Model> add_order_model = preferences.getUserOrder(activity);
        Add_Order_Model.Order_details order_details=list.get(position);
        if(order_details.getAmount()>1){
        order_details.setTotal_price(order_details.getTotal_price()-(order_details.getTotal_price()/order_details.getAmount()));
            order_details.setAmount(order_details.getAmount()-1);

            list.set(position,order_details);
        notifyDataSetChanged();
        add_order_model.get(index).setOrder_details(list);
        preferences.create_update_order(activity, add_order_model);
            fragment_cart.gettotal();
            data_cart_adapter.notifyDataSetChanged();

        }

    }

}


