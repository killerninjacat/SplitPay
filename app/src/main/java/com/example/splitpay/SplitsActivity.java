package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplitsActivity extends AppCompatActivity {
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    List<String> details;
    List<Integer> amts;
    List<SplitResponse> split;
    int u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splits);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiservice = retrofit.create(APIservice.class);
        details=new ArrayList<>();
        amts=new ArrayList<>();
        u_id=getIntent().getIntExtra("u_id",0);
        Call<List<SplitResponse>> getSplitsCall=apiservice.getallsplits(u_id);
        getSplitsCall.enqueue(new Callback<List<SplitResponse>>() {
            @Override
            public void onResponse(Call<List<SplitResponse>> call, Response<List<SplitResponse>> response) {
                if(response.isSuccessful())
                {
                    split=response.body();
                    for(SplitResponse sp:split)
                    {
                        details.add(sp.getTitle());
                        amts.add(sp.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SplitResponse>> call, Throwable t) {
                Log.d("getsplits",t.getMessage());
            }
        });
    }
}