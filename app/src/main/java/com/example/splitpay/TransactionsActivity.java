package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransactionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Button new_transaction;
    List<TransactionResponse> transaction;
    List <String> details,displaylist;
    List<Integer> amts;
    String detail;
    ArrayAdapter<String> arr;
    ListView transactionslist;
    int amt,u_id;
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    public void newtransactiondialog() {
        EditText detailsbox,amountbox;
        Button credit,debit;
        final Dialog dialog = new Dialog(TransactionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.newtransaction);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        detailsbox=dialog.findViewById(R.id.detail);
        amountbox=dialog.findViewById(R.id.amount);
        credit=dialog.findViewById(R.id.credit);
        debit=dialog.findViewById(R.id.debit);
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debit","onclick"+amountbox.getText());
                String s1,s2;
                if (!amountbox.getText().toString().isEmpty() && !detailsbox.getText().toString().isEmpty())
                {
                    detail = detailsbox.getText().toString();
                    amt = Integer.parseInt(amountbox.getText().toString());
                    amt=0-amt;
                    TransactionRequest transactionRequest = new TransactionRequest(detail, amt);
                    transactionRequest.setTitle(detail);
                    transactionRequest.setDescription(amt);
                    Call<TransactionResponse> call = apiservice.createTransaction(u_id, transactionRequest);
                    call.enqueue(new Callback<TransactionResponse>() {
                        @Override
                        public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                            if (response.isSuccessful()) {
                            } else {
                                Log.d("signupactivity", "Code: " + response.code());
                                Log.d("signupActivity", "Message: " + response.message());
                                Log.d("error", "error msg: " + response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<TransactionResponse> call, Throwable t) {
                            Log.d("transactionsactivity", "failure" + t.getMessage());
                        }
                    });
                    dialog.dismiss();
                    displaylist.add("  "+detail+"        "+amt);
                    arr.notifyDataSetChanged();
                    transactionslist.invalidateViews();
                }
                else
                    Toast.makeText(TransactionsActivity.this,"Enter the values",Toast.LENGTH_SHORT).show();
            }
        });
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amountbox.getText().toString().isEmpty() && !detailsbox.getText().toString().isEmpty())
                {
                    detail = detailsbox.getText().toString();
                amt = Integer.parseInt(amountbox.getText().toString());
                TransactionRequest transactionRequest = new TransactionRequest(detail, amt);
                transactionRequest.setTitle(detail);
                transactionRequest.setDescription(amt);
                Call<TransactionResponse> call = apiservice.createTransaction(u_id, transactionRequest);
                call.enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                        if (response.isSuccessful()) {
                        } else {
                            Log.d("signupactivity", "Code: " + response.code());
                            Log.d("signupActivity", "Message: " + response.message());
                            Log.d("error", "error msg: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Log.d("transactionsactivity", "failure" + t.getMessage());
                    }
                });
                dialog.dismiss();
                    displaylist.add("  "+detail+"        "+amt);
                    arr.notifyDataSetChanged();
                    transactionslist.invalidateViews();
            }
                else
                    Toast.makeText(TransactionsActivity.this,"Enter the values",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        transactionslist=(ListView) findViewById(R.id.transactionslist);
        new_transaction=(Button) findViewById(R.id.new_transaction);
        u_id=getIntent().getIntExtra("u_id",0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiservice = retrofit.create(APIservice.class);
        details=new ArrayList<>();
        displaylist=new ArrayList<>();
        amts=new ArrayList<>();
        Call<List<TransactionResponse>> getTransactionsCall=apiservice.getalltransactions(u_id);
        getTransactionsCall.enqueue(new Callback<List<TransactionResponse>>() {
            @Override
            public void onResponse(Call<List<TransactionResponse>> call, Response<List<TransactionResponse>> response) {
                if(response.isSuccessful())
                {
                    transaction=response.body();
                    for(TransactionResponse transac:transaction)
                    {
                        details.add(transac.getTitle());
                        amts.add(transac.getDescription());
                    }
                    for(int j=0;j<details.size();j++)
                        displaylist.add(j,"  "+details.get(j)+"        "+amts.get(j));
                    Log.d("value",displaylist.get(0));
                    arr.notifyDataSetChanged();
                    transactionslist.invalidateViews();
                }
            }

            @Override
            public void onFailure(Call<List<TransactionResponse>> call, Throwable t) {
                Log.d("gettransactions",t.getMessage());
            }
        });
        arr=new ArrayAdapter<String>(this,R.layout.eachitem,R.id.name,displaylist);
        transactionslist.setAdapter(arr);
        transactionslist.setOnItemClickListener(this);
        new_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newtransactiondialog();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Handle item click here
        String selectedItem = displaylist.get(position);
        // Perform any desired action with the selected item
        Toast.makeText(this, "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
    }
}