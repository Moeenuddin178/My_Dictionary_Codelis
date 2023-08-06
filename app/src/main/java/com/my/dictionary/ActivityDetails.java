package com.my.dictionary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.my.dictionary.data.AppConfig;
import com.my.dictionary.data.DatabaseManager;
import com.my.dictionary.data.DatabaseUserManager;
import com.my.dictionary.data.GDPR;
import com.my.dictionary.data.GlobalVariable;
import com.my.dictionary.data.Tools;
import com.my.dictionary.model.Word;
import com.my.dictionary.theme.ActionBarColoring;

import java.util.Locale;

public class ActivityDetails extends AppCompatActivity {

    private AdView mAdView;
    private Toolbar toolbar;

    private Word item_word;
    private TextView textView_word, textView_result, textView_edited;
    private TextToSpeech ttobj;
    private Menu menu;
    private boolean checked = false;
    private DatabaseManager db;
    private DatabaseUserManager db_user;
    private GlobalVariable global;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parent_view = findViewById(android.R.id.content);

        Intent i = getIntent();
        item_word = (Word) i.getSerializableExtra("object_word");

        db = new DatabaseManager(getApplicationContext());
        db_user = new DatabaseUserManager(getApplicationContext());
        global = (GlobalVariable) getApplication();
        initAds();

        initComponent();
        checkEditable();
        setupToolbar();

    }


    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        new ActionBarColoring(this).getColor(actionBar);
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        textView_word = (TextView) findViewById(R.id.textView_word);
        textView_result = (TextView) findViewById(R.id.textView_result);
        textView_edited = (TextView) findViewById(R.id.textView_edited);
        // set TTS language
        ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (textView_word.getText().toString().matches("(?s).*[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF].*"))
                    {
                        /*There is an Urdu character*/
                        ttobj.setLanguage(new Locale("ur"));
                    }
                    else {
                    ttobj.setLanguage(Locale.US);}
                }
            }
        });
    }

    public void checkEditable() {
        if (!item_word.getEdited().equals("-")) {
            textView_edited.setText("Edited: " + item_word.getEdited());
            textView_edited.setVisibility(View.VISIBLE);
        } else {
            textView_edited.setVisibility(View.GONE);
        }
        textView_word.setText(item_word.getWord());
        textView_result.setText(item_word.getResult());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        if (item_word.isUserDb()) {
            getMenuInflater().inflate(R.menu.activity_details_user, menu);
        } else {
            getMenuInflater().inflate(R.menu.activity_details, menu);
            if (!item_word.getFavorites().equals("-")) {
                checked = true;
                menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_favorites_solid));
            } else {
                checked = false;
                menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_favorites_outline));
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_speak:
                ttobj.speak(textView_word.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.action_favorites:
                if (checked) {
                    menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_favorites_outline));
                    item_word.setFavorites("-");
                    db.updateFavorites(global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME()), item_word);
                    Snackbar.make(parent_view, "'" + textView_word.getText().toString() + "' Removed from favorites", Snackbar.LENGTH_SHORT).show();
                    global.decreaseFavorites();
                    checked = false;
                } else {
                    menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_favorites_solid));
                    item_word.setFavorites("true");
                    db.updateFavorites(global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME()), item_word);
                    Snackbar.make(parent_view, "'" + textView_word.getText().toString() + "' Added to favorites", Snackbar.LENGTH_SHORT).show();
                    global.increaseFavorites();
                    checked = true;
                }
                break;

            case R.id.action_share:
                methodShare(textView_word.getText().toString(), textView_result.getText().toString());
                break;

            case R.id.action_edit:
                dialogEditWord(item_word);
                break;

            case R.id.action_delete:
                dialogDeleteConfirmation();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogEditWord(Word word) {
        final Word this_word = word;
        final Dialog dialog = new Dialog(ActivityDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_edit_record);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText editText_word = (EditText) dialog.findViewById(R.id.editText_word);
        final EditText editText_result = (EditText) dialog.findViewById(R.id.editText_result);
        Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);
        Button button_save = (Button) dialog.findViewById(R.id.button_save);
        editText_word.setText(this_word.getWord());
        editText_result.setText(this_word.getResult());
        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_word.getText().toString().equals("") && !editText_result.getText().toString().equals("")) {
                    this_word.setWord(editText_word.getText().toString());
                    this_word.setResult(editText_result.getText().toString());
                    this_word.setEdited(global.generateCurrentDate(2));
                    db_user.updateRecord(global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME()), this_word);
                    item_word.setWord(this_word.getWord());
                    item_word.setResult(this_word.getResult());
                    checkEditable();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void methodShare(String word, String result) {
        //final String pack =this.getPackageName();
        final String marketUrl = getResources().getString(R.string.app_url);
        String shareBody = word + "\n" + result + "\nTranslate using - " + marketUrl;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Result");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    protected void dialogDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Word Confirmation");
        builder.setMessage("Are you sure remove this word from database?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db_user.deleteRecord(global.getStringPref(global.S_KEY_TABLE, db.getTABLE2_NAME()), item_word);
                Snackbar.make(parent_view, "Word deleted", Snackbar.LENGTH_SHORT).show();
                textView_word.setText("");
                textView_result.setText("");
                textView_edited.setText("");
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (textView_word.getText().toString().matches("(?s).*[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF].*"))
                    {
                        /*There is an Urdu character*/
                        ttobj.setLanguage(new Locale("ur"));
                    }
                    else {
                        ttobj.setLanguage(Locale.US);}
                }
            }
        });

        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }

        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void initAds() {
        if (!AppConfig.ENABLE_DETAILS_BANNER) return;
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, GDPR.getBundleAd(this)).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }
        });
    }

}
