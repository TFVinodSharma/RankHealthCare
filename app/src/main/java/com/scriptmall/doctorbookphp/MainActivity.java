package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner spincity,spincat;
    Button search,all,top;
    private ArrayList<String> catlist,cityList;
    String cat,city;
//    FloatingActionButton fab;

    String uid,utype;
    Session session;
    ProgressDialog loading;

    String logo;
    ImageView logo_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        session = new Session(this);
        if(!session.loggedin())
        {
            logout();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");
        utype = sharedPreferences.getString(Config.UTYPE_SHARED_PREF,"Not Available");

        logo = sharedPreferences.getString(Config.LOGO_SHARED_PREF,"Not Available");
        logo_img=(ImageView)findViewById(R.id.img);
        Picasso .with(this).load(logo).into(logo_img);

//        Toast.makeText(this, uid+" "+utype, Toast.LENGTH_SHORT).show();

        spincat=(Spinner)findViewById(R.id.spincat);
        spincity=(Spinner)findViewById(R.id.spincity);
        search=(Button)findViewById(R.id.button);
        all=(Button)findViewById(R.id.all);
        top=(Button)findViewById(R.id.top);


        if(utype.equals("0")){
            navigationView.getMenu().setGroupVisible(R.id.user,true);
            navigationView.getMenu().setGroupVisible(R.id.lawyer,false);
        }else if(utype.equals("1")){
            navigationView.getMenu().setGroupVisible(R.id.user,false);
            navigationView.getMenu().setGroupVisible(R.id.lawyer,true);
        }

//        loadSpinnerData();
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else {
            getCatName();
            getCityName();
        }
        catlist= new ArrayList<String>();
        cityList= new ArrayList<String>();

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cat=spincat.getSelectedItem().toString();
//                String a=state;
//                a=a.replaceAll(" ","%20");
//                getCityname1(a);
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spincity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                city=spincity.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SearchResultActivity.class);
                i.putExtra("city",city);
                i.putExtra("cat",cat);
                i.putExtra("key","0");
                startActivity(i);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SearchResultActivity.class);
                i.putExtra("city","0");
                i.putExtra("cat","0");
                i.putExtra("key","0");
                startActivity(i);
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SearchResultActivity.class);
                i.putExtra("city","0");
                i.putExtra("cat","0");
                i.putExtra("key","1");
                startActivity(i);
            }
        });


    }

    private void getCatName() {
        loading = ProgressDialog.show(MainActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);
        final String url = Config.SPLLIST_URL;
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
                        loading.dismiss();
                        Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_LONG).show();
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
                    String a=collegeData.getString("catid");
                    catlist.add(collegeData.getString(Config.CATNAME));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            spincat.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.simple_spinner_item, catlist));
        }
    }

    private void getCityName() {
//        loading = ProgressDialog.show(MainActivity.this,"Please wait...","Fetching...",false,false);
        final String url = Config.SEARCH_CITY_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                loading.dismiss();
                showJSONcityspinner(response);
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

    private void showJSONcityspinner(String response) {
        if (!response.toLowerCase().trim().equals("failure")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                for(int i=0;i<result.length();i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    String a=collegeData.getString("city_id");
                    cityList.add(collegeData.getString(Config.CITY));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            spincity.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.simple_spinner_item, cityList));
        }
    }

    public void loadSpinnerData(){
        List<String> l5 = new ArrayList<String>();
        l5.add("Select Your Option");
        l5.add("Allergist"); l5.add("Allergist");l5.add("Cardiologist");l5.add("Cardiologist");l5.add("Neurologist");l5.add("Ophthalmologist");
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l5);
        dataAdapter5.setDropDownViewResource(R.layout.simple_spinner_item);
        spincat.setAdapter(dataAdapter5);

        List<String> l7 = new ArrayList<String>();
        l7.add("Select City");
        l7.add("Altembroek"); l7.add("Anvers");l7.add("Cable Beach");l7.add("Chennai");l7.add("Formosa");l7.add("Namakkal");
        ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l7);
        dataAdapter7.setDropDownViewResource(R.layout.simple_spinner_item);
        spincity.setAdapter(dataAdapter7);
    }

    private void logout()
    {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            session.setLoggedin(false);
            finish();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(utype.equals("0")){
            getMenuInflater().inflate(R.menu.main, menu);
        }

//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.become) {
            Intent i=new Intent(getApplicationContext(),BecomeActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.profile) {
            Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.review) {
            Intent i=new Intent(getApplicationContext(),MyReviewsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.appoint) {
            Intent i=new Intent(getApplicationContext(),MyBookingsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.logout) {
            logout();
        }

        else if (id == R.id.lhome) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        }else if (id == R.id.lprofile) {
            Intent i=new Intent(getApplicationContext(),ProfileDoctorActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.lmaage) {
            Intent i=new Intent(getApplicationContext(),ManageLicenseActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.lreview) {
            Intent i=new Intent(getApplicationContext(),MyReviewsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.lureview) {
            Intent i=new Intent(getApplicationContext(),UserReviewsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.lappoint) {
            Intent i=new Intent(getApplicationContext(),MyBookingsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.luappoint) {
            Intent i=new Intent(getApplicationContext(),MyAppointmentsActivity.class);
            i.putExtra("status",utype);
            startActivity(i);
        } else if (id == R.id.llogout) {
            logout();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
