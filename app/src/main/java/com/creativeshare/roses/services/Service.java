package com.creativeshare.roses.services;




import com.creativeshare.roses.models.Add_Order_Model;
import com.creativeshare.roses.models.AppDataModel;
import com.creativeshare.roses.models.BankDataModel;
import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Market_model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.models.Offer_Model;
import com.creativeshare.roses.models.One_Order_Model;
import com.creativeshare.roses.models.Order_Model;
import com.creativeshare.roses.models.PlaceGeocodeData;
import com.creativeshare.roses.models.PlaceMapDetailsData;
import com.creativeshare.roses.models.Product_Model;
import com.creativeshare.roses.models.Slider_Model;
import com.creativeshare.roses.models.SocialDataModel;
import com.creativeshare.roses.models.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Service {
    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> Signup(
                           @Field("name") String name,
                           @Field("phone") String phone,
                           @Field("phone_code") String phone_code,
                           @Field("software_type") String software_type,
                           @Field("password") String password
    );
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> Signin(
            @Field("phone") String phone,
            @Field("phone_code") String phone_code,
            @Field("password") String password
    );
    @GET("api/slider")
    Call<Slider_Model> get_slider();
    @GET("api/categories")
    Call<Catogries_Model> getDepartment(



    );
    @FormUrlEncoded
    @POST("api/single-market")
    Call<Market_model> getsinglemarkey(

            @Field("market_id") int market_id



    );
    @FormUrlEncoded
    @POST("api/market/cats")
    Call<Catogries_Model> getDepartment(
            @Field("market_id") int market_id



    );
    @FormUrlEncoded
    @POST("api/market/offers")
    Call<Offer_Model> getoffer(
            @Field("page") int page,
            @Field("market_id") int market_id



    );
    @FormUrlEncoded
    @POST("api/products/cat/market")
    Call<Product_Model> getproducts(
            @Field("page") int page,
            @Field("cat_id") int cat_id,
            @Field("market_id") int market_id




            );
    @FormUrlEncoded
    @POST("api/my-orders/type")
    Call<Order_Model> getorders(
            @Field("page") int page,
            @Field("user_id") int user_id,
            @Field("type")int type



    );
    @FormUrlEncoded
    @POST("api/cat/markets")
    Call<Markets_Model> getmarkets(
            @Field("cat_id") int cat_id



    );
    @GET("api/markets")
    Call<Markets_Model> getMarkets(
            @Query("page") int page


    );
    @GET("api/about-us")
    Call<AppDataModel> getterms(


    );
    @GET("api/about-us")
    Call<AppDataModel> getabout(


    );
    @FormUrlEncoded
    @POST("api/visit")
    Call<ResponseBody> updateVisit(@Field("software_type") int software_type, @Field("date") String date);
    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> Logout(@Field("user_id") String user_id

    );
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @POST("api/order/create")
    Call<One_Order_Model> accept_orders(@Body Add_Order_Model add_order_model);
    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contact_us(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("message") String message,
                                  @Field("subject") String subject

    );
    @GET("api/banks")
    Call<BankDataModel> getBankAccount();
    @GET("api/all-social")
    Call<SocialDataModel> getSocial();
}
