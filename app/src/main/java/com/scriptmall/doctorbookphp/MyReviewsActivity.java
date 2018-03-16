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

public class MyReviewsActivity extends AppCompatActivity {

    MyReviewAdapter mAdapter;
    RecyclerView recyclerView;
    private List<Book> reviewList = new ArrayList<>();
    ProgressDialog loading;
    String rname,rrate,rdesc,rdate,rimg,pid,uid;
    String docid,docname,rid;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

//        Intent i=getIntent();
//        status=i.getStringExtra("status");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, getGridSpanCount(this)));


//        mAdapter = new MyReviewAdapter(this,reviewList);
//        recyclerView.setAdapter(mAdapter);

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
        getSupportActionBar().setTitle("My Reviews");


    }

    public void prepairData(){
        Book near=new Book("Gowtham Kumar","","To discuss every thing with this doctor. he 'll give correct suggestion.He is the well experianced person","07-07-2017");
        reviewList.add(near);
        near=new Book("Sivakumar","","good to consulting with him","01-07-2017");
        reviewList.add(near);
        near=new Book("Sathish","","awesome to consulting ","10-06-2017");
        reviewList.add(near);
        near=new Book("Jeeva","","To discuss every thing with this doctor. he 'll give correct suggestion.He is the well experianced person","12-05-2017");
        reviewList.add(near);
        near=new Book("Lokeshbabu","","nice to talk with him","29-03-2017");
        reviewList.add(near);

    }

    public void getData() {
        // Toast.makeText(getApplicationContext(),purpose,Toast.LENGTH_LONG).show();
        loading = ProgressDialog.show(MyReviewsActivity.this,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MY_REVIEWS_URL,
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
                params.put(Config.UID,uid);

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
                docid = eve.getString(Config.DOCID);
                docname=eve.getString(Config.DOCNAME);
                rid=eve.getString(Config.RID);
                rdate = eve.getString(Config.RDATE);
                rdesc=eve.getString(Config.RDESC);


//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Book ed=new Book();
                ed.setDocid(docid);
                ed.setDocname(docname);
                ed.setRid(rid);
                ed.setRdesc(rdesc);
                ed.setRdate(rdate);

                reviewList.add(ed);
//                    Toast.makeText(getApplicationContext(), (CharSequence) eventsList,Toast.LENGTH_LONG).show();

            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));
            mAdapter = new MyReviewAdapter(this, recyclerView, reviewList);
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
