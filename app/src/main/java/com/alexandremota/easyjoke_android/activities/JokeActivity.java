package com.alexandremota.easyjoke_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alexandremota.easyjoke_android.R;
import com.alexandremota.easyjoke_android.models.Category;
import com.alexandremota.easyjoke_android.models.Joke;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    public static final String JOKE_CATEGORY = "category";
    @BindView(R.id.contentJoke)
    public TextView contentJoke;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Joke mJoke;
    Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getIntent().getExtras() != null) {
            mJoke = getIntent().getExtras().getParcelable(JOKE);
            if (mJoke != null) {
                contentJoke.setText(mJoke.getContent());
                getSupportActionBar().setTitle(mJoke.getTitle());
            }

            mCategory = getIntent().getExtras().getParcelable(JOKE_CATEGORY);
            if (mCategory != null) {
                getSupportActionBar().setSubtitle(mCategory.getName());
            }

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_joke, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_share)
                .actionBar().color(Color.WHITE));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_share:
                startActivity(Intent.createChooser(getShareIntent(),
                        getString(R.string.share)));
            case android.R.id.home:
                this.onBackPressed();
        }
        return true;
    }

    private Intent getShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, formatJokeToShare());
        intent.setType("text/plain");
        return intent;
    }

    private String formatJokeToShare() {
        StringBuilder result = new StringBuilder();
        result.append(mJoke.getTitle()).append("\n\n");
        result.append(Html.fromHtml(mJoke.getContent()));
        return result.toString();
    }

}
