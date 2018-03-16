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
import android.widget.ImageView;
import android.widget.RatingBar;
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
public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    String pid;
    RecyclerView view;
    String rname,rrate,rdesc,rdate,rimg,uid,resp;
    String docid,docname,rid;
    ProgressDialog loading;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,desc,rate,date;
        ImageView details,delete;
        RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lname);
            desc = (TextView) view.findViewById(R.id.msg);
//            rate = (TextView) view.findViewById(R.id.ratetv);
            date = (TextView) view.findViewById(R.id.date);
//            rating=(RatingBar) view.findViewById(R.id.set_rating);
            details=(ImageView)view.findViewById(R.id.details);
            delete=(ImageView)view.findViewById(R.id.delete);

        }
    }

    public MyReviewAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public MyReviewAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_reviews, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = iconList.get(position);

        docname=ride.getDocname();
        docid=ride.getDocid();
        rid=ride.getRid();
        rdesc=ride.getRdesc();
//        rrate=ride.getRrate();
//        rname=ride.getRname();
        rdate=ride.getRdate();

        if(rdesc.length()>55){
            rdesc=rdesc.replace(rdesc.substring(55,rdesc.length()),"...");
            holder.desc.setText(rdesc);
        }else{
            holder.desc.setText(rdesc);
        }




        holder.name.setText(docname);

//        holder.rating.setRating(Float.valueOf(rrate));
//        holder.rate.setText(" ( "+rrate+" ) ");
//        holder.rating.setIsIndicator(true);
        holder.date.setText(" - "+rdate);


        holder.delete.setOnClickListener(new View.OnClickListener() {
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

            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book ride = iconList.get(position);
                docid=ride.getDocid();
                Intent i=new Intent(ctx,DetailsActivity.class);
                i.putExtra("docid",docid);
                ctx.startActivity(i);
            }
        });



    }

    private void deletDB(int position) {
        Book ride = iconList.get(position);
        rid=ride.getRid();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        loading = ProgressDialog.show(ctx,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REVIEW_DELETE_URL,
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
                        Toast.makeText(ctx,"Can't able to delete",Toast.LENGTH_LONG).show();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.RID,rid);
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
        Intent i=new Intent(ctx,MyReviewsActivity.class);
        ctx.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}
