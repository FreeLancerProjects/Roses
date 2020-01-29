package com.creativeshare.roses.activites_fragments.activity_image;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import com.creativeshare.roses.R;
import com.creativeshare.roses.adapter.Shop_Products_Adapter;
import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.models.Product_Model;
import com.creativeshare.roses.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class Order_Image_Activity extends AppCompatActivity {


    private String lang;
    private ImageView arrowback, imdeisplay;
    private LinearLayout llback;
    private Product_Model.Data data;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("detials") != null) {
            data = (Product_Model.Data) getIntent().getExtras().getSerializable("detials");
        }


        initView();
        if (data != null) {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL + data.getImage())).fit().into(imdeisplay);

        }
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        llback = findViewById(R.id.ll_back);
        imdeisplay = findViewById(R.id.image);
        arrowback = findViewById(R.id.arrow_back);
        if (lang.equals("en")) {
            arrowback.setRotation(180.0f);
        }
        llback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

    }


    public void back() {
        finish();
    }


}
