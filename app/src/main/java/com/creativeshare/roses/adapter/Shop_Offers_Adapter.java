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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_shop_profile.fragment_shop_data.Fragment_Shop_Offers;
import com.creativeshare.roses.models.Offer_Model;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Shop_Offers_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Offer_Model.Data> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private String current_lang;
    private Fragment_Shop_Offers fragment_shop_offers;
    private Preferences pr;
    private List<Integer> ids;
    public Shop_Offers_Adapter(List<Offer_Model.Data> data,List<Integer> ids, Context context, Fragment fragment) {

        this.data = data;
        this.context = context;
        activity = (HomeActivity) context;
        this.fragment = fragment;
        this.ids=ids;
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        pr = Preferences.getInstance();

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
            final Offer_Model.Data data1 = data.get(myHolder.getAdapterPosition());
            if (current_lang.equals("en")) {
                ((MyHolder) holder).tv_name.setText(data1.getProduct_en_title());
                ((MyHolder) holder).tv_offer.setText(data1.getEn_title());


            } else {
                ((MyHolder) holder).tv_name.setText(data1.getProduct_ar_title());
                ((MyHolder) holder).tv_offer.setText(data1.getAr_title());


            }
            ((MyHolder) holder).tv_price.setText(context.getResources().getString(R.string.price) + " " + data1.getProduct_price());

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getImage())).fit().into(((MyHolder) holder).im_offer);
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getImage())).fit().into(((MyHolder) holder).im_cart);

            ((MyHolder) holder).cons_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment instanceof Fragment_Shop_Offers) {
                        Log.e("lll",data.get(holder.getLayoutPosition()).getMarket_id()+"");

                        fragment_shop_offers = (Fragment_Shop_Offers) fragment;
                            fragment_shop_offers.setproduct(data.get(holder.getLayoutPosition()),((MyHolder) holder).im_cart);


                    }
                }
            });
            if(!ids.contains(data1.getProduct_id())){
                ((MyHolder) holder).imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cart));
            }
            else {
                ((MyHolder) holder).imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cart));

            }
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
        private RoundedImageView im_offer,im_cart;
        private ConstraintLayout cons_cart;
        private ImageView imageView;

        private View view;
        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);

            im_offer = itemView.findViewById(R.id.im_offer);
            im_cart = itemView.findViewById(R.id.im_cart);
            imageView=itemView.findViewById(R.id.image);

            tv_offer = itemView.findViewById(R.id.tv_offer);
            cons_cart = itemView.findViewById(R.id.cons_cart);
            view=itemView.findViewById(R.id.view);
            if(current_lang.equals("ar")){
                tv_offer.setRotation(-45f);
                view.setRotation(-45f);

            }
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
        Offer_Model.Data data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
