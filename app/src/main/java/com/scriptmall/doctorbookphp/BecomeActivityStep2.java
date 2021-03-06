package com.scriptmall.doctorbookphp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.core.deps.guava.base.Joiner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BecomeActivityStep2 extends AppCompatActivity {

    EditText etsince,etpin,etfees,etlanguage,etophone,etoaddr,etweb,etabt,etarea,etbuild,etstreet;
    TextView ettuefrom,ettueto,etwedfrom,etwedto,etthursfrom,etthursto,etfrifrom,etfrito,etsatfrom,etsatto,etsunfrom,etsunto;
    TextView etissuedate,etmonfrom,etmonto,clear;

    private int year,hh,mm;
    private int month;
    private int day;
    StringBuilder datestr;
    static final int DATE_PICKER_ID = 0;

    Button submit;

    String mon_from,mon_to,tue_from,tue_to,wed_from,wed_to,thurs_from,thurs_to,fri_from,fri_to,sat_from,sat_to,sun_from,sun_to;

    String expin,since,pin,fees,lamguage,ophone,oaddr,web,abt,issudate;
    String ubuild1,ustreet1,uarea1;

    String status,uid,lname,catid,resp;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_step2);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        Intent i=getIntent();
        expin=i.getStringExtra("expin");
        abt=i.getStringExtra("abt");

        etsince=(EditText)findViewById(R.id.etsince);
        etpin=(EditText)findViewById(R.id.etpin);
        etfees=(EditText)findViewById(R.id.etfees);
        etlanguage=(EditText)findViewById(R.id.etlanguage);
        etophone=(EditText)findViewById(R.id.etophone);
        etarea=(EditText)findViewById(R.id.etarea);
        etbuild=(EditText)findViewById(R.id.etbuild);
        etstreet=(EditText)findViewById(R.id.etstreet);
