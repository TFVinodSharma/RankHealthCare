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

public class ProfileDoctorActivity extends AppCompatActivity {

    TextView etlname,etlmail,etlphone,etbarid,etissudate,etexpin,etsince,etpin,etfees,etlanguage,etophone,etweb,etcountry,etcity,etstate,etaddr,etoaddr,etzip,etabt;
    String fname,lname,email,phone,barid,issudate,expin,since,pin,fees,lamguage,ophone,web,country,city,state,addr,oaddr,zip,abt;
    String mon_from,mon_to,tue_from,tue_to,wed_from,wed_to,thurs_from,thurs_to,fri_from,fri_to,sat_from,sat_to,sun_from,sun_to;

    String ubuild,ustreet,uarea,ubuild1,ustreet1,uarea1;

    TextView mon,tue,wed,thurs,fri,sat,sun;
    String lmon,ltue,lwed,lthurs,lfri,lsat,lsun;
    ImageView img;

    String status,uimg,uid;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        etlname=(TextView)findViewById(R.id.etlname);
        etlmail=(TextView)findViewById(R.id.etlmail);
        etlphone=(TextView)findViewById(R.id.etlphone);

        etbarid=(TextView)findViewById(R.id.etbarid);
        etissudate=(TextView)findViewById(R.id.etissuedate);
        etexpin=(TextView)findViewById(R.id.etexpin);
        etsince=(TextView)findViewById(R.id.etsince);
        etpin=(TextView)findViewById(R.id.etpin);
        etfees=(TextView)findViewById(R.id.etfees);
        etlanguage=(TextView)findViewById(R.id.etlanguage);

        etophone=(TextView)findViewById(R.id.etophone);
        etweb=(TextView)findViewById(R.id.etweb);
        etcountry=(TextView)findViewById(R.id.etcountry);
        etcity=(TextView)findViewById(R.id.etcity);
        etstate=(TextView)findViewById(R.id.etstate);
        etaddr=(TextView)findViewById(R.id.etaddr);
        etoaddr=(TextView)findViewById(R.id.etoaddr);
        etzip=(TextView)findViewById(R.id.etzip);
        etabt=(TextView)findViewById(R.id.etabt);

        mon=(TextView)findViewById(R.id.mon);
        tue=(TextView)findViewById(R.id.tue);
        wed=(TextView)findViewById(R.id.wed);
        thurs=(TextView)findViewById(R.id.thurs);
        fri=(TextView)findViewById(R.id.fri);
        sat=(TextView)findViewById(R.id.sat);
        sun=(TextView)findViewById(R.id.sun);

