package com.alexandremota.easyjoke_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.alexandremota.easyjoke_android.items.JokeItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeActivity extends AppCompatActivity {

    public static final String JOKE_TITLE = "title";
    public static final String JOKE_CONTENT = "content";

    @BindView(R.id.titleJoke)
    public TextView titleJoke;

    @BindView(R.id.contentJoke)
    public TextView contentJoke;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        ButterKnife.bind(this);


        getSupportActionBar();



        titleJoke.setText(getIntent().getExtras().getString(JOKE_TITLE));

       contentJoke.setText(getIntent().getExtras().getString(JOKE_CONTENT));

//        getSupportActionBar().setSubtitle("Teste Categoria");
    }
}
