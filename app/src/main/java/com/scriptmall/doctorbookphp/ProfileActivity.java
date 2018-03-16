package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView etufname,etulname,etumail,etuphone,etcoutry,etcity,etstate,etaddr,etzip;
    ProgressDialog loading;
    String uid,ufname,ulname,umail,uaddr,country,uphone,city,zip,state,uarea,ubuild,ustreet,uimg;
    ImageView img;
    LinearLayout lin1;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");


        etufname = (TextView) findViewById(R.id.tv_name);
//        etulname = (TextView) findViewById(R.id.etulname);
        etumail = (TextView) findViewById(R.id.tv_email);
        etuphone = (TextView) findViewById(R.id.tv_phone);
        etcoutry = (TextView) findViewById(R.id.etcoutry);
        etcity = (TextView) findViewById(R.id.etcity);
        etstate = (TextView) findViewById(R.id.etstate);
        etaddr = (TextView) findViewById(R.id.etaddr);
        etzip = (TextView) findViewById(R.id.etzip);

        lin1=(LinearLayout)findViewById(R.id.lin1);

        img=(ImageView)findViewById(R.id.uimg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPhotoActivity.class);
                intent.putExtra("uimg",uimg);
                startActivity(intent);
            }
        });





//        img.setVisibility(View.GONE);

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
        getSupportActionBar().setTitle("My Profile");


    }

    private void getData() {

        loading = ProgressDialog.show(ProfileActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PROFILE_URL,
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
                        Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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

            JSONObject eve = result.getJSONObject(0);
            ulname=eve.getString(Config.ULNAME);
            ufname = eve.getString(Config.UFNAME);
            uphone = eve.getString(Config.UPHONENO);
            umail = eve.getString(Config.UMAIL);
            ubuild = eve.getString(Config.UBUILD);
            ustreet = eve.getString(Config.USTREET);
            uarea = eve.getString(Config.UAREA);
            city = eve.getString(Config.CITY);
            country = eve.getString(Config.COUNTRY);
            state = eve.getString(Config.STATE);
            zip = eve.getString(Config.ZIP);
            uimg = eve.getString(Config.UIMG);




        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        Picasso
                .with(this)
                .load(Config.IMG_URL+uimg)
                .into(img);

        etcity.setText(city);
        etcoutry.setText(country);
        etstate.setText(state);
        etuphone.setText(uphone);
        etumail.setText(umail);
        etufname.setText(ufname+" "+ulname);
        etaddr.setText(ubuild+","+ustreet+","+uarea);
        etzip.setText(zip);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.send:
                Intent i = new Intent(getApplicationContext(), EditprofileActivity.class);
                i.putExtra("ulname",ulname);
                i.putExtra("ufname",ufname);
                i.putExtra("umail",umail);
                i.putExtra("uphone",uphone);
                i.putExtra("city",city);
                i.putExtra("country",country);
                i.putExtra("state",state);
                i.putExtra("uarea",uarea);
                i.putExtra("ustreet",ustreet);
                i.putExtra("ubuild",ubuild);
                i.putExtra("zip",zip);
                startActivityForResult(i, 0);
                break;
            case android.R.id.home:Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("status",status);
                startActivityForResult(intent, 0);
                break;
            case R.id.send1:
                final Intent in;
                in = new Intent(this, PasswordUpdateActivity.class);
//                in.putExtra("uid",uid);
                startActivity(in);

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
