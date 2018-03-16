package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scriptmall on 11/16/2017.
 */
public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    String pid;
    RecyclerView view;
    String bdate,btime,bstatus,lname,bid,uid,resp;
    ProgressDialog loading;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,time,status,date;
        ImageView img;
        Button cancel;
        LinearLayout lin1;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lname);
            time = (TextView) view.findViewById(R.id.time);
            status = (TextView) view.findViewById(R.id.status);
            date = (TextView) view.findViewById(R.id.date);
            lin1=(LinearLayout) view.findViewById(R.id.lin1);
            img=(ImageView)view.findViewById(R.id.img);
            cancel=(Button)view.findViewById(R.id.cancel);

        }
    }

    public MyBookingsAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public MyBookingsAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_bookings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = iconList.get(position);

//        catid=ride.getCatid();
        bid=ride.getBid();
        lname=ride.getLname();
        bdate=ride.getBdate();
        btime=ride.getBtime();
        bstatus=ride.getBstatus();


        holder.name.setText(lname);

//        holder.rating.setRating(Float.valueOf(rrate));
//        holder.rate.setText(" ( "+rrate+" ) ");
//        holder.rating.setIsIndicator(true);
        holder.date.setText("Date: "+bdate);
        holder.time.setText("Time: "+btime);

        if (bstatus.equals("0")){
            holder.status.setText("Pending");
            holder.status.setTextColor(ctx.getResources().getColor(R.color.blue));
            holder.img.setImageResource(R.drawable.waiting);
            holder.img.setColorFilter(ctx.getResources().getColor(R.color.blue));
        }else if(bstatus.equals("1")){
            holder.status.setText("Accepted");
            holder.status.setTextColor(ctx.getResources().getColor(R.color.green));
            holder.img.setImageResource(R.drawable.ic_done);
            holder.img.setColorFilter(ctx.getResources().getColor(R.color.green));
        }else if(bstatus.equals("3")){
            holder.status.setText("Canceled");
            holder.status.setTextColor(ctx.getResources().getColor(R.color.red));
            holder.img.setImageResource(R.drawable.cancel);
            holder.img.setColorFilter(ctx.getResources().getColor(R.color.red));
            holder.cancel.setVisibility(View.GONE);
        }
        else if(bstatus.equals("2")){
            holder.lin1.setVisibility(View.GONE);
            holder.cancel.setText("Canceled");
            holder.cancel.setBackgroundResource(R.drawable.graybox);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder al = new AlertDialog.Builder(ctx);
                al.setTitle("Reject");
                al.setCancelable(true);
                al.setMessage("Are you sure want to delete this review.");

                al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                        if (netInfo == null) {
                            Toast.makeText(ctx, "Network Error", Toast.LENGTH_SHORT).show();
                        }else{
                            deletDB(position);
                        }
                    }
                });

                al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(),"exit cancel",Toast.LENGTH_LONG).show();
//                moveTaskToBack(true);
                    }
                });
                al.show();
//                holder.lin1.setVisibility(View.GONE);
//                holder.cancel.setText("Canceled");
//                holder.cancel.setBackgroundResource(R.drawable.graybox);
            }
        });

    }

    private void deletDB(int position) {
        Book ride = iconList.get(position);
        bid=ride.getBid();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        loading = ProgressDialog.show(ctx,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.APPOINTMENT_CANCEL_URL,
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
                        Toast.makeText(ctx,"Can't able to cancel this appointment",Toast.LENGTH_LONG).show();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.BID,bid);
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
        Toast.makeText(ctx, resp, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(ctx,MyBookingsActivity.class);
        ctx.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}
