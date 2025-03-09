package com.example.app2p_restapi;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app2p_restapi.Config.Posts;
import com.example.app2p_restapi.Config.RestApiMethods;

import java.util.List;

public class ListPlaceHolder extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    List<Posts> postsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_place_holder);

        SendData();
    }

    private void SendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.EndPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    String mensaje = response.toString();
                    Log.d("RESPUESTA", mensaje);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}