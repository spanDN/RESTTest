package com.example.delle5540.resttest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by dell e5540 on 5/10/2018.
 */

public interface ObscureApi {
    @POST("sign_in")
    Call<Response<ResponseBody>> signIn( @Body RequestBody body
    );
}
