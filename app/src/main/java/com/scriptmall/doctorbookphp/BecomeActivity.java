package com.scriptmall.doctorbookphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.core.deps.guava.base.Joiner;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BecomeActivity extends AppCompatActivity {

    EditText ettitle,etbarid,etabout;
    String title,barid,abt;
    Button choose,submit;
    TextView tv_imgname;
    private int PICK_IMAGE_REQUEST = 1;
    private String imagepath=null;
    private Bitmap bitmap;
    String image_name,ip;
    String image;

    BecomeAdapter mAdapter;
    RecyclerView recyclerView;
    private List<Book> splList = new ArrayList<>();
    String lname,catid,expin,resp,uid;
    ProgressDialog loading;
    static List<Book> mylist = new ArrayList<>();

    String logo;
    ImageView logo_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        logo = sharedPreferences.getString(Config.LOGO_SHARED_PREF,"Not Available");
        logo_img=(ImageView)findViewById(R.id.img);
        Picasso.with(this).load(logo).into(logo_img);

        ettitle=(EditText)findViewById(R.id.ettitle);
        etbarid=(EditText)findViewById(R.id.etbarid);
        etabout=(EditText)findViewById(R.id.etabout);

        choose=(Button)findViewById(R.id.choose);
        tv_imgname=(TextView)findViewById(R.id.imgname);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BecomeAdapter(this,splList);
        recyclerView.setAdapter(mAdapter);

//        prepairData();
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }else{
            getSplList();

        }

        submit=(Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    submitToDB();
                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Become A Doctor");

    }



    public void prepairData(){
        Book near=new Book("Allergist");
        splList.add(near);
        near=new Book("Anesthesiologist");
        splList.add(near);
        near=new Book("Cardiologist");
        splList.add(near);
        near=new Book("Dabetologists");
        splList.add(near);
        near=new Book("Dentist");
        splList.add(near);
        near=new Book("Diagnostician");
        splList.add(near);
        near=new Book("Neonatologist");
        splList.add(near);
        near=new Book("Neurologist");
        splList.add(near);
        near=new Book("Obstetrician");
        splList.add(near);
        near=new Book("Oncologist");
        splList.add(near);
        near=new Book("Pediatrician");
        splList.add(near);
        near=new Book("Physiotherapist");
        splList.add(near);
        near=new Book("Plastic surgeon");
        splList.add(near);
        near=new Book("Psychiatrist");
        splList.add(near);
        near=new Book("Surgeons");
        splList.add(near);
        near=new Book("Veterinarian");
        splList.add(near);



    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {

            Uri filePath = data.getData();
            imagepath = getPath(filePath);
            String[] s = imagepath.split("/+");
            image_name = s[s.length - 1];
            tv_imgname.setText(image_name);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri filePath1 = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    image=getStringImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void getSplList() {
//        loading = ProgressDialog.show(ManageLicenseActivity.this,"Please wait...","Fetching...",false,false);
        final String url = Config.SPLLIST_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                loading.dismiss();
                showJSONcatlist(response);
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

    private void showJSONcatlist(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            splList.clear();
            for(int i=0;i<result.length();i++)
            {

                JSONObject eve = result.getJSONObject(i);
                lname = eve.getString(Config.CATNAME);
                catid=eve.getString(Config.CATID);



//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Book ed=new Book();
                ed.setLname(lname);
                ed.setCatid(catid);


                splList.add(ed);
//                    Toast.makeText(getApplicationContext(), (CharSequence) eventsList,Toast.LENGTH_LONG).show();

            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setLayoutManager(new GridLayoutManager(this, MainActivity.getGridSpanCount(this)));
            mAdapter = new BecomeAdapter( this, recyclerView, splList);
            recyclerView.setAdapter(mAdapter);
//
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitToDB() {

        title=ettitle.getText().toString().trim();
        abt=etabout.getText().toString().trim();
        barid=etbarid.getText().toString().trim();

        List<Book> spList = ((BecomeAdapter) mAdapter).getSpllist();
        List<String> newList1=new ArrayList<>();
        for (int i = 0; i < spList.size(); i++) {
            Book spl = spList.get(i);
            if (spl.isSelected() == true) {
                newList1.add(spl.getCatid().toString());
            }
        }
        expin= Joiner.on(",").join(newList1);

        loading = ProgressDialog.show(BecomeActivity.this,"Please wait...","Fetching...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BECOME_DOCTOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(BecomeActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Sorry, Try again",Toast.LENGTH_LONG).show();
//                        Toast.makeText(MyReviewsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.UID,uid);
                params.put(Config.BARID,barid);
                params.put(Config.RTITLE,title);
                params.put(Config.ABOUT,abt);
                params.put(Config.EXPIN,expin);
                params.put(Config.UIMGSTR,image);
                params.put(Config.UIMAGENAME,image_name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
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
        SharedPreferences sharedPreferences = BecomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.UTYPE_SHARED_PREF, "1");
        editor.commit();
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Congrats");
        al.setMessage("Your profile upgraded to Doctor. Your profile will be visible to other users if Admin accept your request. " +
                "Please enter your official details in \"Manage License\" page. Then only admin will process yor request");
        al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
       al.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){

            case android.R.id.home:
//                super.onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("status","0");
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("status","0");
        startActivityForResult(intent, 0);
    }


}
