package com.example.delle5540.resttest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    AppCompatEditText edt_password;
    AppCompatEditText edt_email;
    AppCompatButton  Btn;
    OkHttpClient.Builder httpClient;
    String API_BASE_URL = "http://37.57.92.40:8084";
    Retrofit retrofit;
    TextView txt_result;

    protected String toJson(Object obj) throws NullPointerException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String mgson = gson.toJson(obj);
        return mgson;
    }

    protected String encode(String gson) {
        Log.d("RETy", "BaseInteractor encode() gson " + gson);
        try {
            byte[] enc = gson.getBytes("UTF-8");
            String b64 = Base64.encodeToString(enc, Base64.URL_SAFE);
            b64 = b64.replace("\n", "");
            b64 = b64.replace("\t", "");
            b64 = b64.replace("\r", "");
            return b64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("RETy", "BaseInteractor encode() UnsupportedEncodingException " + e.getMessage());
        }
        return null;
    }


    protected String decode(String gson) {
        Log.d("RETy", "BaseInteractor decode() gson " + gson);
        byte[] data = Base64.decode(gson, Base64.URL_SAFE);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("RETy", "BaseInteractor decode() UnsupportedEncodingException " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_password = (AppCompatEditText) findViewById(R.id.edt_passwd);
        edt_email = (AppCompatEditText) findViewById(R.id.edt_email);
        Btn = (AppCompatButton) findViewById(R.id.btn_request);
        txt_result = (TextView) findViewById(R.id.txt_result);


        httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit = builder.client( httpClient.build() ) .build();

        ObscureApi client =  retrofit.create(ObscureApi.class);
    }

    public void sendRequest(View view) {
        edt_password.setText("Test");

        // Create a very simple REST adapter which points the GitHub API endpoint.
        ObscureApi client =  retrofit.create(ObscureApi.class);

// Fetch a list of the Github repositories.

        CommonRequest req = new CommonRequest(edt_email.getText().toString(), edt_password.getText().toString());
        String json = toJson(req);
        String encoded = encode(json);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"data\""+encoded+"}");

        Call<Response<ResponseBody>> call = client.signIn( body );

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<Response<ResponseBody>>() {
            @Override
            public void onResponse(Call<Response<ResponseBody>> call, Response<Response<ResponseBody>> response) {
                Log.d("SPANTST ","OnResponse");

                if(!response.isSuccessful()){
//                    Gson gson = new Gson();
//                    MyErrorMessage message=gson.fromJson(response.errorBody().charStream(),MyErrorMessage.class);
                    try {
                        Log.d("SPANTST ", "!!!response " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.d("SPANTST ", "response " + String.valueOf(response.body().toString()));
                    txt_result.setText(response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<Response<ResponseBody>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                txt_result.setText(t.getMessage());
                Log.d("SPANTST ",t.getMessage());
            }
        });
    }
}
