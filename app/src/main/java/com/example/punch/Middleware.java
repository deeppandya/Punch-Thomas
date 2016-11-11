package com.example.punch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;


public class Middleware extends AppCompatActivity implements SurfaceHolder.Callback{

	Camera mCamera;
    SurfaceView mPreview;
    Context context=this;
    static final String KEY_USER = "user";
    ArrayList<HashMap<String, String>> userlist=new ArrayList<HashMap<String, String>>();
    Button btnsubmit;
    ProgressBar loading;
    EditText txtcompanycode;
    TextView txtmsg;
    RadioButton rdyes,rdno,rdenglish,rdfrench;
    String employee_id,action,actionstring,language;
    SharedPreferences settings;
	Editor editor;
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.camera);
        Intent mIntent = getIntent();
        settings= PreferenceManager.getDefaultSharedPreferences(context);
        editor=settings.edit();
        action = mIntent.getStringExtra("action");
        actionstring = mIntent.getStringExtra("actionstring");
        loading=(ProgressBar)findViewById(R.id.loadingbar);
        loading.setVisibility(View.VISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(Color.BLACK, Mode.SRC_IN);
        txtmsg=(TextView)findViewById(R.id.txtmsg);
        language=settings.getString("language", "");
        if(language.equals("fr"))
        {
        	txtmsg.setText("Votre action est en cours d'enregistrement, Merci de patienter");
        }
        else if(language.equals("en"))
        {
        	txtmsg.setText("Loading...\n\nYour action is being submitted.\n\n Please Wait...");
        }
        mPreview = (SurfaceView)findViewById(R.id.camerapreview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCamera = Camera.open(Common.findFrontFacingCamera());
        //mCamera.takePicture(null,null,new PhotoHandler(this));
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        Log.d("CAMERA","Destroy");
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);
        
        mCamera.setDisplayOrientation(180);
        mCamera.startPreview();
        mCamera.takePicture(null,null,new PhotoHandler(this,action,actionstring));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW","surfaceDestroyed");
        mCamera.release();
    }
}