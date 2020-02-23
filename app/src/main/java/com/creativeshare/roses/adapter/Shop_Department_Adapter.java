package com.creativeshare.roses.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Send_Data;
import com.creativeshare.roses.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Shop_Department_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;

    private List<Catogries_Model.Data> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private Fragment_Home fragment_home;
    private String current_lang;

    public Shop_Department_Adapter(List<Catogries_Model.Data> data, Context context, Fragment fragment) {

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

        View view = LayoutInflater.from(context).inflate(R.layout.department_row, parent, false);
        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


        final MyHolder myHolder = (MyHolder) holder;
        final Catogries_Model.Data data1 = data.get(myHolder.getAdapterPosition());
        if (current_lang.equals("en")) {
            ((MyHolder) holder).tv_title.setText(data1.getEn_title());
        } else {
            ((MyHolder) holder).tv_title.setText(data1.getAr_title());

        }
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + data1.getImage())).fit().into(((MyHolder) holder).im_depart);
        ((MyHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof Fragment_Home) {
                    fragment_home = (Fragment_Home) fragment;
                    fragment_home.mDrawer.closeDrawers();
                    Send_Data.setCat_id(data.get(holder.getLayoutPosition()).getId());
                    activity.DisplayFragmentMarkets();
                    Send_Data.setCat_id(data.get(holder.getLayoutPosition()).getId());

                } else {
                    Send_Data.setCat_id(data.get(holder.getLayoutPosition()).getId());
                    activity.DisplayFragmentProduct();
                }

            }
        });

        //Log.e("msg",advertsing.getMain_image());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private RoundedImageView im_depart;

        public MyHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            im_depart = itemView.findViewById(R.id.im_depart);


        }

    }


}
