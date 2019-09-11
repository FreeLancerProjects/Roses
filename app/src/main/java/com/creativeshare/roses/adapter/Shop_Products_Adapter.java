package com.creativeshare.roses.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Shop_Products;
import com.creativeshare.roses.models.Offer_Model;
import com.creativeshare.roses.models.Product_Model;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Shop_Products_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Product_Model.Data> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private String current_lang;
    private Fragment_Shop_Products fragment_shop_products;
private Preferences pr;
    public Shop_Products_Adapter(List<Product_Model.Data> data, Context context, Fragment fragment) {

        this.data = data;
        this.context = context;
        activity = (HomeActivity) context;
        this.fragment = fragment;
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
pr=Preferences.getInstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.boqute_row, parent, false);
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
            final Product_Model.Data data1 = data.get(myHolder.getAdapterPosition());
            if (current_lang.equals("en")) {
                ((MyHolder) holder).tv_name.setText(data1.getEn_title());

            } else {
                ((MyHolder) holder).tv_name.setText(data1.getAr_title());
                //Log.e("lll", data1.getAr_title());

            }
            ((MyHolder) holder).tv_price.setText(context.getResources().getString(R.string.price) + " " + data1.getPrice());
            ((MyHolder) holder).cons_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fragment instanceof  Fragment_Shop_Products){
                            fragment_shop_products=(Fragment_Shop_Products)fragment;
                            Log.e("lll",data.get(holder.getLayoutPosition()).getMarket_id()+"");
                            fragment_shop_products.setproduct(data.get(holder.getLayoutPosition()),((MyHolder) holder).im_cart);


                    }
                }
            });
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getImage())).fit().into(((MyHolder) holder).im_cart);

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getImage())).fit().into(((MyHolder) holder).im_offer);


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
        private TextView tv_name, tv_price, tv_offer;
        private ConstraintLayout cons_cart;
        private View view;
        private RoundedImageView im_offer,im_cart;

        public MyHolder(View itemView) {
            super(itemView);
            cons_cart = itemView.findViewById(R.id.cons_cart);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);

            im_offer = itemView.findViewById(R.id.im_offer);
            im_cart = itemView.findViewById(R.id.im_cart);

            tv_offer = itemView.findViewById(R.id.tv_offer);
           view = itemView.findViewById(R.id.view);
            tv_offer.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

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
        Product_Model.Data data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
