package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText namebox,passwordbox;
    Button loginbutton,signupbutton;
    String name,password;
    int id;
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    private boolean passwordcheck;
    private SharedPreferences sharedPreferences;
    private void saveLoginState(int userId, String usname) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putString("name1",usname);
        editor.putBoolean("isLoggedIn", true);
        editor.commit();
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    private void redirectToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("userid", sharedPreferences.getInt("userId", 0));
        intent.putExtra("uname", sharedPreferences.getString("name1",""));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namebox=(EditText) findViewById(R.id.editTextName);
        passwordbox=(EditText) findViewById(R.id.editTextPassword);
        loginbutton=(Button) findViewById(R.id.signupbtn);
        signupbutton=(Button) findViewById(R.id.signupbutton);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiservice = retrofit.create(APIservice.class);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        if (isLoggedIn()) {
            redirectToHome();
        }
        Intent i=new Intent(MainActivity.this,SignUpActivity.class);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=namebox.getText().toString();
                name=name.trim();
                password=passwordbox.getText().toString();
                Call<List<UserResponse>> getUsersCall = apiservice.getUsers();
                getUsersCall.enqueue(new Callback<List<UserResponse>>() {
                    @Override
                    public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                        if(response.isSuccessful())
                        {
                            List<UserResponse> users = response.body();
                            boolean exists=false;
                            Log.d("name","name: "+name);
                            Log.d("user.getusername","username: "+users.get(1).getEmail());
                            for(UserResponse user:users)
                            {
                                if(user.getEmail().equals(name))
                                {
                                    exists=true;
                                    id=user.getId();
                                    VerificationRequest verificationRequest=new VerificationRequest(name,password);
                                    verificationRequest.setEmail(name);
                                    verificationRequest.setPassword(password);
                                    Call<VerificationResponse> call1=apiservice.verifyPassword(verificationRequest);
                                    call1.enqueue(new Callback<VerificationResponse>() {
                                        @Override
                                        public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                                            if(response.isSuccessful()) {
                                                VerificationResponse verificationResponse = response.body();
                                                passwordcheck = verificationResponse.isPassword_correct();
                                                if (passwordcheck) {
                                                    saveLoginState(id,name);
                                                    redirectToHome();
                                                }
                                            }
                                            else
                                            {
                                                Log.d("verification", "Error: " + response.code());
                                                Log.d("verification", "Message: " + response.message());
                                                Log.d("error","error msg: "+response.errorBody());
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<VerificationResponse> call, Throwable t) {
                                            Log.d("verification","failure"+t.getMessage());
                                        }
                                    });
                                    break;
                                }
                            }
                            if(!exists)
                                Toast.makeText(MainActivity.this, "USER DOES NOT EXIST. CREATE A NEW ACCOUNT.", Toast.LENGTH_SHORT).show();
                            else if(!passwordcheck)
                                Toast.makeText(MainActivity.this,"INCORRECT PASSWORD",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Log.d("GetUsersError", "Error: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                        Log.d("GetUsersError", "Request failed: " + t.getMessage());
                    }
                });
            }
        });
    }
}