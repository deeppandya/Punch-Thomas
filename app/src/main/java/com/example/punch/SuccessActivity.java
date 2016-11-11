package com.example.punch;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Java class to display success message
 */

public class SuccessActivity extends Activity{
	Activity activity;
	Context context;
	Database database;
	SharedPreferences settings;
	ProgressBar dialog;
    String companycode,url,Password,language,share,employee_id,actionstring,firstname;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.successmsg);
	        activity=this;
	        context=getBaseContext();
	        settings = PreferenceManager.getDefaultSharedPreferences(context);
	    	database=new Database(context);
	    	dialog=(ProgressBar)findViewById(R.id.loadingbar);
            dialog.setVisibility(View.VISIBLE);           
            dialog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B3FFFFFF"), Mode.SRC_IN);
            ImageView companyicon=(ImageView)findViewById(R.id.company_logo);
            byte[] image_data=database.getCompanyImage();
            if(image_data!=null)
            {
            	Bitmap bmp = BitmapFactory.decodeByteArray(database.getCompanyImage(), 0, database.getCompanyImage().length);
                companyicon.setImageBitmap(bmp);
            }
            else
            {
            	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.company);
            	ByteArrayOutputStream stream = new ByteArrayOutputStream();
            	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            	byte[] bitMapData = stream.toByteArray();
            	Bitmap bmp = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
                companyicon.setImageBitmap(bmp);
            }
	        language=settings.getString("language", "");
	        employee_id=settings.getString("employeecode", "");
	        firstname=database.GetUserById(employee_id.trim()).get(Database.KEY_FIRSTNAME);
	        actionstring=getIntent().getStringExtra("actionstring");
	        String successmsg = null;
      		if(language.equals("fr"))
      		{
      			successmsg="Merci "+firstname+",\n\n Votre action ' "+actionstring+" ' a \u00E9t\u00E9 enregistr\u00E9e.";
      		}
      		else if(language.equals("en"))
      		{
      			successmsg="Thanks "+firstname+",\n\n Your action ' "+actionstring+" ' has been saved.";
      		}
      		TextView txtmsg=(TextView)findViewById(R.id.successmsg);
      		txtmsg.setText(successmsg);
      		
      		Handler handler = new Handler();
      		handler.postDelayed(new Runnable() {
      		    public void run() {
      		    	startActivity(new Intent(context, EmployeeCode.class));
      		    }
      		}, 3000);
	    }
}
