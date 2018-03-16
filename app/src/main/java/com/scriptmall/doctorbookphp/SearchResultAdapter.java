package com.scriptmall.doctorbookphp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scriptmall on 11/15/2017.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {


    private List<Book> catList;
    private Context ctx;
    String lname,lphone,laddr,lepx,lrate,lspl,docid,rimg,review_status,app_status;
    RecyclerView view;
    ArrayList<Integer> alImage;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, exp, addr, spl, rate;
        public ImageView img;
        Button enqury, call;
        RatingBar rateing;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);

            exp = (TextView) view.findViewById(R.id.exp);
            addr = (TextView) view.findViewById(R.id.addr);
            spl = (TextView) view.findViewById(R.id.spl);
            rate = (TextView) view.findViewById(R.id.ratetv);
            rateing = (RatingBar) view.findViewById(R.id.set_rating);

            enqury = (Button) view.findViewById(R.id.enquery);
            call = (Button) view.findViewById(R.id.call);
            img = (ImageView) view.findViewById(R.id.img);


        }
    }

    public SearchResultAdapter(Context context,List<Book> catList, ArrayList<Integer> alImage) {
        this.catList = catList;
        this.alImage = alImage;
        ctx = context;

    }

    public SearchResultAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.catList = catList;
        ctx = context;
        this.view = view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = catList.get(position);

        docid=ride.getDocid();
        lname=ride.getLname();
        lphone=ride.getLphone();
        laddr=ride.getLaddr();
        lepx=ride.getLepx();
        lrate=ride.getLrate();
        lspl=ride.getLspl();
        rimg=ride.getRimg();
//        review_status=ride.getReview_status();
//        app_status=ride.getApp_status();

        if(lspl.length()<25){
            holder.spl.setText(lspl);
        }else {
            lspl=lspl.replace(lspl.substring(25,lspl.length()),"...");
            holder.spl.setText(lspl);
        }




        holder.name.setText(lname);
        holder.exp.setText(lepx+" years Experience");
        holder.addr.setText(laddr);

//        holder.img.setImageResource(alImage.get(position));
        holder.rate.setText("( " + lrate + " )");
        holder.rateing.setRating(Float.valueOf(lrate));
        holder.rateing.setIsIndicator(true);


        Picasso
                .with(ctx)
                .load(Config.IMG_URL+rimg)
                .into(holder.img);




        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book ride = catList.get(position);
                lphone=ride.getLphone();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
                } else {
//                    //Open call function
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lphone));
                    ctx.startActivity(intent);
                }

            }
        });

        holder.enqury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book ride = catList.get(position);
                docid=ride.getDocid();
                lname=ride.getLname();
                lphone=ride.getLphone();
                laddr=ride.getLaddr();
                lepx=ride.getLepx();
                lrate=ride.getLrate();
                lspl=ride.getLspl();
                rimg=ride.getRimg();
                review_status=ride.getReview_status();
                app_status=ride.getApp_status();
                Intent i=new Intent(ctx,DetailsActivity.class);
                i.putExtra("docid",docid);
                ctx.startActivity(i);
//                Toast.makeText(ctx, docid, Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void requestPermissions(String[] strings, int permissionsRequestPhoneCall) {
    }







    @Override
    public int getItemCount() {
        return catList.size();
    }
}