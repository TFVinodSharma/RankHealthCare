package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class EditDoctorprofileActivity extends AppCompatActivity {

    EditText etufname,etulname,etuphone,etuaddr,et_zip,etarea,etbuild,etstreet;
    TextView etumail;
    String uid,ufname,ulname,uphone,umail,uaddr,country,city,state,zip,uarea,ubuild,ustreet;
    Spinner spincountry1,spinstate1,spincity1;
    private ArrayList<String> countrylist,stateList,cityList;
    ProgressDialog loading;
    String resp;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctorprofile);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            getCountryname();
        }

        Intent i=getIntent();
        ufname=i.getStringExtra("ufname");
        ulname=i.getStringExtra("ulname");
        umail=i.getStringExtra("umail");
        uphone=i.getStringExtra("uphone");
        city=i.getStringExtra("city");
        country=i.getStringExtra("country");
        state=i.getStringExtra("state");
        zip=i.getStringExtra("zip");
        uarea=i.getStringExtra("uarea");
        ubuild=i.getStringExtra("ubuild");
        ustreet=i.getStringExtra("ustreet");


        etufname=(EditText)findViewById(R.id.etufname);
        etulname=(EditText)findViewById(R.id.etulname);
        etuphone=(EditText)findViewById(R.id.etuphone);
        etumail=(TextView)findViewById(R.id.etumail);
        etarea=(EditText)findViewById(R.id.etarea);
        etbuild=(EditText)findViewById(R.id.etbuild);
        etstreet=(EditText)findViewById(R.id.etstreet);
        et_zip=(EditText)findViewById(R.id.etzip);
        spincountry1=(Spinner)findViewById(R.id.spincountry1);
        spincity1=(Spinner)findViewById(R.id.spincity1);
        spinstate1=(Spinner)findViewById(R.id.spinstate1);

        etufname.setText(ufname);
        etulname.setText(ulname);
        etuphone.setText(uphone);
        etumail.setText(umail);
        et_zip.setText(zip);
        etarea.setText(uarea);
        etbuild.setText(ubuild);
        etstreet.setText(ustreet);

//        loadSpinnerData();
//        getCountryname();
//        getStatename1(country);
//        getCityname1(state);

        countrylist= new ArrayList<String>();
        stateList= new ArrayList<String>();
        cityList= new ArrayList<String>();

        spincountry1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                country=spincountry1.getSelectedItem().toString();
                String a=country;
                a=a.replaceAll(" ","%20");
                getStatename1(country);
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spinstate1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                state=spinstate1.getSelectedItem().toString();
                String a=state;
                a=a.replaceAll(" ","%20");
                getCityname1(state);
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spincity1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                city=spincity1.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    insertdb();
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
    }

    private void getCountryname() {
        loading = ProgressDialog.show(EditDoctorprofileActivity.this,"Please wait...","Fetching...",false,false);
        final String url = Config.COUNTRY_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSONspinner(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EditprofileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                        Toast.makeText(EditprofileActivity.this,"country", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONspinner(String response) {
        if (!response.toLowerCase().trim().equals("failure")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                for(int i=0;i<result.length();i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    countrylist.add(collegeData.getString(Config.COUNTRY));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            spincountry1.setAdapter(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, countrylist));
            spincountry1.setSelection(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, countrylist).getPosition(country));
        }
    }

    private void getStatename1(final String country) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSONStatespinner(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EditprofileActivity.this,"state",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.COUNTRY,country);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSONStatespinner(String response) {
        if (!response.toLowerCase().trim().equals("failure")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                stateList.clear();
                for(int i=0;i<result.length();i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    stateList.add(collegeData.getString(Config.STATE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            spinstate1.setAdapter(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, stateList));
            spinstate1.setSelection(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, stateList).getPosition(state));
        }
    }

    private void getCityname1(final String state) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSONCityspinner(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EditprofileActivity.this,"city",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.STATE,state);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSONCityspinner(String response) {
        if (!response.toLowerCase().trim().equals("failure")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                cityList.clear();
                for(int i=0;i<result.length();i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    cityList.add(collegeData.getString(Config.CITY));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            spincity1.setAdapter(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, cityList));
            spincity1.setSelection(new ArrayAdapter<String>(EditDoctorprofileActivity.this, R.layout.simple_spinner_item, cityList).getPosition(city));
        }
    }
    void insertdb(){

        ufname = etufname.getText().toString().trim();
        ulname = etulname.getText().toString().trim();
        uphone = etuphone.getText().toString().trim();
//        umail = etumail.getText().toString().trim();
        uarea = etarea.getText().toString().trim();
        ubuild = etbuild.getText().toString().trim();
        ustreet = etstreet.getText().toString().trim();
        zip = et_zip.getText().toString().trim();


        loading = ProgressDialog.show(EditDoctorprofileActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EDITPROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(EditprofileActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Toast.makeText(AddNewActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.ULNAME,ulname);
                params.put(Config.UFNAME,ufname);
                params.put(Config.UPHONENO,uphone);
                params.put(Config.UMAIL,umail);
                params.put(Config.CITY,city);
                params.put(Config.COUNTRY,country);
                params.put(Config.STATE,state);
                params.put(Config.ZIP,zip);
                params.put(Config.UAREA,uarea);
                params.put(Config.UBUILD,ubuild);
                params.put(Config.USTREET,ustreet);
                params.put(Config.UID,uid);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);

            resp = eve.getString("Result");
//            Toast.makeText(this, resp, Toast.LENGTH_SHORT).show();
            if (resp.equals("success")) {
                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ProfileDoctorActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Can't be updated, Try again", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadSpinnerData(){
        List<String> l5 = new ArrayList<String>();
        l5.add("---Select---");
        l5.add("India"); l5.add("America");
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l5);
        dataAdapter5.setDropDownViewResource(R.layout.simple_spinner_item);
        spincountry1.setAdapter(dataAdapter5);

        List<String> l6 = new ArrayList<String>();
        l6.add("---Select---");
        l6.add("Tamil Nadu"); l6.add("Kerala");
        ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l6);
        dataAdapter6.setDropDownViewResource(R.layout.simple_spinner_item);
        spinstate1.setAdapter(dataAdapter6);

        List<String> l7 = new ArrayList<String>();
        l7.add("---Select---");
        l7.add("Chennai"); l7.add("Tiruvannamalai");
        ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l7);
        dataAdapter7.setDropDownViewResource(R.layout.simple_spinner_item);
        spincity1.setAdapter(dataAdapter7);
    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //overridePendingTransition(R.anim.blank_anim,R.anim.left_to_right);
        Intent i = new Intent(getApplicationContext(), ProfileDoctorActivity.class);
        startActivityForResult(i, 0);

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
//            case R.id.send:
//                final Intent intent;
//                intent = new Intent(this, PasswordUpdateActivity.class);
//                intent.putExtra("uid",uid);
//                startActivity(intent);
//                break;
            case android.R.id.home:
//                super.onBackPressed();
                Intent i = new Intent(getApplicationContext(), ProfileDoctorActivity.class);
                startActivityForResult(i, 0);
                break;
        }
        return true;
    }



}
