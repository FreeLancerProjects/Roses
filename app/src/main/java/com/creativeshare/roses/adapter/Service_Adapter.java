package com.creativeshare.roses.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.roses.activites_fragments.home_activity.fragments.fragment_main.Fragment_Main;
import com.creativeshare.roses.models.Market_model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Service_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Market_model.MarketService> data;
    private Context context;
    private HomeActivity activity;
    private Fragment fragment;
    private Fragment_Main fragment_main;
private String current_lang;
    public Service_Adapter(List<Market_model.MarketService> data, Context context, Fragment fragment) {

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


            View view = LayoutInflater.from(context).inflate(R.layout.service_row, parent, false);
            return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {



            final MyHolder myHolder = (MyHolder) holder;
            final Market_model.MarketService data1 = data.get(myHolder.getAdapterPosition());
if(current_lang.equals("ar")) {
    ((MyHolder) holder).ch_service.setText(data1.getService_ar_title());
}
else {
    ((MyHolder) holder).ch_service.setText(data1.getService_en_title());

}

            //Log.e("msg",advertsing.getMain_image());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CheckBox ch_service;


        public MyHolder(View itemView) {
            super(itemView);

          ch_service = itemView.findViewById(R.id.chec_service);



        }

    }




}
