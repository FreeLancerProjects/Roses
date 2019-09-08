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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Catogry;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Catogries_Text_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Catogries_Model.Data> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private Fragment_Catogry fragment_catogry;
private String current_lang;
    public Catogries_Text_Adapter(List<Catogries_Model.Data> data, Context context, Fragment fragment) {

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


            View view = LayoutInflater.from(context).inflate(R.layout.catogry_row, parent, false);
            return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {



            final MyHolder myHolder = (MyHolder) holder;
            final Catogries_Model.Data data1 = data.get(myHolder.getAdapterPosition());
if(current_lang.equals("en")) {
    ((MyHolder) holder).tv_name.setText(data1.getEn_title());
}
else {
    ((MyHolder) holder).tv_name.setText(data1.getAr_title());
    Log.e("lll",data1.getAr_title());
}

        ((MyHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof  Fragment_Catogry){
                    fragment_catogry=(Fragment_Catogry)fragment;
                    fragment_catogry.getMarkets(data.get(holder.getLayoutPosition()).getId());
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
        private TextView tv_name;

        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);


        }

    }


}
