package com.scriptmall.doctorbookphp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    TextView name,rate,fees,exp,spl,abt,num,date,lpracticein,llang, desc,cont, mail,phone,addr,web,time,mon,tue,wed,thurs,fri,sat,sun;
//    String lfees,lexp,labt,lnum,ldate, ldesc,lcont,lmail,lweb,ltime,lmon,ltue,lwed,lthurs,lfri,lsat,lsun;

    String lname,lphone,email,laddr,lepx,lrate,lspl,docid,rimg;
    String mon_from,mon_to,tue_from,tue_to,wed_from,wed_to,thurs_from,thurs_to,fri_from,fri_to,sat_from,sat_to,sun_from,sun_to;
    String barid,since,pin,lfees,lamguage,ophone,oaddr,lweb,labt,issudate,city;
    String ubuild1,ustreet1,uarea1,review_status,app_status,uid;

    LinearLayout linabt,lincont,lintime;
    String abtst,contst,timest;
    RatingBar ratebar;
    ProgressDialog loading;
    ImageView img;

    Button book;


    EditText etuname,etumail,etuphone,etumsg;
    TextView tvbookdate,tvbooktime,tvfees;
    Button booknow;
    Spinner spintype;
    private ArrayList<String> typeList;
    String type;

    private int year,hh,mm;
    private int month;
    private int day;
    StringBuilder datestr;
    String timestr,resp;
    static final int DATE_PICKER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i=getIntent();
        docid=i.getStringExtra("docid");
//        lname=i.getStringExtra("lname");
//        lphone=i.getStringExtra("lphone");
//        laddr=i.getStringExtra("laddr");
//        lepx=i.getStringExtra("lepx");
//        lrate=i.getStringExtra("lrate");
//        lspl=i.getStringExtra("lspl");
//        rimg=i.getStringExtra("rimg");
//        review_status=i.getStringExtra("review_status");
//        app_status=i.getStringExtra("app_status");

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        name=(TextView)findViewById(R.id.name);
        rate=(TextView)findViewById(R.id.rate);
        fees=(TextView)findViewById(R.id.lfees);
        exp=(TextView)findViewById(R.id.lexp);
        spl=(TextView)findViewById(R.id.lspl);

        abt=(TextView)findViewById(R.id.labt);
        num=(TextView)findViewById(R.id.lnum);
        date=(TextView)findViewById(R.id.ldate);
        desc=(TextView)findViewById(R.id.ldesc);
        lpracticein=(TextView)findViewById(R.id.lpracticein);
        llang=(TextView)findViewById(R.id.llang);


        cont=(TextView)findViewById(R.id.lcont);
        mail=(TextView)findViewById(R.id.lmail);
        phone=(TextView)findViewById(R.id.lphone);
        addr=(TextView)findViewById(R.id.laddr);
        web=(TextView)findViewById(R.id.lweb);

        time=(TextView)findViewById(R.id.time);
        mon=(TextView)findViewById(R.id.mon);
        tue=(TextView)findViewById(R.id.tue);
        wed=(TextView)findViewById(R.id.wed);
        thurs=(TextView)findViewById(R.id.thurs);
        fri=(TextView)findViewById(R.id.fri);
        sat=(TextView)findViewById(R.id.sat);
        sun=(TextView)findViewById(R.id.sun);

        ratebar=(RatingBar)findViewById(R.id.set_rating);
        linabt=(LinearLayout)findViewById(R.id.linabt);
        lincont=(LinearLayout)findViewById(R.id.lincont);
        lintime=(LinearLayout)findViewById(R.id.lintime);

        linabt.setVisibility(View.GONE);
        lincont.setVisibility(View.GONE);
        lintime.setVisibility(View.GONE);

        abtst="0";contst="0";timest="0";


        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(abtst.equals("0")){
                    linabt.setVisibility(View.VISIBLE);
                    lincont.setVisibility(View.GONE);
                    lintime.setVisibility(View.GONE);
                    abtst="1";contst="0";contst="0";
                }else if(abtst.equals("1")) {
                    linabt.setVisibility(View.GONE);
                    lincont.setVisibility(View.GONE);
                    lintime.setVisibility(View.GONE);
                    abtst="0";
                }
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contst.equals("0")){
                    linabt.setVisibility(View.GONE);
                    lincont.setVisibility(View.VISIBLE);
                    lintime.setVisibility(View.GONE);
                    contst="1";abtst="0";timest="0";
                }else if(abtst.equals("1")) {
                    linabt.setVisibility(View.GONE);
                    lincont.setVisibility(View.GONE);
                    lintime.setVisibility(View.GONE);
                    contst="0";
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timest.equals("0")){
                    linabt.setVisibility(View.GONE);
                    lincont.setVisibility(View.GONE);
                    lintime.setVisibility(View.VISIBLE);
//                    lintime.findFocus();
                    timest="1";abtst="0";contst="0";
                }else if(abtst.equals("1")) {
                    linabt.setVisibility(View.GONE);
                    lincont.setVisibility(View.GONE);
                    lintime.setVisibility(View.GONE);
                    timest="0";
                }
            }
        });

        img=(ImageView) findViewById(R.id.img);



        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            getData();
        }



        book=(Button)findViewById(R.id.book);
        if(uid.equals(docid)){
            book.setVisibility(View.GONE);
        }

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForBook();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

    }

    private void getData() {

        loading = ProgressDialog.show(DetailsActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(DetailsActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailsActivity.this,"Try Again",Toast.LENGTH_LONG).show();
//                        Toast.makeText(DetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.DOCID,docid);
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


            lname = eve.getString(Config.DOCNAME);
            email = eve.getString(Config.UMAIL);
            rimg = eve.getString(Config.UIMG);
            lrate = eve.getString(Config.RATING);
            lfees = eve.getString(Config.FEES);
            lepx=eve.getString(Config.EXP);
            lspl = eve.getString(Config.EXPIN);
            ubuild1 = eve.getString(Config.UBUILD1);
            ustreet1 = eve.getString(Config.USTREET1);
            uarea1 = eve.getString(Config.UAREA1);
            lphone = eve.getString(Config.UPHONENO);
            pin = eve.getString(Config.PIN);
            barid=eve.getString(Config.BARID);
            issudate = eve.getString(Config.ISSUEDATE);
            lamguage = eve.getString(Config.LANG);
            lweb = eve.getString(Config.WEBSITE);
            labt = eve.getString(Config.ABOUT);
            app_status=eve.getString(Config.APPSTATUS);
            review_status=eve.getString(Config.RSTATUS);

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
                    .load(Config.IMG_URL+rimg)
                    .into(img);

            name.setText(lname);
            rate.setText("("+lrate+")");
            ratebar.setRating(Float.valueOf(lrate));
            ratebar.setIsIndicator(true);
            exp.setText(lepx+" years Experience");
            spl.setText(lspl);

            fees.setText(lfees+" / Hr");

            num.setText(barid);
            date.setText(issudate);
            lpracticein.setText(pin);
            llang.setText(lamguage);
            desc.setText(labt);

            mail.setText(email);
            phone.setText(lphone);
            addr.setText(ubuild1+","+ustreet1+","+uarea1);
            web.setText(lweb);

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
            if(thurs.equals("")){
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


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialogForBook() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.book_now);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        tvbookdate=(TextView)dialog.findViewById(R.id.etdate);
        tvbooktime=(TextView)dialog.findViewById(R.id.ettime);
        tvfees=(TextView)dialog.findViewById(R.id.etfees);

        etuname=(EditText)dialog.findViewById(R.id.etuname);
        etumail=(EditText)dialog.findViewById(R.id.etmail);
        etuphone=(EditText)dialog.findViewById(R.id.etmob);
        etumsg=(EditText)dialog.findViewById(R.id.etmail);

        spintype=(Spinner)dialog.findViewById(R.id.spintype);
        typeList= new ArrayList<String>();
//        loadSpinnerData();
        tvfees.setText(lfees+"/Hr");

        spintype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                type=spintype.getSelectedItem().toString();
//                getStatename1(country1);
            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        String s[]=lspl.split(",+");
        for(int i=0;i<s.length;i++){
            typeList.add(s[i]);
        }
        spintype.setAdapter(new ArrayAdapter<String>(DetailsActivity.this, R.layout.simple_spinner_item, typeList));

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        datestr=new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append("") ;
        tvbookdate.setText("Select Date");
        tvbookdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        tvbooktime.setText("Select Time");
        tvbooktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String am_pm="AM";    String mm_precede = ""; String hh_precede = "";
                                if(hourOfDay>=12){   am_pm="PM";
                                    if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12;   }  else { hourOfDay = 12; }
                                } else if (hourOfDay == 0) { hourOfDay = 12;  }
                                if (minute < 10) { mm_precede = "0"; }
                                if (hourOfDay < 10) { hh_precede = "0"; }
                                timestr=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm;  tvbooktime.setText(timestr);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        booknow=(Button)dialog.findViewById(R.id.book);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    submitForook();
                }

