package com.alexandremota.easyjoke_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeActivity extends AppCompatActivity {

    public static final String JOKE_CONTENT = "joke_content";

    @BindView(R.id.titleJoke)
    private TextView titleJoke;

    @BindView(R.id.contentJoke)
    private TextView contentJoke;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        ButterKnife.bind(this);

        titleJoke.setText("");
        contentJoke.setText(getIntent().getExtras().getString(JOKE_CONTENT));

        getSupportActionBar().setSubtitle("Teste Categoria");
    }
}
