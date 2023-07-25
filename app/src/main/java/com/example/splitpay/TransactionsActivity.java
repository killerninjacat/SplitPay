package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
    List<Boolean> isSettled;
    List<Integer> amts,ids;
    String detail;
    boolean exists,e;
    ArrayAdapter<String> arr;
    ListView transactionslist;
    int amt,u_id,transaction_id;
    List<Integer> settled_names;
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    public void settledialog(String n,int a, int pos, int ids1)
    {
        Button settle;
        TextView n1,a1;
        boolean settled=false;
        final Dialog dialog = new Dialog(TransactionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.settlepayment);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        settle=dialog.findViewById(R.id.settlebtn);
        n1=dialog.findViewById(R.id.nm);
        a1=dialog.findViewById(R.id.amt);
        a1.setText(a+"");
        n1.setText(n);
        if(settled_names.contains(ids1)) {
            settle.setText("Settled");
            settle.setBackgroundResource(R.drawable.settled);
            settled=true;
        }
        if(!settled) {
            settle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<Void> settletr=apiservice.settleTransaction(u_id,ids1,true);
                    settletr.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                    settled_names.add(ids1);
                    dialog.dismiss();
                    isSettled.set(pos,true);
                    displaylist.set(pos,"  "+details.get(pos)+"        "+amts.get(pos));
                    arr.notifyDataSetChanged();
                    transactionslist.invalidateViews();
                }
            });
        }
        dialog.show();
    }
    public interface UserCheckCallback {
        void onResult(boolean userExists);
    }
    public void usercheck(String name,UserCheckCallback callback) {
        Call<List<UserResponse>> getUsersCall = apiservice.getUsers();
        getUsersCall.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if(response.isSuccessful())
                {
                    e=false;
                    List<UserResponse> users=response.body();
                    for(UserResponse user:users)
                    {
                        if(user.getEmail().equals(name)) {
                            e = true;
                            break;
                        }
                    }
                    callback.onResult(e);
                }
            }
            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {

            }
        });
    }
    public void newtransactiondialog() {
        EditText detailsbox, amountbox;
        Button credit, debit;
        exists = false;
        final Dialog dialog = new Dialog(TransactionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.newtransaction);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        detailsbox = dialog.findViewById(R.id.name1);
        amountbox = dialog.findViewById(R.id.amount);
        credit = dialog.findViewById(R.id.credit);
        debit = dialog.findViewById(R.id.debit);
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usercheck(detailsbox.getText().toString(), new UserCheckCallback() {
                    @Override
                    public void onResult(boolean userExists) {
                        if (!userExists)
                            Toast.makeText(TransactionsActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        else {
                            if (!amountbox.getText().toString().isEmpty() && !detailsbox.getText().toString().isEmpty()) {
                                detail = detailsbox.getText().toString();
                                amt = Integer.parseInt(amountbox.getText().toString());
                                amt = 0 - amt;
                                TransactionRequest transactionRequest = new TransactionRequest(detail, amt, false);
                                transactionRequest.setTitle(detail);
                                transactionRequest.setDescription(amt);
                                transactionRequest.setSettled(false);
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
                                /*if (details.contains(detail)) {
                                    displaylist.set(details.indexOf(detail),detail + "        " + (amts.get(details.indexOf(detail)) + amt));
                                    amts.set(details.indexOf(detail),amts.get(details.indexOf(detail))+amt);
                                }
                                else {*/
                                    displaylist.add(detail + "        " + amt);
                                    amts.add(amt);

                                details.add(detail);
                                ids.add(ids.get(ids.size()-1)+1);
                                isSettled.add(false);
                                arr.notifyDataSetChanged();
                                transactionslist.invalidateViews();
                            } else
                                Toast.makeText(TransactionsActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usercheck(detailsbox.getText().toString(), new UserCheckCallback() {
                    @Override
                    public void onResult(boolean userExists) {
                        if (!userExists)
                            Toast.makeText(TransactionsActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        else {
                            if (!amountbox.getText().toString().isEmpty() && !detailsbox.getText().toString().isEmpty()) {
                                detail = detailsbox.getText().toString();
                                amt = Integer.parseInt(amountbox.getText().toString());
                                TransactionRequest transactionRequest = new TransactionRequest(detail, amt, false);
                                transactionRequest.setTitle(detail);
                                transactionRequest.setDescription(amt);
                                transactionRequest.setSettled(false);
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
                                /*if (details.contains(detail)) {
                                    displaylist.set(details.indexOf(detail),detail + "        " + (amts.get(details.indexOf(detail)) + amt));
                                    amts.set(details.indexOf(detail),amts.get(details.indexOf(detail))+amt);
                                }
                                else {*/
                                    displaylist.add(detail + "        " + amt);
                                    amts.add(amt);

                                details.add(detail);
                                ids.add(ids.get(ids.size()-1)+1);
                                isSettled.add(false);
                                arr.notifyDataSetChanged();
                                transactionslist.invalidateViews();
                            } else
                                Toast.makeText(TransactionsActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        ids=new ArrayList<>();
        settled_names=new ArrayList<>();
        isSettled=new ArrayList<>();
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
                        ids.add(transac.getId());
                        isSettled.add(transac.isSettled());
                        if(transac.isSettled()) {
                            settled_names.add(transac.getId());
                            Log.d("settled_names","settled_names"+transac.getTitle());
                        }
                    }
                    /*for(int f=0;f<details.size();f++)
                    {
                        String t=details.get(f);
                        int r=amts.get(f);
                        for(int f1=f+1;f1<details.size();f1++)
                        {
                            if(t.equals(details.get(f1))) {
                                r+=amts.get(f1);
                                settled_names.remove(details.get(f1));
                                details.remove(f1);
                                ids.remove(f1);
                                isSettled.remove(f1);
                                amts.remove(f1);
                                f1--;
                            }
                        }
                        amts.set(f,r);
                    }*/
                    for(int j=0;j<details.size();j++)
                        displaylist.add(j,details.get(j)+"        "+amts.get(j));
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
        Log.d("sizes","sizes: "+details.size()+" "+amts.size());
        settledialog(details.get(position),amts.get(position), position,ids.get(position));
    }
}