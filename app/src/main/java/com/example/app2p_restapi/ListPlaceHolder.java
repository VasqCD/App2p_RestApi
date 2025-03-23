package com.example.app2p_restapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    private List<Posts> filteredPostsList;
    private ListView listView;
    private PostsAdapter adapter;
    private EditText searchEditText;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_place_holder);

        setTitle("Lista de Posts");

        // Inicializar vistas
        listView = findViewById(R.id.listplace);
        searchEditText = findViewById(R.id.searchEditText);
        btnSalvar = findViewById(R.id.btnSalvar);

        postsList = new ArrayList<>();
        filteredPostsList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Posts selectedPost = (Posts) parent.getItemAtPosition(position);

                Intent intent = new Intent(ListPlaceHolder.this, PostDetailActivity.class);
                intent.putExtra("post_id", selectedPost.getId());
                startActivity(intent);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPosts();
            }
        });

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

                            // limpiar lista
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

                            filteredPostsList.addAll(postsList);
                            adapter = new PostsAdapter(ListPlaceHolder.this, filteredPostsList);
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

        requestQueue.add(stringRequest);
    }

    private void filterPosts() {
        String searchText = searchEditText.getText().toString().toLowerCase().trim();

        filteredPostsList.clear();

        if (searchText.isEmpty()) {
            filteredPostsList.addAll(postsList);
        } else {
            boolean isNumber = false;
            int searchId = -1;

            try {
                searchId = Integer.parseInt(searchText);
                isNumber = true;
            } catch (NumberFormatException e) {
                isNumber = false;
            }

            for (Posts post : postsList) {
                // Buscar por ID (si el texto es un número)
                if (isNumber && (post.getId() == searchId || post.getUserId() == searchId)) {
                    filteredPostsList.add(post);
                }
                // Buscar por título o cuerpo
                else if (post.getTitle().toLowerCase().contains(searchText) ||
                        post.getBody().toLowerCase().contains(searchText)) {
                    filteredPostsList.add(post);
                }
            }
        }

        if (filteredPostsList.isEmpty()) {
            // Si no hay resultados, mostrar un mensaje y recargar la lista completa
            Toast.makeText(this, "No se encontraron resultados. Mostrando lista completa.", Toast.LENGTH_SHORT).show();
            filteredPostsList.addAll(postsList); // Recargar con todos los posts
        }

        adapter = new PostsAdapter(ListPlaceHolder.this, filteredPostsList);
        listView.setAdapter(adapter);
    }
}