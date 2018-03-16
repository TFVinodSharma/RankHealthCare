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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Map;

public class EditPhotoActivity extends AppCompatActivity {

    ImageView img;
    Button change,upload;
    private int PICK_IMAGE_REQUEST = 1;
    private String imagepath=null;
    private Bitmap bitmap;
    String image,uid,resp,uimg,image_name;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        Intent i=getIntent();
        uimg=i.getStringExtra("uimg");


        img=(ImageView) findViewById(R.id.img);

        Picasso
                .with(this)
                .load(Config.IMG_URL+uimg)
                .into(img);

        change=(Button) findViewById(R.id.change);
        upload=(Button) findViewById(R.id.upload);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    submitToDB();
                }
//                image=getStringImage(bitmap);
//                Toast.makeText(EditPhotoActivity.this, image, Toast.LENGTH_SHORT).show();
            }
        });

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                alertForDelete();

                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change");
    }

    private void alertForDelete() {
        final AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Reject");
        al.setCancelable(true);
        al.setMessage("Are you sure want to remove this image.");

        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }else{
                    removePhoto();
                }

//                getData();
                img.setImageResource(R.drawable.noimage);
//                Toast.makeText(EditPhotoActivity.this, "Image removed Successfully", Toast.LENGTH_SHORT).show();
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

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri filePath1 = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    image=getStringImage(bitmap);
                    img.setImageBitmap(bitmap);
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

    public  void submitToDB(){
//        image=getStringImage(bitmap);
        loading = ProgressDialog.show(EditPhotoActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IMAGEUPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(EditProfileActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Toast.makeText(EditPhotoActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.UID,uid);
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
            resp = eve.getString("success");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, resp, Toast.LENGTH_SHORT).show();
    }

    private void removePhoto() {
        loading = ProgressDialog.show(EditPhotoActivity.this,"Please wait...","Fetching...",false,false);
        loading.setCanceledOnTouchOutside(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IMAGEREMOVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        showJSON(response);
//                        Toast.makeText(EditProfileActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditPhotoActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.UID,uid);
                params.put(Config.UIMAGENAME,uimg);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(intent, 0);
    }

}
