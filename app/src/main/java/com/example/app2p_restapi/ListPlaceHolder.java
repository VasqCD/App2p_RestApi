package com.example.app2p_restapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app2p_restapi.Config.Posts;
import com.example.app2p_restapi.Config.PostsAdapter;
import com.example.app2p_restapi.Config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPlaceHolder extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private List<Posts> postsList;
    private ListView listView;
    private PostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_place_holder);

        listView = findViewById(R.id.listplace);
        postsList = new ArrayList<>();

        // Obtener datos de la API
        fetchData();
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                RestApiMethods.EndPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA", response);

                        try {
                            // parsear el JSON
                            JSONArray jsonArray = new JSONArray(response);

                            // lipiar lista
                            postsList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Posts post = new Posts(
                                        jsonObject.getInt("userId"),
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("body")
                                );
                                postsList.add(post);
                            }

                            adapter = new PostsAdapter(ListPlaceHolder.this, postsList);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ListPlaceHolder.this, "Error al procesar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.toString());
                        Toast.makeText(ListPlaceHolder.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }
}