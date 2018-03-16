package com.scriptmall.doctorbookphp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SCRIPTSMALL on 6/30/2017.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    String pid;
    RecyclerView view;
    ArrayList<Integer> alImage,alImage1,alImage2,alImage3,alImage4;
    Float off;
    Double b;
    String rname,rrate,rdesc,rdate,rimg;
    String docid,docname,rid,rtitle;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,desc,rate,date,title;
        RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            desc = (TextView) view.findViewById(R.id.desc);
            rate = (TextView) view.findViewById(R.id.ratetv);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            rating=(RatingBar) view.findViewById(R.id.set_rating);



        }
    }

    public ReviewAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public ReviewAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = iconList.get(position);
        docname=ride.getDocname();
//        docid=ride.getDocid();
        rid=ride.getRid();
        rdesc=ride.getRdesc();
        rrate=ride.getRrate();
        rtitle=ride.getRtitle();
        rdate=ride.getRdate();


        holder.name.setText(docname);
        holder.desc.setText(rdesc);
        holder.rating.setRating(Float.valueOf(rrate));
        holder.rate.setText(" ( "+rrate+" ) ");
        holder.rating.setIsIndicator(true);
        holder.date.setText(" - "+rdate);
        holder.title.setText(rtitle);





    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}