        img=(ImageView)findViewById(R.id.uimg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditDoctorPhotoActivity.class);
                intent.putExtra("uimg",uimg);
                startActivity(intent);
            }
        });

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            getData();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

    }

    private void getData() {

            loading = ProgressDialog.show(ProfileDoctorActivity.this,"Please wait...","Fetching...",false,false);
            loading.setCanceledOnTouchOutside(true);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PROFILE_DOCTOR_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            showJSON(response);
//                        Toast.makeText(ProfileDoctorActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProfileDoctorActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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

            expin = eve.getString(Config.EXPIN);
            fname = eve.getString(Config.UFNAME);
            lname=eve.getString(Config.ULNAME);
            phone = eve.getString(Config.UPHONENO);
            email = eve.getString(Config.UMAIL);
            ubuild = eve.getString(Config.UBUILD);
            ustreet = eve.getString(Config.USTREET);
            uarea = eve.getString(Config.UAREA);
            ubuild1 = eve.getString(Config.UBUILD1);
            ustreet1 = eve.getString(Config.USTREET1);
            uarea1 = eve.getString(Config.UAREA1);
            country = eve.getString(Config.COUNTRY);
            state = eve.getString(Config.STATE);
            city = eve.getString(Config.CITY);
            zip = eve.getString(Config.ZIP);
            uimg = eve.getString(Config.UIMG);

            barid=eve.getString(Config.BARID);
            issudate = eve.getString(Config.ISSUEDATE);
            since = eve.getString(Config.SINCE);
            pin = eve.getString(Config.PIN);
            fees = eve.getString(Config.FEES);
            lamguage = eve.getString(Config.LANG);
            ophone = eve.getString(Config.OPHONE);
            web = eve.getString(Config.WEBSITE);
            abt = eve.getString(Config.ABOUT);


            mon_from=eve.getString(Config.MONFROM);
            mon_to = eve.getString(Config.MONTO);
            tue_from = eve.getString(Config.TUEFROM);
            tue_to = eve.getString(Config.TUETO);
            wed_from = eve.getString(Config.WEDFROM);
            wed_to = eve.getString(Config.WEDTO);
            thurs_from = eve.getString(Config.THURSFROM);
            thurs_to = eve.getString(Config.THURSTO);
            fri_from = eve.getString(Config.FRIFROM);
            fri_to = eve.getString(Config.FRITO);
            sat_from = eve.getString(Config.SATFROM);
            sat_to = eve.getString(Config.SATTO);
            sun_from = eve.getString(Config.SUNFROM);
            sun_to = eve.getString(Config.SUNTO);

            Picasso
                    .with(this)
                    .load(Config.IMG_URL+uimg)
                    .into(img);

            etcity.setText(city);
            etcountry.setText(country);
            etstate.setText(state);
            etlphone.setText(phone);
            etlmail.setText(email);
            etlname.setText(fname+" "+lname);
            etaddr.setText(ubuild+","+ustreet+","+uarea);
            etzip.setText(zip);

            etbarid.setText(barid);
            etissudate.setText(issudate);
            etexpin.setText(expin);
            etsince.setText(since);
            etpin.setText(pin);
            etfees.setText(fees+"Hr");
            etlanguage.setText(lamguage);
            etophone.setText(ophone);
            etweb.setText(web);
            etoaddr.setText(ubuild1+","+ustreet1+","+uarea1);
            etabt.setText(abt);

            if(sat_from.equals("")){
                sat.setText("Holiday");
                sat.setTextColor(getResources().getColor(R.color.red));
            }else {
                sat.setText(sat_from+" - "+sat_to);
            }

            if(sun_from.equals("")){
                sun.setText("Holiday");
                sun.setTextColor(getResources().getColor(R.color.red));
            }else {
                sun.setText(sun_from+" - "+sun_to);
            }
            if(mon_from.equals("")){
                mon.setText("Holiday");
                mon.setTextColor(getResources().getColor(R.color.red));
            }else {
                mon.setText(mon_from+" - "+mon_to);
            }
            if(tue_from.equals("")){
                tue.setText("Holiday");
                tue.setTextColor(getResources().getColor(R.color.red));
            }else {
                tue.setText(tue_from+" - "+tue_to);
            }
            if(wed_from.equals("")){
                wed.setText("Holiday");
                wed.setTextColor(getResources().getColor(R.color.red));
            }else {
                wed.setText(wed_from+" - "+wed_to);
            }
            if(thurs_from.equals("")){
                thurs.setText("Holiday");
                thurs.setTextColor(getResources().getColor(R.color.red));
            }else {
                thurs.setText(thurs_from+" - "+thurs_to);
            }
            if(fri_from.equals("")){
                fri.setText("Holiday");
                fri.setTextColor(getResources().getColor(R.color.red));
            }else {
                fri.setText(fri_from+" - "+fri_to);
            }

//            mon.setText(mon_from+" - "+mon_to);
//            tue.setText(tue_from+" - "+tue_to);
//            wed.setText(wed_from+" - "+wed_to);
//            thurs.setText(thurs_from+" - "+thurs_to);
//            fri.setText(fri_from+" - "+fri_to);
//            sat.setText(sat_from+" - "+sat_to);
//            sun.setText(sun_from+" - "+sun_to);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }





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
                Intent i = new Intent(getApplicationContext(), EditDoctorprofileActivity.class);
                i.putExtra("ulname",lname);
                i.putExtra("ufname",fname);
                i.putExtra("umail",email);
                i.putExtra("uphone",phone);
                i.putExtra("city",city);
                i.putExtra("country",country);
                i.putExtra("state",state);
                i.putExtra("uarea",uarea);
                i.putExtra("ubuild",ubuild);
                i.putExtra("ustreet",ustreet);
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
