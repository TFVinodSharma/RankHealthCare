package com.scriptmall.doctorbookphp;

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
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class PasswordUpdateActivity extends AppCompatActivity {

    EditText etpwd,etcpwd,etopwd;
    Button submit;
    String uid;
    TextView error;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);


        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        etpwd=(EditText)findViewById(R.id.etpwd);
        etopwd=(EditText)findViewById(R.id.etopwd);
        etcpwd=(EditText) findViewById(R.id.etcpwd);
        error=(TextView)findViewById(R.id.error);

        submit=(Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String pwd = etpwd.getText().toString().trim();

                final String cpwd = etcpwd.getText().toString().trim();

                if (pwd.equals(cpwd)) {

                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                    if (netInfo == null) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }else{
                        insertdb();
                    }

                } else {
                    Toast.makeText(PasswordUpdateActivity.this, "Password Does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Password");



    }


    void insertdb(){

        final String npwd = etpwd.getText().toString().trim();
        final String opwd = etopwd.getText().toString().trim();
        final String cpwd = etcpwd.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PASSWORDUPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJoson(response);
//                        Toast.makeText(PasswordUpdateActivity.this,response,Toast.LENGTH_LONG).show();
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
                params.put(Config.UID,uid);
                params.put(Config.OPWD,opwd);
                params.put(Config.CPWD,cpwd);
                params.put(Config.NPWD,npwd);


                return params;
            }
//            @Override
//            public Priority getPriority() {
//                return Priority.IMMEDIATE;
//            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJoson(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            JSONObject eve = result.getJSONObject(0);
            String resp=eve.getString("Result");

            if(resp.equals("password changed successfully")){
                Toast.makeText(this, "Your password Changed", Toast.LENGTH_SHORT).show();
                etpwd.setText("");
                etopwd.setText("");
                etcpwd.setText("");
//                Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
//                startActivity(i);
            }
            else{
                Toast.makeText(this, "Enter old password correctly", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
            case android.R.id.home:super.onBackPressed();
//                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                startActivityForResult(intent, 0);
                break;
        }
        return true;
    }


}
