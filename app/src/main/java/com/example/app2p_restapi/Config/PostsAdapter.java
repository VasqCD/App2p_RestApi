package com.example.app2p_restapi.Config;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app2p_restapi.PostDetailActivity;
import com.example.app2p_restapi.R;

import java.util.List;

public class PostsAdapter extends ArrayAdapter<Posts> {
    private Context context;
    private List<Posts> postsList;

    public PostsAdapter(@NonNull Context context, List<Posts> list) {
        super(context, R.layout.posts_item, list);
        this.context = context;
        this.postsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.posts_item, null);
        }

        Posts post = getItem(position);

        if (post != null) {
            TextView txtId = view.findViewById(R.id.txtId);
            TextView txtTitle = view.findViewById(R.id.txtTitle);
            TextView txtBody = view.findViewById(R.id.txtBody);

            txtId.setText("ID: " + post.getId() + " (Usuario: " + post.getUserId() + ")");
            txtTitle.setText(post.getTitle());
            txtBody.setText(post.getBody());

            // Agregar el listener de clic al elemento de la lista
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("post_id", post.getId());
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }
}