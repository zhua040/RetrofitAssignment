package com.example.retrofitassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    //Widget
    private Button btnShowAll, btnSearch, btnClear;
    private EditText edtUserID;

    Integer number = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign vars
        btnShowAll = findViewById(R.id.btn_showAll);
        btnSearch = findViewById(R.id.btn_Search);
        btnClear = findViewById(R.id.btn_Clear);
        edtUserID = findViewById(R.id.edt_userId);
        textViewResult = findViewById(R.id.text_view_result);

        // Retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Can not create an instance of jsonPlaceHolderApi, because of Interface, but use retrofit to implement the methods
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);



        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosts();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = edtUserID.getText().toString();
                number = Integer.parseInt(value);
                getPosts();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResult.setText("");
            }
        });

        //getPosts();
        //getComments();
    }

    //1. getPost()
    private void getPosts() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(number, null, null, null); // create the body of the method-getPost()

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() != 200) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // handle the error & display it
                textViewResult.setText(t.getMessage());
            }
        });
    }

    // 2. getComments()
    private void getComments() {
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Comment> comments = response.body();
                for (Comment com : comments) {
                    String content = "";
                    content += "ID: " + com.getId() + "\n";
                    content += "Post ID: " + com.getPostId() + "\n";
                    content += "Name: " + com.getName() + "\n";
                    content += "Email: " + com.getEmail() + "\n";
                    content += "Text: " + com.getText() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // handle the error & display it
                textViewResult.setText(t.getMessage());
            }
        });


    }


}