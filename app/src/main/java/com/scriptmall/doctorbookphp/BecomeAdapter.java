package com.scriptmall.doctorbookphp;

import android.content.Context;
import android.support.test.espresso.core.deps.guava.base.Joiner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.lang3.StringUtils;

/**
 * Created by scriptmall on 11/17/2017.
 */
public class BecomeAdapter extends RecyclerView.Adapter<BecomeAdapter.MyViewHolder>{



    private List<Book> iconList;
    private Context ctx;
    RecyclerView view;
    String lname,catid,expin;
    static String a;
    static ArrayList<String> mylist = new ArrayList<String>();
    private int selected_position = -1;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        CheckBox ch;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lname);
            ch = (CheckBox) view.findViewById(R.id.check);

        }
    }

    public BecomeAdapter(Context ctx, List<Book> catList) {
        this.iconList = catList;
        this.ctx=ctx;



    }

    public BecomeAdapter(Context context, RecyclerView view, List<Book> catList) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
    }

    public BecomeAdapter(Context context, RecyclerView view, List<Book> catList,String expin) {
        this.iconList = catList;
        ctx = context;
        this.view=view;
        this.expin=expin;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spllist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book ride = iconList.get(position);
        lname=ride.getLname();
        catid=ride.getCatid();

        holder.name.setText(lname);
        holder.ch.setChecked(iconList.get(position).isSelected());
        holder.ch.setTag(iconList.get(position));
        mylist.clear();

        holder.ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Book contact = (Book) cb.getTag();
                contact.setSelected(cb.isChecked());
                iconList.get(position).setSelected(cb.isChecked());
            }
        });


    }

    public static String getSPl(){


        a= Joiner.on(",").join(mylist);
        return a;
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public List<Book> getSpllist() {
        return iconList;
    }
}
