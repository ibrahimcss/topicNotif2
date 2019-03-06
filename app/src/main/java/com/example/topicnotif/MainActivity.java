package com.example.topicnotif;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    JSONObject json;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel=
                    new NotificationChannel("Notification,","Notification",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);



        }


        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        String msg="succesfull";
                        if(!task.isSuccessful()){
                            msg="fail";

                        }
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);


    }




    public  void send(View view){
         json = new JSONObject();
        try {
            JSONObject userData=new JSONObject();
            userData.put("title","your title");
            userData.put("body","your body");

            json.put("data",userData);
            json.put("to", "/topics/all");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.i("onResponse", "" + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("onErrorResponse", "" + error.toString());

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "key=AAAAbF86RT8:APA91bGunfG6_bk0g9A6UCXObq0XMeqTHREqe2tWBeEnxKnEaFGDSzdAowVAvgAM0Je8fE1t4BES-c6S8IP1AAorpVp10Sa3SdTb7JZuDAsrRfBT8TMHy9Sbbkytrtz5hpUphyzx3FSG");
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);


        }
        catch (JSONException e) {
            Log.i("JSONException",e.getMessage());

// MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
    }



}