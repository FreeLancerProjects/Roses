package com.creativeshare.roses.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_language(Context context, String lang) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", lang);
        editor.apply();


    }

    public String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        return preferences.getString("lang", Locale.getDefault().getLanguage());

    }

    public void setIsLanguageSelected(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("language_selected", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("selected",true);
        editor.apply();
    }

    public boolean isLanguageSelected(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("language_selected", Context.MODE_PRIVATE);
        return preferences.getBoolean("selected",false);
    }

    public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }

    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }
    public void saveVisitTime(Context context, String time)
    {
        SharedPreferences preferences = context.getSharedPreferences("visit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("time",time);
        editor.apply();
    }



    public String getVisitTime(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("visit", Context.MODE_PRIVATE);
        return preferences.getString("time","");
    }
    public void create_update_order(Context context, List<Add_Order_Model> buy_models){
        SharedPreferences sharedPreferences=context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String user_order=gson.toJson(buy_models);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user_order",user_order);
        editor.apply();
editor.commit();
    }
    public ArrayList<Add_Order_Model> getUserOrder(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_order = preferences.getString("user_order",null);
        Type type=new TypeToken<ArrayList<Add_Order_Model>>(){}.getType();
        ArrayList<Add_Order_Model> buy_models=gson.fromJson(user_order,type);
        return buy_models;
    }

}
