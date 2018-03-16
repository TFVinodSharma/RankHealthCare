package com.scriptmall.doctorbookphp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    EditText etufname,etulname,etumail,etuphone,etupwd,etucpwd;
    String ufname,ulname,umail,uphone,upwd,ucpwd;

    Button reg;
    CheckBox check;

    String resp;
    String logo,terms_str;
    ImageView logo_img;
    TextView terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        logo = sharedPreferences.getString(Config.LOGO_SHARED_PREF,"Not Available");
        logo_img=(ImageView)findViewById(R.id.img);
        Picasso.with(this).load(logo).into(logo_img);


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else {
            getTerms();
        }

        etufname=(EditText)findViewById(R.id.etufname);
        etumail=(EditText)findViewById(R.id.etumail);
        etuphone=(EditText)findViewById(R.id.etuphone);
        etulname=(EditText)findViewById(R.id.etulname);
        etupwd=(EditText)findViewById(R.id.etupwd);
        etucpwd=(EditText)findViewById(R.id.etucpwd);

        terms=(TextView) findViewById(R.id.terms);

        check=(CheckBox)findViewById(R.id.check);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder al = new AlertDialog.Builder(RegisterActivity.this);
                al.setTitle("Terms and Condition");
                al.setMessage(terms_str);
                al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                al.show();
            }
        });

        reg=(Button)findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upwd=etupwd.getText().toString().trim();
                ucpwd=etucpwd.getText().toString().trim();
                umail=etumail.getText().toString().trim();
                ufname=etufname.getText().toString().trim();
                ulname=etulname.getText().toString().trim();
                uphone=etuphone.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(!ufname.equals("") && !ulname.equals("") &&!umail.equals("") &&!uphone.equals("") &&!upwd.equals("") &&!ucpwd.equals("")){
                    if(check.isChecked()){
                        if(umail.matches(emailPattern)) {
                            if(upwd.equals(ucpwd)){

                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                if (netInfo == null) {
                                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                }else {
                                    submitToDb();
                                }

                            }else{
                                Toast.makeText(RegisterActivity.this,"Password Does not Match...:)", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Enter correct email address", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please Accept Terms & Condition", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please Enter All Details", Toast.LENGTH_SHORT).show();
                }



            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

    }

    private void getTerms() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.TERMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // showLogoJSON(response);
//                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(), Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*private void showLogoJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            terms_str = eve.getString("terms");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

    private void submitToDb() {

        final String fname = etufname.getText().toString().trim();
        final String lname = etulname.getText().toString().trim();
        final String mailid = etumail.getText().toString().trim();
        final String pwd = etupwd.getText().toString().trim();
        final String phone = etuphone.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USERREG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.UFNAME,fname);
                params.put(Config.ULNAME,lname);
                params.put(Config.UMAIL,mailid);
                params.put(Config.UPWD,pwd);
                params.put(Config.UPHONENO,phone);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            JSONObject eve = result.getJSONObject(0);
            resp = eve.getString("Result");

            if(resp.equals("Registration successfull")){
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }else if(resp.equals("EmailID already exists!")){
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
            }else if(resp.equals("Registration failed")){
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }
}
