package com.creativeshare.roses.services;




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
}
