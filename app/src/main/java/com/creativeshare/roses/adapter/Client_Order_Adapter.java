package com.creativeshare.roses.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
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
import com.creativeshare.roses.models.Offer_Model;
import com.creativeshare.roses.models.Order_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Client_Order_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Order_Model.Data> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private String current_lang;

    public Client_Order_Adapter(List<Order_Model.Data> data, Context context, Fragment fragment) {

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

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            final MyHolder myHolder = (MyHolder) holder;
            final Order_Model.Data data1 = data.get(myHolder.getAdapterPosition());
         if(data1.getType()==1){
            ((MyHolder) holder).tv_name.setText(data1.getMarket_name());
             ((MyHolder)holder).tv1.setText(activity.getResources().getString(R.string.store_name));

         }
         else {
             ((MyHolder)holder).tv_name.setText(data1.getTitle());
             ((MyHolder)holder).tv1.setText(activity.getResources().getString(R.string.ocasion_name));

         }
            ((MyHolder) holder).tv_phone.setText(data1.getMarket_phone());
           // ((MyHolder) holder).tv_price.setText("6555");

            ((MyHolder) holder).tv_price.setText(data1.getTotal_cost()+context.getResources().getString(R.string.ryal));
            ((MyHolder) holder).tv_quantity.setText(data1.getOrderDetails().size()+context.getResources().getString(R.string.bouquet));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
            String date = dateFormat.format(new Date(data1.getNext_date()*1000));
            ((MyHolder) holder).tv_date.setText(date);

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getMarket_image())).fit().into(((MyHolder) holder).im_order);
            ((MyHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Send_Data.setData(data.get(holder.getLayoutPosition()));
                }
            });

            //Log.e("msg",advertsing.getMain_image());
        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_phone, tv_price, tv_quantity,tv_date,tv1;
        private ImageView im_order;

        public MyHolder(View itemView) {
            super(itemView);
tv1=itemView.findViewById(R.id.tv1);
            tv_name = itemView.findViewById(R.id.tvstorename);
            tv_phone = itemView.findViewById(R.id.tvPhone);
            tv_price = itemView.findViewById(R.id.tvprice);
            tv_quantity = itemView.findViewById(R.id.tvquantity);
            tv_date=itemView.findViewById(R.id.tv_date);
            im_order = itemView.findViewById(R.id.im1);


        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
      Order_Model.Data data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
