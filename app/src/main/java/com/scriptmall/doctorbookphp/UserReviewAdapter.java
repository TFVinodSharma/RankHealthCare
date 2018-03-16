package com.scriptmall.doctorbookphp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by scriptmall on 11/17/2017.
 */
public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    String pid;
    RecyclerView view;
    String rname,rrate,rdesc,rdate,rimg;
    String docid,docname,rid;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,desc,rate,date;
        ImageView details;
        RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lname);
            desc = (TextView) view.findViewById(R.id.msg);
            date = (TextView) view.findViewById(R.id.date);
            details=(ImageView)view.findViewById(R.id.details);

        }
    }

    public UserReviewAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public UserReviewAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userreview_list_row, parent, false);

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

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}
