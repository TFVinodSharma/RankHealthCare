package com.scriptmall.doctorbookphp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class ReviewActivity extends AppCompatActivity {

    ReviewAdapter mAdapter;
    RecyclerView recyclerView;
    private List<Book> reviewList = new ArrayList<>();
    ProgressDialog loading;
    String rname,rrate,rdesc,rdate,rimg,pid,uid,resp;
    Button give;

    EditText etreview,ettitle;
    RatingBar ratebar;
    Button send;
    String getrate,reviewet,title;
    String docid,docname,rid,rtitle,review_status,app_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        Intent i=getIntent();
        docid=i.getStringExtra("docid");
        review_status=i.getStringExtra("review_status");
        app_status=i.getStringExtra("app_status");
//        Toast.makeText(this, review_status+" "+app_status, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, getGridSpanCount(this)));
        mAdapter = new ReviewAdapter(this,reviewList);
        recyclerView.setAdapter(mAdapter);

//        prepairData();
//        getData();

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            getData();
        }

        give=(Button)findViewById(R.id.give);

        if(uid.equals(docid)){
            give.setVisibility(View.GONE);
        }

        if(review_status.equals("1")){
            give.setText("Edit Ratings");
        }

        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(app_status.equals("1")){
                    if(review_status.equals("1")){
                        showDialogForEditReview();
                    }else {
                        showDialogForReview();
                    }

                }else {
                    AlertDialog.Builder al=new AlertDialog.Builder(ReviewActivity.this);
                    al.setTitle("Sorry");
                    al.setMessage("You cant able to give ratings before getting appintment from this doctor");
                    al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    al.show();
                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reviews & Ratings");


    }



    public void prepairData(){
        Book near=new Book("M.Shankardass","4.5","nice to consulting with him","07-07-2017");
        reviewList.add(near);
        near=new Book("S.G.Shangamithra","5.0","good to consulting with him","01-07-2017");
        reviewList.add(near);
        near=new Book("S.G.Sagananthan","3.0","awesome to consulting ","10-06-2017");
        reviewList.add(near);
        near=new Book("M.Anandaraman","2.5","nice to consulting with him","12-05-2017");
        reviewList.add(near);
        near=new Book("M.Shankardass","4.0","nice to talk with him","29-03-2017");
        reviewList.add(near);

    }

    public void getData() {
        // Toast.makeText(getApplicationContext(),purpose,Toast.LENGTH_LONG).show();
        loading = ProgressDialog.show(ReviewActivity.this,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USER_REVIEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(ReviewActivity.this, response, Toast.LENGTH_SHORT).show();
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
                params.put(Config.DOCID,docid);

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
                docname=eve.getString(Config.USERNAME);
                rid=eve.getString(Config.RID);
                rdate = eve.getString(Config.RDATE);
                rdesc=eve.getString(Config.RDESC);
                rrate=eve.getString(Config.RATING);
                rtitle=eve.getString(Config.RTITLE);


//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Book ed=new Book();
//                ed.setDocid(docid);
                ed.setDocname(docname);
                ed.setRid(rid);
                ed.setRdesc(rdesc);
                ed.setRdate(rdate);
                ed.setRrate(rrate);
                ed.setRtitle(rtitle);

                reviewList.add(ed);
//                    Toast.makeText(getApplicationContext(), (CharSequence) eventsList,Toast.LENGTH_LONG).show();

            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));
            mAdapter = new ReviewAdapter(this, recyclerView, reviewList);
            recyclerView.setAdapter(mAdapter);
//
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showDialogForReview(){

        final Dialog dialog = new Dialog(ReviewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        etreview=(EditText)dialog.findViewById(R.id.etreview);
        ettitle=(EditText)dialog.findViewById(R.id.ettitle);
        ratebar=(RatingBar)dialog.findViewById(R.id.get_rating);
        send=(Button)dialog.findViewById(R.id.send);

        getrate="0.0";

        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                getrate= String.valueOf(rating);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewet=etreview.getText().toString().trim();
                title=ettitle.getText().toString().trim();
//                Toast.makeText(ReviewActivity.this, getrate, Toast.LENGTH_SHORT).show();
                if(!reviewet.equals("") && !title.equals("")){
                    submitToDB();
                    dialog.dismiss();
                }else {
                    Toast.makeText(ReviewActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);

    }

    private void showDialogForEditReview() {
        final Dialog dialog = new Dialog(ReviewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        etreview=(EditText)dialog.findViewById(R.id.etreview);
        ettitle=(EditText)dialog.findViewById(R.id.ettitle);
        ratebar=(RatingBar)dialog.findViewById(R.id.get_rating);
        send=(Button)dialog.findViewById(R.id.send);

        getReviewData();
        getrate="0.0";
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                getrate= String.valueOf(rating);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewet=etreview.getText().toString().trim();
                title=ettitle.getText().toString().trim();
//                Toast.makeText(ReviewActivity.this, getrate, Toast.LENGTH_SHORT).show();
                if(!reviewet.equals("") && !title.equals("")){
                    submitToDB();
                    dialog.dismiss();
                }else {
                    Toast.makeText(ReviewActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);
    }

    private void getReviewData() {
        loading = ProgressDialog.show(ReviewActivity.this,"Please wait...","Fetching...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.REVIEW_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJsonReviewDetails(response);
//                        Toast.makeText(ReviewActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String,String>();
                params.put(Config.UID,uid);
                params.put(Config.DOCID,docid);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJsonReviewDetails(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            getrate = eve.getString(Config.RATING);
            title = eve.getString(Config.RTITLE);
            reviewet = eve.getString(Config.RDESC);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        etreview.setText(reviewet);
        ettitle.setText(title);
        ratebar.setRating(Float.valueOf(getrate));
    }

    public void submitToDB(){
        reviewet=etreview.getText().toString().trim();
        title=ettitle.getText().toString().trim();

//        Toast.makeText(this, docid+" "+uid, Toast.LENGTH_SHORT).show();

        loading = ProgressDialog.show(ReviewActivity.this,"Please wait...","Fetching...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.REVIEW_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJsonPostReview(response);
//                        Toast.makeText(ReviewActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ReviewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String,String>();
                params.put(Config.RATING,getrate);
                params.put(Config.RDESC,reviewet);
                params.put(Config.RTITLE,title);
                params.put(Config.UID,uid);
                params.put(Config.DOCID,docid);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void showJsonPostReview(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            resp = eve.getString("Result");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        getData();
        Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
//        Intent i=new Intent(getApplicationContext(),DetailsActivity.class);
//        i.putExtra("docid",docid);
//        i.putExtra("review_status",review_status);
//        i.putExtra("app_status",app_status);
//        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
//        intent.putExtra("docid",docid);
//        startActivityForResult(intent, 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.prof, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
//                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
//                intent.putExtra("docid",docid);
//                startActivityForResult(intent, 0);
                break;
        }
        return true;
    }



}
