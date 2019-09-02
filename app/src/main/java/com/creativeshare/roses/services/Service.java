package com.creativeshare.roses.services;




import com.creativeshare.roses.models.Catogries_Model;
import com.creativeshare.roses.models.Markets_Model;
import com.creativeshare.roses.models.Slider_Model;
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
            @Query("page") int page


    );
    @GET("api/markets")
    Call<Markets_Model> getMarkets(
            @Query("page") int page


    );
}
