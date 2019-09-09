package com.creativeshare.roses.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.models.Order_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Client_Order_Detials_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Order_Model.Data.OrderDetails> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private String current_lang;

    public Client_Order_Detials_Adapter(List<Order_Model.Data.OrderDetails> data, Context context, Fragment fragment) {

        this.data = data;
        this.context = context;
        activity = (HomeActivity) context;
        this.fragment = fragment;
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.order_detials_row, parent, false);
            return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


            final MyHolder myHolder = (MyHolder) holder;
            final Order_Model.Data.OrderDetails data1 = data.get(myHolder.getAdapterPosition());

           // ((MyHolder) holder).tv_price.setText("6555");
if(current_lang.equals("ar")){
    ((MyHolder) holder).tv_name.setText(data1.getProduct_ar_title());

}
else {
    ((MyHolder) holder).tv_name.setText(data1.getProduct_en_title());

}
            ((MyHolder) holder).tv_price.setText(data1.getTotal_price()+context.getResources().getString(R.string.ryal));
            ((MyHolder) holder).tv_quantity.setText(data1.getAmount()+context.getResources().getString(R.string.bouquet));



            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getProduct_default_image())).fit().into(((MyHolder) holder).im_order);


            //Log.e("msg",advertsing.getMain_image());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_price, tv_quantity;
        private ImageView im_order;

        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvboquename);
            tv_price = itemView.findViewById(R.id.tvprice);
            tv_quantity = itemView.findViewById(R.id.tvquantity);
            im_order = itemView.findViewById(R.id.im1);


        }

    }


}
