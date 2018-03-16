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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    String lname,lphone,laddr,lepx,lrate,lspl,docid,rimg,review_status,app_status;
    private List<Book> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    SearchResultAdapter mAdapter;
    ProgressDialog loading;
    ArrayList<Integer> alImage;
    String key,city,cat,uid;

    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        Intent i=getIntent();
        key=i.getStringExtra("key");
        city=i.getStringExtra("city");
        cat=i.getStringExtra("cat");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));

        alImage = new ArrayList<>(Arrays.asList(R.drawable.aa, R.drawable.ba, R.drawable.ca,R.drawable.da, R.drawable.ea));

        mAdapter = new SearchResultAdapter(this,productList,alImage);
        recyclerView.setAdapter(mAdapter);

//        prepairData();


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else {
            getData();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");

    }

    public void prepairData(){
        Book just=new Book("SRI T"," 9789876781","Flat No.3, Car Street,Vadalur","15","4.5","Dentist");
        productList.add(just);
        just=new Book("Prabha K"," 9785642525","35, Raja St, Chennai","28","4.0","Dabetologists");
        productList.add(just);
        just=new Book("Prabhjit Jauhar"," 9876543210","57, Main Road, Kodambakkam, Chennai","35","3.25","Anesthesiologist, Cardiologist, Dabetologists");
        productList.add(just);
        just=new Book("Josva S"," 9785642525","35, Raja St, Chennai","18","2.5","Allergist");
        productList.add(just);
        just=new Book("Googly"," 9876543210","57, Main Road, Kodambakkam, Chennai","10","2.0","Anesthesiologist, Cardiologist");
        productList.add(just);
    }

    public void getData() {
        // Toast.makeText(getApplicationContext(),purpose,Toast.LENGTH_LONG).show();
        loading = ProgressDialog.show(SearchResultActivity.this,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(SearchResultActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"No results found",Toast.LENGTH_LONG).show();
                        SearchResultActivity.super.onBackPressed();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.CITY,city);
                params.put("key",key);
                params.put(Config.CATNAME,cat);
//                params.put(Config.UID,uid);

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
            productList.clear();
            for(int i=0;i<result.length();i++)
            {
                JSONObject eve = result.getJSONObject(i);
                docid = eve.getString(Config.DOCID);
                lname=eve.getString(Config.DOCNAME);
                lphone=eve.getString(Config.UPHONENO);
                laddr = eve.getString(Config.DOCADDR);
                lepx=eve.getString(Config.EXP);
                lrate = eve.getString(Config.RATING);
                lspl=eve.getString(Config.EXPIN);
                rimg=eve.getString(Config.UIMG);
//                app_status=eve.getString(Config.APPSTATUS);
//                review_status=eve.getString(Config.RSTATUS);

                Book ed=new Book();
                ed.setDocid(docid);
                ed.setLphone(lphone);
                ed.setLaddr(laddr);
                ed.setLname(lname);
                ed.setLrate(lrate);
                ed.setLepx(lepx);
                ed.setLspl(lspl);
                ed.setRimg(rimg);
//                ed.setReview_status(review_status);
//                ed.setApp_status(app_status);

                productList.add(ed);

            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));
            mAdapter = new SearchResultAdapter(this, recyclerView, productList);
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
