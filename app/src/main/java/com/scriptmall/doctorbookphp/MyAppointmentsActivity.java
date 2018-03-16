package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAppointmentsActivity extends AppCompatActivity {

    MyAppointmentsAdapter mAdapter;
    RecyclerView recyclerView;
    private List<Book> reviewList = new ArrayList<>();
    ProgressDialog loading;
    String lname,bdate,btime,bstatus,bid,uid,uphone;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, getGridSpanCount(this)));


        mAdapter = new MyAppointmentsAdapter(this,reviewList);
        recyclerView.setAdapter(mAdapter);

//        prepairData();
//        getData();

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
//
//            img.setVisibility(View.VISIBLE);
//            lin1.setVisibility(View.GONE);
//
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
//            img.setVisibility(View.GONE);
//            lin1.setVisibility(View.VISIBLE);
            getData();
////                        Toast.makeText(getApplicationContext(), "Connection available", Toast.LENGTH_SHORT).show();
        }




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Appointments");


    }




    public void prepairData(){
        Book near=new Book("Gowtham Kumar","9578196348","10.00 AM","07-12-2017","0","");
        reviewList.add(near);
        near=new Book("Sivakumar","9786586463","12.00 PM","01-11-2017","1","");
        reviewList.add(near);
        near=new Book("Sathish","9626692026","9.30 AM","10-11-2017","3","");
        reviewList.add(near);
        near=new Book("Jeeva","8248867288","4.30 PM","12-10-2017","1","");
        reviewList.add(near);
        near=new Book("Lokeshbabu","9944844680","6.30 PM","29-09-2017","2","");
        reviewList.add(near);

    }

    public void getData() {
        // Toast.makeText(getApplicationContext(),purpose,Toast.LENGTH_LONG).show();
        loading = ProgressDialog.show(MyAppointmentsActivity.this,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USER_APPOINTMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"No Data found",Toast.LENGTH_LONG).show();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.DOCID,uid);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            reviewList.clear();
            for(int i=0;i<result.length();i++)
            {

                JSONObject eve = result.getJSONObject(i);
                bid = eve.getString(Config.BID);
                lname=eve.getString(Config.USERNAME);
                bdate=eve.getString(Config.BDATE);
                btime = eve.getString(Config.BTIME);
                bstatus=eve.getString(Config.BSTATUS);
                uphone=eve.getString(Config.UPHONENO);


//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Book ed=new Book();
                ed.setBid(bid);
                ed.setBdate(bdate);
                ed.setBtime(btime);
                ed.setLname(lname);
                ed.setBstatus(bstatus);
                ed.setLphone(uphone);

                reviewList.add(ed);
//                    Toast.makeText(getApplicationContext(), (CharSequence) eventsList,Toast.LENGTH_LONG).show();

            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));
            mAdapter = new MyAppointmentsAdapter(this, recyclerView, reviewList);
            recyclerView.setAdapter(mAdapter);
//
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){

            case android.R.id.home:
//                super.onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("status",status);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("status",status);
        startActivityForResult(intent, 0);
    }

}
