package com.example.delle5540.resttest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import okhttp3.OkHttpClient;
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
    String API_BASE_URL = "https://api.github.com/";
    Retrofit retrofit;
    TextView txt_result;

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

        GitHubClient client =  retrofit.create(GitHubClient.class);
    }

    public void sendRequest(View view) {
        edt_password.setText("Test");

        // Create a very simple REST adapter which points the GitHub API endpoint.
        GitHubClient client =  retrofit.create(GitHubClient.class);

// Fetch a list of the Github repositories.
        Call<List<GitHubRepo>> call = client.reposForUser(edt_email.getText().toString());

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                for (GitHubRepo input : response.body())
                {
                    Log.i("Repositories ",input.getName());
                    txt_result.setText(txt_result.getText() + "\n " + input.getName());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
            }
        });
    }
}
