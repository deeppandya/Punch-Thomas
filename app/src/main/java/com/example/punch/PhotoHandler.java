package com.example.punch;

import java.io.File;
import java.io.FileOutputStream;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.widget.Toast;

/**
 * Java class to take a picture
 */
public class PhotoHandler implements PictureCallback {

  private final Context context;
  MainActivity main=new MainActivity();
  String share,language,companycode,xmldata;
  String employee_id,action,url,password,actionstring;
  ProgressDialog pDialog;
  GPSTracker gps;
  Double lng,lat,altitude;
  File pictureFile;

  public PhotoHandler(Context context,String Action,String Actionstring) {
    this.context = context;
    action=Action;
    actionstring=Actionstring;
    gps=new GPSTracker(context);
  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {
	  File pictureFileDir = Common.getDir();

	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
	      Toast.makeText(context, "Can't create directory to save image.",
	          Toast.LENGTH_LONG).show();
	      return;

	    }
	    String photoFile = "Picture_" + Common.GetCurrentDate() + ".jpg";

	    String filename = pictureFileDir.getPath() + File.separator + photoFile;

	    pictureFile = new File(filename);

	    try {
	      //FileOutputStream fos = new FileOutputStream(pictureFile);
	      //fos.write(data);
	      //fos.close();
	      //Toast.makeText(context, "New Image saved:" + photoFile,Toast.LENGTH_LONG).show();
	      SubmitAction submitaction=new SubmitAction(context, action,actionstring, data);
	      submitaction.start();
	    } catch (Exception error) {
	      Toast.makeText(context, "Image could not be saved.",
	          Toast.LENGTH_LONG).show();
	    }
	    camera.startPreview();
	    }
} 
