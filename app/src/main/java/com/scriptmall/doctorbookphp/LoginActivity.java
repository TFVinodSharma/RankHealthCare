package com.scriptmall.doctorbookphp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText mail;
    private EditText pwd;
    TextView register,fpwd;
    private Button login;
    private String mailid;
    private String password;
//    ProgressDialog loading;
    Session session;
    String resp,uid,utype;

    EditText et;
    Button b;
    TextView log_in,regis, tv1;
    String logo;
    ImageView logo_img;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WAKE_LOCK,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
//            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (checkPermissions()){
            //  permissions  granted.
        }

        session = new Session(this);
        if(session.loggedin()){

            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder al=new AlertDialog.Builder(LoginActivity.this);
            al.setTitle("Sorry");
            al.setMessage("Please enable your mobile data or wi-fi to use the doctor search app efficiently");
            al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                }
            });
            al.show();
        }else {
           // getLogo();
        }


         logo_img=(ImageView)findViewById(R.id.img);



        mail=(EditText)findViewById(R.id.etemail);
        pwd=(EditText)findViewById(R.id.etpwd);
        register=(TextView)findViewById(R.id.register);
        fpwd=(TextView)findViewById(R.id.fpwd);
        tv1=(TextView)findViewById(R.id.tv1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        fpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogForFpwd();
            }
        });

        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailid = mail.getText().toString().trim();
                password = pwd.getText().toString().trim();
                if(!mailid.equals("")&&!password.equals("")){

                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                    if (netInfo == null) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }else {
                        userLogin();
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Enter Valid Email and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

  /*  private void getLogo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showLogoJSON(response);
//                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_LONG ).show();
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

    private void showLogoJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            logo = eve.getString("logopath");

            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.LOGO_SHARED_PREF, logo);
            editor.commit();

            Picasso
                    .with(this)
                    .load(logo)
                    .into(logo_img);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
//            result = ContextCompat.checkSelfPermission(getActivity(),p);
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }

    private void userLogin() {
        mailid = mail.getText().toString().trim();
        password = pwd.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        openProfile(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_EMAIL,mailid);
                map.put(Config.KEY_PWD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void openProfile(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            JSONObject eve = result.getJSONObject(0);
            uid=eve.getString(Config.UID);
            utype=eve.getString(Config.UTYPE);
            resp = eve.getString("success");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (resp.equals("Login success")) {

//        //Creating a shared preference
            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
            editor.putString(Config.UID_SHARED_PREF, uid);
            editor.putString(Config.UTYPE_SHARED_PREF, utype);
//        Toast.makeText(this,uid,Toast.LENGTH_LONG).show();

            //Saving values to editor
            editor.commit();

            session.setLoggedin(true);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(LoginActivity.this, "Enter Valid Email and Password", Toast.LENGTH_SHORT).show();
        }
    }


    public void showDialogForFpwd(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgot_password);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

         et=(EditText)dialog.findViewById(R.id.editText);

        log_in=(TextView)dialog.findViewById(R.id.log_in);
        regis=(TextView)dialog.findViewById(R.id.regis);


         b=(Button)dialog.findViewById(R.id.button);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        logo = sharedPreferences.getString(Config.LOGO_SHARED_PREF,"Not Available");
        logo_img=(ImageView)dialog.findViewById(R.id.img);
        Picasso.with(this).load(logo).into(logo_img);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.matches(emailPattern))
                {
//                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                    if (netInfo == null) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }else {
                        insertDb(email);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Enter correct email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });



        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);

    }

    public void insertDb(final String email){
//        loading = ProgressDialog.show(ForgotpwdActivity.this,"Please wait...","Sending...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOTPWD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        loading.dismiss();
                        showJson(response);
//                            Toast.makeText(ForgotpwdActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        loading.dismiss();
                        if(error.toString().equals("com.android.volley.TimeoutError")){
                            Toast.makeText(LoginActivity.this, "Request sent successfully\nCheck your Mail", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_LONG ).show();
                        }

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_EMAIL,email);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJson(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            JSONObject eve = result.getJSONObject(0);
            String resp=eve.getString("Result");

//            if(resp.equals("success")){
//                Toast.makeText(this, "Request sent successfully\nCheck your mail", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, resp, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
//            }
//            else{
//                Toast.makeText(this, resp, Toast.LENGTH_SHORT).show();
//            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {

        moveTaskToBack(true);
        // super.onBackPressed();
    }


}
