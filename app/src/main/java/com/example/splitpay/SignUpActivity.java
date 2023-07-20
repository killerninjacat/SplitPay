package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    Button signupbtn;
    int userId;
    EditText namebox,passwordbox;
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    String name,password;
    private SharedPreferences sharedPreferences;
    private void redirectToHome() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        intent.putExtra("userid", sharedPreferences.getInt("userId", 0));
        intent.putExtra("uname", sharedPreferences.getString("name1",""));
        startActivity(intent);
        finish();
    }
    private void saveLoginState(int userId, String usname) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putString("name1",usname);
        editor.putBoolean("isLoggedIn", true);
        editor.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupbtn=(Button) findViewById(R.id.signupbtn);
        namebox=(EditText) findViewById(R.id.editTextName);
        passwordbox=(EditText) findViewById(R.id.editTextPassword);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiservice = retrofit.create(APIservice.class);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=namebox.getText().toString();
                password=passwordbox.getText().toString();
                UserRequest userRequest = new UserRequest(name,password);
                userRequest.setEmail(name);
                userRequest.setPassword(password);
                Call<UserResponse> call = apiservice.createUser(userRequest);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            UserResponse userResponse = response.body();
                            Log.d("success","success"+userResponse.getEmail());
                            userId = response.body().getId();
                            saveLoginState(userId,name);
                            redirectToHome();
                        } else {
                            Log.d("Signupactivity", "Error: " + response.code());
                            Log.d("signupActivity", "Message: " + response.message());
                            Log.d("error","error msg: "+response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.d("signupactivity","failure"+t.getMessage());
                    }
                });
                Intent i2=new Intent(SignUpActivity.this,HomeActivity.class);
                i2.putExtra("uname",name);
                i2.putExtra("userid",userId);
                startActivity(i2);
            }
        });
    }
}