//                dialog.cancel();
            }
        });



        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);


    }

    private void submitForook() {

      final String comments=etumsg.getText().toString().trim();

        loading = ProgressDialog.show(DetailsActivity.this,"Please wait...","Fetching...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.BOOK_URL,
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
                        Toast.makeText(DetailsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String,String>();
                params.put(Config.BOOKDATE,String.valueOf(datestr));
                params.put(Config.BOOKTIME,timestr);
                params.put(Config.TYPE,type);
                params.put(Config.CMT,comments);
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
        Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
//        Intent i=new Intent(getApplicationContext(),ReviewActivity.class);
//        i.putExtra("docid",docid);
//        i.putExtra("review_status",review_status);
//        i.putExtra("app_status",app_status);
//        startActivity(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
//                return new DatePickerDialog(this, pickerListener, year, month,day);
                DatePickerDialog _date =   new DatePickerDialog(this, pickerListener, year,month,day){
                    @Override
                    public void onDateChanged(DatePicker view, int myear, int mmonth, int mday)
                    {
                        if (myear < year)
                            view.updateDate(year, month, day);
                        if (mmonth < month && myear == year)
                            view.updateDate(year, month, day);
                        if (mday < day && myear == year && mmonth == month)
                            view.updateDate(year, month, day);
                    }
                };
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            datestr=new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append("") ;
            tvbookdate.setText(datestr);
        }
    };

    public void loadSpinnerData(){
        List<String> l5 = new ArrayList<String>();
        l5.add("---Select---"); l5.add("Allergist");
        l5.add("Anesthesiologist");
        l5.add("Cardiologist");
        l5.add("Dabetologists");
        l5.add("Dentist");
        l5.add("Diagnostician");
        l5.add("Neonatologist");
        l5.add("Neurologist");
        l5.add("Obstetrician");
        l5.add("Oncologist");
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item, l5);
        dataAdapter5.setDropDownViewResource(R.layout.simple_spinner_item);
        spintype.setAdapter(dataAdapter5);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.review:
                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                i.putExtra("docid",docid);
                i.putExtra("review_status",review_status);
                i.putExtra("app_status",app_status);
                startActivityForResult(i, 0);
                break;
//            case R.id.contact:
//                Intent in = new Intent(getApplicationContext(), ContactUsActivity.class);
//                in.putExtra("pid",pid);
//                startActivityForResult(in, 0);
//                break;
            case android.R.id.home:
                super.onBackPressed();

                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



}
