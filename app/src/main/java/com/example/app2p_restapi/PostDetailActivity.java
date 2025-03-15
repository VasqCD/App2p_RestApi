package com.example.app2p_restapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app2p_restapi.Config.RestApiMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class PostDetailActivity extends AppCompatActivity {

    private TextView txtId;
    private TextView txtUserId;
    private TextView txtTitle;
    private TextView txtBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_detail);

        // Inicializar vistas
        txtId = findViewById(R.id.txtDetailId);
        txtUserId = findViewById(R.id.txtDetailUserId);
        txtTitle = findViewById(R.id.txtDetailTitle);
        txtBody = findViewById(R.id.txtDetailBody);

        // Obtener el ID del post de los extras
        int postId = getIntent().getIntExtra("post_id", -1);

        if (postId != -1) {
            fetchPostDetail(postId);
        } else {
            Toast.makeText(this, "Error: ID de post no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchPostDetail(int postId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = RestApiMethods.EndPoint + "/" + postId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA_DETALLE", response);

                        try {
                            // Parsear el JSON
                            JSONObject jsonObject = new JSONObject(response);

                            // Actualizar las vistas con los datos del post
                            txtId.setText("ID: " + jsonObject.getInt("id"));
                            txtUserId.setText("Usuario ID: " + jsonObject.getInt("userId"));
                            txtTitle.setText(jsonObject.getString("title"));
                            txtBody.setText(jsonObject.getString("body"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PostDetailActivity.this,
                                    "Error al procesar los datos: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR_DETALLE", error.toString());
                        Toast.makeText(PostDetailActivity.this,
                                "Error de conexión: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }
}