//        etabt=(EditText)findViewById(R.id.etabt);
        etweb=(EditText)findViewById(R.id.etweb);
        etissuedate=(TextView) findViewById(R.id.etissuedate);

        etmonfrom=(TextView) findViewById(R.id.etmonfrom);
        etmonto=(TextView)findViewById(R.id.etmonto);
        ettuefrom=(TextView)findViewById(R.id.ettuefrom);
        ettueto=(TextView)findViewById(R.id.ettueto);
        etwedfrom=(TextView)findViewById(R.id.etwedfrom);
        etwedto=(TextView)findViewById(R.id.etwedto);
        etthursfrom=(TextView)findViewById(R.id.etthursfrom);
        etthursto=(TextView)findViewById(R.id.etthursto);
        etfrifrom=(TextView)findViewById(R.id.etfrifrom);
        etfrito=(TextView)findViewById(R.id.etfrito);
        etsatfrom=(TextView)findViewById(R.id.etsatfrom);
        etsatto=(TextView)findViewById(R.id.etsatto);
        etsunfrom=(TextView)findViewById(R.id.etsunfrom);
        etsunto=(TextView)findViewById(R.id.etsunto);

        etmonfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                in_time=showTimePicker();
                final Calendar c = Calendar.getInstance();
                final int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        mon_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etmonfrom.setText(mon_from);
                    }
                }, hour, minute, false);
                timePickerDialog.show();

            }
        });

        etmonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour1 = c1.get(Calendar.HOUR_OF_DAY); int minute1 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        mon_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etmonto.setText(mon_to);
                    }
                }, hour1, minute1, false);
                timePickerDialog2.show();
            }
        });

        ettuefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour3 = c1.get(Calendar.HOUR_OF_DAY); int minute3 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog3 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        tue_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; ettuefrom.setText(tue_from);
                    }
                }, hour3, minute3, false);
                timePickerDialog3.show();
            }
        });
        ettueto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour4 = c1.get(Calendar.HOUR_OF_DAY); int minute4 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        tue_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; ettueto.setText(tue_to);
                    }
                }, hour4, minute4, false);
                timePickerDialog2.show();
            }
        });

        etwedfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour5 = c1.get(Calendar.HOUR_OF_DAY); int minute5 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog5 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        wed_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etwedfrom.setText(wed_from);
                    }
                }, hour5, minute5, false);
                timePickerDialog5.show();
            }
        });
        etwedto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour6 = c1.get(Calendar.HOUR_OF_DAY); int minut6 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog6 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        wed_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etwedto.setText(wed_to);
                    }
                }, hour6, minut6, false);
                timePickerDialog6.show();
            }
        });

        etthursfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour7 = c1.get(Calendar.HOUR_OF_DAY); int minute7 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog7 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        thurs_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etthursfrom.setText(thurs_from);
                    }
                }, hour7, minute7, false);
                timePickerDialog7.show();
            }
        });
        etthursto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour8 = c1.get(Calendar.HOUR_OF_DAY); int minut8 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog8 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        thurs_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etthursto.setText(thurs_to);
                    }
                }, hour8, minut8, false);
                timePickerDialog8.show();
            }
        });

        etfrifrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour9 = c1.get(Calendar.HOUR_OF_DAY); int minute9 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog9 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        fri_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etfrifrom.setText(fri_from);
                    }
                }, hour9, minute9, false);
                timePickerDialog9.show();
            }
        });
        etfrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour10 = c1.get(Calendar.HOUR_OF_DAY); int minut10 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog10 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        fri_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etfrito.setText(fri_to);
                    }
                }, hour10, minut10, false);
                timePickerDialog10.show();
            }
        });

        etsatfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour11 = c1.get(Calendar.HOUR_OF_DAY); int minute11 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog11 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        sat_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etsatfrom.setText(sat_from);
                    }
                }, hour11, minute11, false);
                timePickerDialog11.show();
            }
        });
        etsatto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour10 = c1.get(Calendar.HOUR_OF_DAY); int minut10 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog12 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        sat_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etsatto.setText(sat_to);
                    }
                }, hour10, minut10, false);
                timePickerDialog12.show();
            }
        });

        etsunfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour11 = c1.get(Calendar.HOUR_OF_DAY); int minute11 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog13 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        sun_from=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etsunfrom.setText(sun_from);
                    }
                }, hour11, minute11, false);
                timePickerDialog13.show();
            }
        });
        etsunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c1 = Calendar.getInstance();int hour10 = c1.get(Calendar.HOUR_OF_DAY); int minut10 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog14 = new TimePickerDialog(BecomeActivityStep2.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm="AM"; String mm_precede = "";String hh_precede = "";
                        if(hourOfDay>=12){
                            am_pm="PM";
                            if (hourOfDay >=13 && hourOfDay < 24) { hourOfDay -= 12; } else { hourOfDay = 12; }
                        } else if (hourOfDay == 0) {  hourOfDay = 12; }
                        if (minute < 10) {  mm_precede = "0"; }
                        if (hourOfDay < 10) {  hh_precede = "0"; }
                        sun_to=hh_precede+hourOfDay + ":" +mm_precede+ minute+" "+am_pm; etsunto.setText(sun_to);
                    }
                }, hour10, minut10, false);
                timePickerDialog14.show();
            }
        });

        etissuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });

        submit=(Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToDB();
            }
        });

        clear=(TextView) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etmonfrom.setText("");
                etmonto.setText("");
                ettuefrom.setText("");
                ettueto.setText("");
                etwedfrom.setText("");
                etwedto.setText("");
                etthursfrom.setText("");
                etthursto.setText("");
                etfrifrom.setText("");
                etfrito.setText("");
                etsatfrom.setText("");
                etsatto.setText("");
                etsunfrom.setText("");
                etsunto.setText("");

                mon_from="";mon_to="";tue_from="";tue_to="";wed_from="";wed_to="";
                thurs_from="";thurs_to="";fri_from="";fri_to="";sat_from="";sat_to="";sun_from="";sun_to="";

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage License");

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
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
            etissuedate.setText(datestr);
        }
    };

    private void submitToDB() {

        since=etsince.getText().toString().trim();
        pin=etpin.getText().toString().trim();
        fees=etfees.getText().toString().trim();
        lamguage=etlanguage.getText().toString().trim();
        ophone=etophone.getText().toString().trim();
        ubuild1=etbuild.getText().toString().trim();
        ustreet1=etstreet.getText().toString().trim();
        uarea1=etarea.getText().toString().trim();
        web=etweb.getText().toString().trim();
//        abt=etabt.getText().toString().trim();

        loading = ProgressDialog.show(BecomeActivityStep2.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MANAGE_LICENCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSONResult(response);
//                        Toast.makeText(ProfileDoctorActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BecomeActivityStep2.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.UID,uid);
                params.put(Config.SINCE,since);                     params.put(Config.PIN,pin);
                params.put(Config.FEES,fees);                       params.put(Config.LANG,lamguage);
                params.put(Config.OPHONE,ophone);                   params.put(Config.UBUILD1,ubuild1);
                params.put(Config.UAREA1,uarea1);                   params.put(Config.USTREET1,ustreet1);
                params.put(Config.EXPIN,expin);                     params.put(Config.ISSUEDATE,issudate);
                params.put(Config.ABOUT,abt);                       params.put(Config.WEBSITE,web);

                params.put(Config.MONFROM,mon_from);                params.put(Config.MONTO,mon_to);
                params.put(Config.TUEFROM,tue_from);                params.put(Config.TUETO,tue_to);
                params.put(Config.WEDFROM,wed_from);                params.put(Config.WEDTO,wed_to);
                params.put(Config.THURSFROM,thurs_from);            params.put(Config.THURSTO,thurs_to);
                params.put(Config.FRIFROM,fri_from);                params.put(Config.FRITO,fri_to);
                params.put(Config.SATFROM,sat_from);                params.put(Config.SATTO,sat_to);
                params.put(Config.SUNFROM,sun_from);                params.put(Config.SUNTO,sun_to);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void showJSONResult(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            resp = eve.getString("Result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, resp, Toast.LENGTH_SHORT).show();
    }
}
