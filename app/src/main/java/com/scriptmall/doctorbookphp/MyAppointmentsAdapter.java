package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Map;

/**
 * Created by scriptmall on 11/17/2017.
 */
public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    String pid;
    RecyclerView view;
    String bdate,btime,bstatus,lname,lphone,bid,uid,resp;
    ProgressDialog loading;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,time,phone,date,tvaccept;
        RelativeLayout accept,reject,canceled;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lname);
            time = (TextView) view.findViewById(R.id.time);
            phone = (TextView) view.findViewById(R.id.uphone);
            date = (TextView) view.findViewById(R.id.date);
            tvaccept = (TextView) view.findViewById(R.id.tvaccept);
            accept=(RelativeLayout) view.findViewById(R.id.accept);
            reject=(RelativeLayout)view.findViewById(R.id.reject);
            canceled=(RelativeLayout)view.findViewById(R.id.cancel);

        }
    }

    public MyAppointmentsAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public MyAppointmentsAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_appointments, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = iconList.get(position);

//        catid=ride.getCatid();
//        catname=ride.getCatname();
        lname=ride.getLname();
        bdate=ride.getBdate();
        btime=ride.getBtime();
        bstatus=ride.getBstatus();
        lphone=ride.getLphone();


        holder.name.setText(lname);
        holder.phone.setText(lphone);
        holder.date.setText("Date: "+bdate);
        holder.time.setText("Time: "+btime);

        if(bstatus.equals("0")){
            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.canceled.setVisibility(View.GONE);
        }else if(bstatus.equals("1")){
            holder.accept.setVisibility(View.VISIBLE);
            holder.tvaccept.setText("Accepted");
            holder.reject.setVisibility(View.GONE);
            holder.canceled.setVisibility(View.GONE);
        }else if(bstatus.equals("2")){
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.canceled.setVisibility(View.VISIBLE);
        }
        else if(bstatus.equals("3")){
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.canceled.setVisibility(View.VISIBLE);
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book ride = iconList.get(position);
                bstatus=ride.getBstatus();
                if(bstatus.equals("0")){
                    alertForAccept(position);
                }

            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book ride = iconList.get(position);
                bstatus=ride.getBstatus();
                if(bstatus.equals("0")){
                    alertForReject(position);
                }

            }
        });




    }

    private void alertForReject(final int position) {
        final AlertDialog.Builder al = new AlertDialog.Builder(ctx);
        al.setTitle("Reject");
        al.setCancelable(true);
        al.setMessage("Are you sure want to reject this request.");

        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeDB(position,"2");
            }
        });

        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        al.show();
    }

    private void alertForAccept(final int position) {
        final AlertDialog.Builder al = new AlertDialog.Builder(ctx);
        al.setTitle("Accept");
        al.setCancelable(true);
        al.setMessage("Are you sure want to accept this request.");

        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(ctx, "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    changeDB(position,"1");
                }
            }
        });

        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        al.show();
    }

    private void changeDB(int position, final String s) {
        Book ride = iconList.get(position);
        bid=ride.getBid();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        loading = ProgressDialog.show(ctx,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USER_APPOINTMENT_CANCEL_URL,
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
                        loading.dismiss();
                        Toast.makeText(ctx,"Can't able to process your request",Toast.LENGTH_LONG).show();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.BID,bid);
                params.put(Config.BSTATUS,s);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject eve = result.getJSONObject(0);
            resp = eve.getString("success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
//        Toast.makeText(ctx, resp, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(ctx,MyAppointmentsActivity.class);
        ctx.startActivity(i);
    }


    @Override
    public int getItemCount() {
        return iconList.size();
    }
}
