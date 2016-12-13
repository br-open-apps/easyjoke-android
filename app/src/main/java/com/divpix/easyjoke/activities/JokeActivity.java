package com.divpix.easyjoke.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.divpix.easyjoke.R;
import com.divpix.easyjoke.models.Category;
import com.divpix.easyjoke.models.Joke;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final String LOG_TAG = JokeActivity.class.getSimpleName();

    public static final String JOKE = "joke";
    public static final String JOKE_CATEGORY = "category";

    @BindView(R.id.contentJoke)
    public TextView contentJoke;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Joke mJoke;
    Category mCategory;
    TextToSpeech mTts;

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

            if (mTts == null) {
                try {
                    mTts = new TextToSpeech(this, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Text to Speech error", e);
                }
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
        MenuItem menuItemShare = menu.findItem(R.id.action_share);
        menuItemShare.setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_share)
                .actionBar().color(Color.WHITE));

        MenuItem menuItemTextSpeech = menu.findItem(R.id.action_speech);
        menuItemTextSpeech.setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_speaker_notes)
                .actionBar().color(Color.WHITE));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_share:
                startActivity(getShareIntent());
                break;
            case R.id.action_speech:
                if (mTts.isSpeaking()) {
                    mTts.stop();
                    item.setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_speaker_notes)
                            .actionBar().color(Color.WHITE));
                } else {
                    item.setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_speaker_notes_off)
                            .actionBar().color(Color.WHITE));
                    mTts.speak(mJoke.getContent(), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mTts != null) {
            mTts.stop();
            mTts.shutdown();
            Log.d(LOG_TAG, "TTS Destroyed");
        }
        super.onBackPressed();
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && mTts != null) {
            int result = mTts.setLanguage(new Locale("pt", "BR"));

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(LOG_TAG, "This Language is not supported");
            }
        } else {
            Log.e(LOG_TAG, "Initialization Failed!");
        }
    }
}
