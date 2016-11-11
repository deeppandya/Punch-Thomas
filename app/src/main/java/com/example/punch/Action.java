package com.example.punch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Java class to select an action
 */

public class Action extends AppCompatActivity {

    Context context = this;
    Database database = new Database(context);
    Spinner spinner;
    String action, actionstring, image;
    ImageView btnstartwork, btnfinishwork, btnstartbreak, btnfinishbreak, btnstartlunch, btnfinishlunch, btnstartextra, btnfinishextra;
    String share, language, employeecode, firstname;
    ArrayList<HashMap<String, String>> userlist = new ArrayList<HashMap<String, String>>();
    SharedPreferences settings;
    Editor editor;
    String ACTION_START_WORK_EN = "I start my work";
    String ACTION_END_WORK_EN = "I finish my work";
    String ACTION_START_LUNCH_EN = "I start my lunch";
    String ACTION_END_LUNCH_EN = "I finish my lunch";
    String ACTION_START_BREAK_EN = "I have a break";
    String ACTION_END_BREAK_EN = "I finish my break";
    String ACTION_START_EXTRA_EN = "I start my extra";
    String ACTION_END_EXTRA_EN = "I finish my extra";

    String ACTION_START_WORK_FR = "J'arrive au travail";
    String ACTION_END_WORK_FR = "J'ai fini ma journ\u00E9e";
    String ACTION_START_LUNCH_FR = "Je pars luncher";
    String ACTION_END_LUNCH_FR = "J'ai fini de luncher";
    String ACTION_START_BREAK_FR = "Je pars en pause";
    String ACTION_END_BREAK_FR = "J'ai fini ma pause";
    String ACTION_START_EXTRA_FR = "Je fais un extra";
    String ACTION_END_EXTRA_FR = "J'ai fini mon extra.";
    private Toolbar toolbar;
    private TextView txtActionMsg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action);

        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ImageView imgLogo=(ImageView)toolbar.findViewById(R.id.imgLogo);
        imgLogo.setVisibility(View.GONE);

        AppCompatButton btnRefresh=(AppCompatButton)toolbar.findViewById(R.id.btnRefresh);
        btnRefresh.setVisibility(View.GONE);

        AppCompatButton btnReset=(AppCompatButton)toolbar.findViewById(R.id.btnReset);
        btnReset.setVisibility(View.GONE);

        AppCompatButton btnExit=(AppCompatButton)toolbar.findViewById(R.id.btnExit);
        btnExit.setVisibility(View.GONE);

        txtActionMsg=(TextView)toolbar.findViewById(R.id.txtActionMsg);
        txtActionMsg.setVisibility(View.VISIBLE);

        settings = PreferenceManager.getDefaultSharedPreferences(context);
        editor = settings.edit();
        btnstartwork = (ImageView) findViewById(R.id.btnstartwork);
        btnfinishwork = (ImageView) findViewById(R.id.btnfinishwork);
        btnstartbreak = (ImageView) findViewById(R.id.btnstartbreak);
        btnfinishbreak = (ImageView) findViewById(R.id.btnfinishbreak);
        btnstartlunch = (ImageView) findViewById(R.id.btnstartlunch);
        btnfinishlunch = (ImageView) findViewById(R.id.btnfinishlunch);
        btnstartextra = (ImageView) findViewById(R.id.btnstartextra);
        btnfinishextra = (ImageView) findViewById(R.id.btnfinishextra);

        btnstartwork.setOnClickListener(onClickListener);

        btnfinishwork.setOnClickListener(onClickListener);

        btnstartbreak.setOnClickListener(onClickListener);

        btnfinishbreak.setOnClickListener(onClickListener);

        btnstartlunch.setOnClickListener(onClickListener);

        btnfinishlunch.setOnClickListener(onClickListener);

        btnstartextra.setOnClickListener(onClickListener);

        btnfinishextra.setOnClickListener(onClickListener);
        language = settings.getString("language", "");
        employeecode = settings.getString("employeecode", "");

        firstname = database.GetUserById(employeecode.trim()).get(Database.KEY_FIRSTNAME);

        if (language.equals("fr")) {
            btnstartwork.setImageResource(R.drawable.frstartwork);
            btnfinishwork.setImageResource(R.drawable.frfinishwork);
            btnstartbreak.setImageResource(R.drawable.frstartbreak);
            btnfinishbreak.setImageResource(R.drawable.frfinishbreak);
            btnstartlunch.setImageResource(R.drawable.frstartlunch);
            btnfinishlunch.setImageResource(R.drawable.frfinishlunch);
            btnstartextra.setImageResource(R.drawable.frstartextra);
            btnfinishextra.setImageResource(R.drawable.frfinishextra);
            txtActionMsg.setText("Bonjour ,  " + firstname);

        } else if (language.equals("en")) {
            btnstartwork.setImageResource(R.drawable.startwork);
            btnfinishwork.setImageResource(R.drawable.finishwork);
            btnstartbreak.setImageResource(R.drawable.havebreak);
            btnfinishbreak.setImageResource(R.drawable.finishbreak);
            btnstartlunch.setImageResource(R.drawable.startlunch);
            btnfinishlunch.setImageResource(R.drawable.finishlunch);
            btnstartextra.setImageResource(R.drawable.startextra);
            btnfinishextra.setImageResource(R.drawable.finishextra);
            txtActionMsg.setText("Hello , " + firstname);

        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            ImageView imagebtn = (ImageView) findViewById(v.getId());
            imagebtn.setColorFilter(Color.argb(206, 216, 246, 255));
            switch (v.getId()) {
                case R.id.btnstartwork:
                    action = "Travail";
                    if (language.equals("fr")) {
                        actionstring = ACTION_START_WORK_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_START_WORK_EN;
                    }
                    break;
                case R.id.btnfinishwork:
                    action = "sortie";
                    if (language.equals("fr")) {
                        actionstring = ACTION_END_WORK_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_END_WORK_EN;
                    }
                    break;
                case R.id.btnstartbreak:
                    action = "Pause";
                    if (language.equals("fr")) {
                        actionstring = ACTION_START_BREAK_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_START_BREAK_EN;
                    }
                    break;
                case R.id.btnfinishbreak:
                    action = "endpause";
                    if (language.equals("fr")) {
                        actionstring = ACTION_END_BREAK_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_END_BREAK_EN;
                    }
                    break;
                case R.id.btnstartlunch:
                    action = "lunch";
                    if (language.equals("fr")) {
                        actionstring = ACTION_START_LUNCH_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_START_LUNCH_EN;
                    }
                    break;
                case R.id.btnfinishlunch:
                    action = "endlunch";
                    if (language.equals("fr")) {
                        actionstring = ACTION_END_LUNCH_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_END_LUNCH_EN;
                    }
                    break;
                case R.id.btnstartextra:
                    action = "Extra";
                    if (language.equals("fr")) {
                        actionstring = ACTION_START_EXTRA_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_START_EXTRA_EN;
                    }
                    break;
                case R.id.btnfinishextra:
                    action = "endextra";
                    if (language.equals("fr")) {
                        actionstring = ACTION_END_EXTRA_FR;
                    } else if (language.equals("en")) {
                        actionstring = ACTION_END_EXTRA_EN;
                    }
                    break;
            }
            Intent intent = new Intent(Action.this, Middleware.class);
            intent.putExtra("action", action);
            intent.putExtra("actionstring", actionstring);
            startActivityForResult(intent, 2);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

        }
    }
}
