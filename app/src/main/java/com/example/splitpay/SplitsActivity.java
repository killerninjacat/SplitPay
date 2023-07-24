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

public class SplitsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String baseurl = "http://127.0.0.1:8000/";
    private APIservice apiservice;
    List<String> details, displaylist,splitlist,allnames,temp;
    List<Integer> amts;
    List<SplitResponse> split;
    String desc;
    int u_id;
    ArrayAdapter<String> arr;
    ListView splitslist,eachsplitlist;
    Button new_split;
    boolean e;
    public interface UserCheckCallback1 {
        void onResult(boolean userExists);
    }
    public void usercheck(String name, UserCheckCallback1 callback) {
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
    public void splitviewdialog(String names,String titlesp,int tot_amt)
    {
        final Dialog dialog = new Dialog(SplitsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.eachsplit);
        dialog.getWindow().setLayout(1000, 1000);
        temp=new ArrayList<>();
        eachsplitlist=dialog.findViewById(R.id.eachsplitview);
        TextView titleofsplit,amountofsplit;
        titleofsplit=dialog.findViewById(R.id.titleofsplit);
        amountofsplit=dialog.findViewById(R.id.totalofsplit);
        amountofsplit.setText("TOTAL: "+tot_amt);
        titleofsplit.setText(titlesp);
        String wd="";int c=0;
        float am1=tot_amt;
        Log.d("names","names: "+names);
        for(int j=0;j<names.length();j++)
        {
            if(names.charAt(j)=='%')
                c++;
        }
        for(int i=0;i<names.length();i++)
        {
            if(names.charAt(i)!='%')
                wd=wd+names.charAt(i);
            else {
                if(!wd.isEmpty())
                   temp.add(wd+"     "+am1/c);
                wd="";
            }
        }
        Log.d("splitviewdialog", "temp contents: " + temp.toString());
        ArrayAdapter<String> arr1 = new ArrayAdapter<String>(this, R.layout.eachitem, R.id.name, temp);
        eachsplitlist.setAdapter(arr1);
        dialog.show();
    }
    public void newsplitdialog()
    {
        splitlist.clear();
        Button add,save;
        EditText name,amt,title;
        final Dialog dialog = new Dialog(SplitsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.newsplit);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        add=dialog.findViewById(R.id.add);
        save=dialog.findViewById(R.id.save);
        title=dialog.findViewById(R.id.splittitle);
        name=dialog.findViewById(R.id.name1);
        amt=dialog.findViewById(R.id.amount);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usercheck(name.getText().toString(), new UserCheckCallback1() {
                    @Override
                    public void onResult(boolean userExists) {
                        if(!userExists)
                            Toast.makeText(SplitsActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        else
                        {
                            if(splitlist.contains(name.getText().toString()))
                                Toast.makeText(SplitsActivity.this,"User already added",Toast.LENGTH_SHORT).show();
                            else {
                                splitlist.add(name.getText().toString());
                                Toast.makeText(SplitsActivity.this,name.getText().toString()+" added!",Toast.LENGTH_SHORT).show();
                                name.setText("");
                            }
                        }
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String splitfinal="";
                if(!splitlist.isEmpty()&&!amt.getText().toString().isEmpty())
                {
                    splitfinal="";int amtfinal;
                    amtfinal=Integer.parseInt(amt.getText().toString());
                    for(int i=0;i<splitlist.size();i++)
                    {
                        splitfinal=splitfinal+splitlist.get(i)+"%";
                    }
                    SplitRequest splitRequest=new SplitRequest(title.getText().toString(),splitfinal,amtfinal);
                    Call<SplitResponse> call=apiservice.createSplit(u_id, splitRequest);
                    call.enqueue(new Callback<SplitResponse>() {
                        @Override
                        public void onResponse(Call<SplitResponse> call, Response<SplitResponse> response) {
                            if (response.isSuccessful()) {
                            } else {
                                Log.d("splitsactivity", "Code: " + response.code());
                                Log.d("splitsActivity", "Message: " + response.message());
                                Log.d("error", "error msg: " + response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<SplitResponse> call, Throwable t) {
                            Log.d("splitsactivity", "failure" + t.getMessage());
                        }
                    });
                }
                dialog.dismiss();
                displaylist.add(title.getText().toString()+"        "+amt.getText());
                details.add(title.getText().toString());
                allnames.add(splitfinal);
                amts.add(Integer.parseInt(amt.getText().toString()));
                arr.notifyDataSetChanged();
                splitslist.invalidateViews();
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splits);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiservice = retrofit.create(APIservice.class);
        details = new ArrayList<>();
        splitlist=new ArrayList<>();
        displaylist=new ArrayList<>();
        allnames=new ArrayList<>();
        splitslist=(ListView) findViewById(R.id.allsplits);
        amts = new ArrayList<>();
        new_split=(Button) findViewById(R.id.new_split);
        u_id = getIntent().getIntExtra("u_id", 0);
        Log.d("user id","user id: "+u_id);
        Call<List<SplitResponse>> getSplitsCall = apiservice.getallsplits(u_id);
        getSplitsCall.enqueue(new Callback<List<SplitResponse>>() {
            @Override
            public void onResponse(Call<List<SplitResponse>> call, Response<List<SplitResponse>> response) {
                if (response.isSuccessful()) {
                    split = response.body();
                    for (SplitResponse sp : split) {
                        details.add(sp.getTitle());
                        amts.add(sp.getDescription());
                        allnames.add(sp.getNames());
                    }
                    for(int j=0;j<details.size();j++)
                        displaylist.add(j,details.get(j)+"        "+amts.get(j));
                    arr.notifyDataSetChanged();
                    splitslist.invalidateViews();
                }
            }

            @Override
            public void onFailure(Call<List<SplitResponse>> call, Throwable t) {
                Log.d("getsplits", t.getMessage());
            }
        });
        arr = new ArrayAdapter<String>(this, R.layout.eachitem, R.id.name, displaylist);
        splitslist.setAdapter(arr);
        splitslist.setOnItemClickListener(this);
        new_split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsplitdialog();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        splitviewdialog(allnames.get(position),details.get(position),amts.get(position));
    }
}
