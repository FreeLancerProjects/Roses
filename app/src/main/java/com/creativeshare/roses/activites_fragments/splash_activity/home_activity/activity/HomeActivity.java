package com.creativeshare.roses.activites_fragments.splash_activity.home_activity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.creativeshare.roses.R;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_shop_profile.Fragment_Map;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_About;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_Bank_Account;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_cart_complete.Fragment_Cart;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Catogry;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_client_profile.Fragment_Client_profile;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_cart_complete.Fragment_Complete;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_Contact_Us;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_Edit_profile;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_main.Fragment_Markets;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_client_profile.fragmnet_client_order.fragment_ordres_detials.Fragment_Product_Details;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_Upgrade;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.Fragment_Home;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_main.Fragment_Main;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_More;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_shop_profile.fragment_shop_data.fragment_shop_department.Fragment_Shop_Products;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragment_shop_profile.Fragment_Shop_profile;
import com.creativeshare.roses.activites_fragments.splash_activity.home_activity.fragments.fragmnet_more.Fragment_Terms_Conditions;
import com.creativeshare.roses.activites_fragments.splash_activity.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.models.UserModel;
import com.creativeshare.roses.preferences.Preferences;
import com.creativeshare.roses.remote.Api;
import com.creativeshare.roses.share.Common;
import com.creativeshare.roses.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int fragment_count = 0;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    private Fragment_Home fragment_home;
    private Fragment_Main fragment_main;
    private Fragment_Catogry fragment_catogry;
    private Fragment_Client_profile fragment_client_profile;
    private Fragment_More fragment_more;
    private Fragment_Shop_profile fragment_shop_profile;
    private Fragment_Terms_Conditions fragmentTerms_conditions;
    private Fragment_Cart fragment_cart;
    private Fragment_Shop_Products fragment_shop_product;
    private Fragment_Complete fragment_complete;
    private Fragment_About fragment_about;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Bank_Account fragment_bank_account;
    private Fragment_Upgrade fragment_upgrade;
    private Fragment_Product_Details fragment_product_detials;
    private Fragment_Markets Fragment_markets;
    private Fragment_Edit_profile fragment_edit_profile;
    private Fragment_Map fragment_map;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        if (savedInstanceState == null) {
            //           CheckPermission();

            DisplayFragmentHome();
            DisplayFragmentMain();
        }

        if (userModel != null) {
            //       updateToken();
        }

    }


    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();
        String visitTime = preferences.getVisitTime(this);
        Calendar calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        String date = dateFormat.format(new Date(timeNow));

        if (!date.equals(visitTime)) {
            addVisit(date);
        }
    }


    public void DisplayFragmentHome() {
        fragment_count += 1;
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }

    }
    public void DisplayFragmentMap() {
        fragment_count += 1;

            fragment_map = Fragment_Map.newInstance();


        if (fragment_map.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_map).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_map, "fragment_map").addToBackStack("fragment_map").commit();

        }

    }

    public void DisplayFragmentMain() {
        if (fragment_main == null) {
            fragment_main = Fragment_Main.newInstance();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_catogry != null && fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_catogry).commit();
        }
        if (fragment_client_profile != null && fragment_client_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(0);
        }
    }

    public void DisplayFragmentCatogry() {
        if (fragment_catogry == null) {
            fragment_catogry = Fragment_Catogry.newInstance();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_client_profile != null && fragment_client_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }

        if (fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_catogry).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_catogry, "fragment_catogry").addToBackStack("fragment_catogry").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(1);
        }
    }

    public void DisplayFragmentclientprofile() {
        if (fragment_client_profile == null) {
            fragment_client_profile = Fragment_Client_profile.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_catogry != null && fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_catogry).commit();
        }

        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_client_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_client_profile, "fragment_client_profile").addToBackStack("fragment_client_profile").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(2);
        }
    }

    public void DisplayFragmentMore() {
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_catogry != null && fragment_catogry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_catogry).commit();
        }

        if (fragment_client_profile != null && fragment_client_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }
        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(3);
        }
    }

    public void DisplayFragmentShopprofile(int id) {
        fragment_count += 1;

        fragment_shop_profile = Fragment_Shop_profile.newInstance(id);


        if (fragment_shop_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_shop_profile).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_shop_profile, "fragment_shop_profile").addToBackStack("fragment_shop_profile").commit();

        }

    }

    public void DisplayFragmentEditprofile() {
        fragment_count += 1;

        fragment_edit_profile = Fragment_Edit_profile.newInstance();


        if (fragment_edit_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_edit_profile).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_edit_profile, "fragment_edit_profile").addToBackStack("fragment_edit_profile").commit();
            //  Log.e("llll","edits");

        }

    }

    public void DisplayFragmentProductdetials() {
        fragment_count += 1;

        fragment_product_detials = Fragment_Product_Details.newInstance();


        if (fragment_product_detials.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_product_detials).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_product_detials, "fragment_product_detials").addToBackStack("fragment_product_detials").commit();

        }

    }

    public void DisplayFragmentMarkets() {
        fragment_count += 1;

        Fragment_markets = Fragment_Markets.newInstance();


        if (Fragment_markets.isAdded()) {
            fragmentManager.beginTransaction().show(Fragment_markets).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, Fragment_markets, "Fragment_markets").addToBackStack("Fragment_markets").commit();

        }

    }

    public void DisplayFragmentTerms_Condition() {

        fragment_count += 1;
        fragmentTerms_conditions = Fragment_Terms_Conditions.newInstance();


        if (fragmentTerms_conditions.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentTerms_conditions).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentTerms_conditions, "fragmentTerms_conditions").addToBackStack("fragmentTerms_conditions").commit();

        }


    }

    public void DisplayFragmentUgrade() {

        fragment_count += 1;
        fragment_upgrade = Fragment_Upgrade.newInstance();


        if (fragment_upgrade.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_upgrade).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_upgrade, "fragment_upgrade").addToBackStack("fragment_upgrade").commit();

        }


    }

    public void DisplayFragmentabout() {

        fragment_count += 1;
        fragment_about = Fragment_About.newInstance();


        if (fragment_about.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_about).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_about, "fragment_about").addToBackStack("fragment_about").commit();

        }


    }

    public void DisplayFragmentContactUS() {

        fragment_count += 1;

        fragment_contact_us = Fragment_Contact_Us.newInstance();

        if (fragment_contact_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_contact_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact_us, "fragment_contact_us").addToBackStack("fragment_contact_us").commit();

        }


    }

    public void DisplayFragmentBankAccount() {

        fragment_count += 1;

        fragment_bank_account = Fragment_Bank_Account.newInstance();


        if (fragment_bank_account.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_bank_account).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_bank_account, "fragment_bank_account").addToBackStack("fragment_bank_account").commit();
        }

    }

    public void DisplayFragmentProduct() {

        fragment_count += 1;
        fragment_shop_product = Fragment_Shop_Products.newInstance();


        if (fragment_shop_product.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_shop_product).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_shop_product, "fragment_shop_product").addToBackStack("fragment_shop_product").commit();

        }


    }

    public void DisplayFragmentComplete() {

        fragment_count += 1;
        fragment_complete = Fragment_Complete.newInstance();


        if (fragment_complete.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_complete).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_complete, "fragment_complete").addToBackStack("fragment_complete").commit();

        }


    }

    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_count > 1) {
            fragment_count -= 1;
            super.onBackPressed();
        } else {

            if (fragment_home != null && fragment_home.isVisible()) {
                if (fragment_main != null && fragment_main.isVisible()) {
                    if (userModel == null) {
                        Common.CreateUserNotSignInAlertDialog(this);
                    } else {
                        finish();
                    }
                } else {
                    DisplayFragmentMain();
                }
            } else {
                DisplayFragmentHome();
            }
        }

    }

    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, Login_Activity.class);
        intent.putExtra("sign_up", isSignIn);
        startActivity(intent);
        finish();

    }

    public void Logout() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .Logout(userModel.getId() + "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            /*new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            manager.cancelAll();
                                        }
                                    },1);
                            userSingleTone.clear(ClientHomeActivity.this);*/
                            preferences.create_update_userdata(HomeActivity.this, null);
                            preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                            Intent intent = new Intent(HomeActivity.this, Login_Activity.class);
                            startActivity(intent);
                            finish();
                            if (cuurent_language.equals("ar")) {
                                //  overridePendingTransition(R.anim.from_left,R.anim.to_right);


                            } else {
                                //  overridePendingTransition(R.anim.from_right,R.anim.to_left);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void addVisit(final String timeNow) {

        Api.getService(Tags.base_url)
                .updateVisit(1, timeNow)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            preferences.saveVisitTime(HomeActivity.this, timeNow);
                            // Log.e("msg",response.body().toString());

                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    public void display_Cart() {
        fragment_count += 1;
        fragment_cart = Fragment_Cart.newInstance();
        if (fragment_cart.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_cart).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_cart, "fragment_cart").addToBackStack("fragment_cart").commit();

        }

    }

    public void RefreshActivity(String lang) {
        //Log.e("lang",selected_language);
        Paper.book().write("lang", lang);
        preferences.create_update_language(this, lang);
        preferences.setIsLanguageSelected(this);
        Language.setNewLocale(this, lang);

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }, 1050);


    }

    public void animate(RoundedImageView im_cart, int quantity) {
        if (fragment_shop_profile != null && fragment_shop_profile.isAdded()) {
            fragment_shop_profile.makeFlyAnimation(im_cart, quantity);
        }
    }

    public void getamount() {
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.gettotal();
        }
        if (fragment_shop_profile != null && fragment_shop_profile.isAdded()) {
            fragment_shop_profile.gettotal();
        }
        if (fragment_shop_product != null && fragment_shop_product.isAdded()) {
            fragment_shop_product.gettotal();
        }
    }

    public void refresh(int type) {
        getamount();
        if (fragment_client_profile != null ) {
            if(fragment_shop_product!=null&&fragment_shop_product.isVisible()){
                Back();
            }
            if(fragment_shop_profile!=null&&fragment_shop_profile.isVisible()){
                Back();
            }
            Back();

            Back();

            DisplayFragmentclientprofile();
            fragment_client_profile.refresh(type);


        }
        else {
            if(fragment_shop_product!=null&&fragment_shop_product.isVisible()){
                Back();
            }
            if(fragment_shop_profile!=null&&fragment_shop_profile.isVisible()){
                Back();
            }
            Back();

            Back();

            DisplayFragmentclientprofile();

        }
    }
}