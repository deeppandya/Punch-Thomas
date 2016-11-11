package com.example.punch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Java class to enter employee code
 */

public class EmployeeCode extends AppCompatActivity {

    Context context;
    Button btnsubmit, btnreset;
    EditText txtemployeecode;
    String employeecode, Language;
    Database database;
    Double lng, lat, altitude;
    GPSTracker gps;
    Activity activity;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.employeecode);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.customtitle);
        activity = this;
        context = getBaseContext();
        database = new Database(context);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imgLogo = (ImageView) toolbar.findViewById(R.id.imgLogo);
        imgLogo.setVisibility(View.VISIBLE);

        AppCompatButton btnRefresh = (AppCompatButton) toolbar.findViewById(R.id.btnRefresh);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    String password = "";

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        password = settings.getString("password", "");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.password_dialog);
                        dialog.setTitle("");
                        final EditText txtpassword = (EditText) dialog.findViewById(R.id.txtpassword);
                        dialog.show();
                        Button submitpassword = (Button) dialog.findViewById(R.id.btnpasswordsubmit);
                        submitpassword.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (password.equals(txtpassword.getText().toString())) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                                    String msg = "";
                                    if (Language.equals("fr")) {
                                        msg = "\n" +"Vous actualisez l\'utilisateur ensemble de donn\u00E9es. \nEs-tu s\u00FBr ?";
                                    } else {
                                        msg = "You are refreshing the user dataset.\nAre you sure ?";
                                    }
                                    alert.setMessage(msg).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                                } else {
                                    AlertDialogManager alert = new AlertDialogManager();
                                    if (Language.equals("fr")) {
                                        alert.showAlertDialog(activity, "Alert", "Le mot de passe ne correspond pas.", false);
                                    } else {
                                        alert.showAlertDialog(activity, "Alert", "Password doesn't match.", false);
                                    }

                                }
                            }
                        });
                    }

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    GetUsers user=new GetUsers(context,false);
                                    user.start();
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                };
                task.execute((Void[]) null);
            }
        });

        AppCompatButton btnReset = (AppCompatButton) toolbar.findViewById(R.id.btnReset);
        btnReset.setVisibility(View.VISIBLE);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    String password = "";

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        password = settings.getString("password", "");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.password_dialog);
                        dialog.setTitle("");
                        final EditText txtpassword = (EditText) dialog.findViewById(R.id.txtpassword);
                        dialog.show();
                        Button submitpassword = (Button) dialog.findViewById(R.id.btnpasswordsubmit);
                        submitpassword.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (password.equals(txtpassword.getText().toString())) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                                    String msg = "";
                                    if (Language.equals("fr")) {
                                        msg = "Vous r\u00E9initialisez les pr\u00E9f\u00E9rences de l'application. \n\u00C9tes-vous s\u00FBr ?";
                                    } else {
                                        msg = "You are resetting the application preferences.\nAre you sure ?";
                                    }
                                    alert.setMessage(msg).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                                } else {
                                    /*AlertDialog alertDialog = new AlertDialog.Builder(EmployeeCode.this).create();
                          	        alertDialog.setTitle("Alert Dialog");
                          	        alertDialog.setMessage("Password doesn't match.");
                          	        alertDialog.setIcon(R.drawable.logo);
                          	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                          	                public void onClick(DialogInterface dialog, int which) {
                          	                }
                          	        });
                          	        alertDialog.show();*/

                                    AlertDialogManager alert = new AlertDialogManager();
                                    if (Language.equals("fr")) {
                                        alert.showAlertDialog(activity, "Alert", "Le mot de passe ne correspond pas.", false);
                                    } else {
                                        alert.showAlertDialog(activity, "Alert", "Password doesn't match.", false);
                                    }

                                }
                            }
                        });
                    }

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.remove("companycode");
                                    editor.remove("share");
                                    editor.remove("language");
                                    editor.remove("url");
                                    editor.remove("password");
                                    editor.commit();
                                    database.dropUserTable();
                                    database.dropRequestTable();
                                    database.dropCompanyImageTable();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                };
                task.execute((Void[]) null);
            }
        });

        AppCompatButton btnExit = (AppCompatButton) toolbar.findViewById(R.id.btnExit);
        btnExit.setVisibility(View.VISIBLE);
        btnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    String password = "";

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        password = settings.getString("password", "");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.password_dialog);
                        dialog.setTitle("");
                        final EditText txtpassword = (EditText) dialog.findViewById(R.id.txtpassword);
                        dialog.show();
                        Button submitpassword = (Button) dialog.findViewById(R.id.btnpasswordsubmit);
                        submitpassword.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (password.equals(txtpassword.getText().toString())) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                                    String msg = "";
                                    if (Language.equals("fr")) {
                                        msg = "Vous quittez l'application. \n\u00C9tes-vous s\u00FBr ?";
                                    } else {
                                        msg = "You are Exiting the application.\nAre you sure ?";
                                    }
                                    alert.setMessage(msg).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                                } else {
	                            		/*AlertDialog alertDialog = new AlertDialog.Builder(EmployeeCode.this).create();
	                          	        alertDialog.setTitle("Alert Dialog");
	                          	        alertDialog.setMessage("Password doesn't match.");
	                          	        alertDialog.setIcon(R.drawable.logo);
	                          	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                          	                public void onClick(DialogInterface dialog, int which) {
	                          	                }
	                          	        });
	                          	        alertDialog.show();
	                          	        */
                                    AlertDialogManager alert = new AlertDialogManager();
                                    if (Language.equals("fr")) {
                                        alert.showAlertDialog(activity, "Alert", "Le mot de passe ne correspond pas.", false);
                                    } else {
                                        alert.showAlertDialog(activity, "Alert", "Password doesn't match.", false);
                                    }
                                }
                            }
                        });
                    }

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                };
                task.execute((Void[]) null);
            }
        });

        TextView txtActionMsg = (TextView) toolbar.findViewById(R.id.txtActionMsg);
        txtActionMsg.setVisibility(View.GONE);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Language = settings.getString("language", "");

        ImageView companyicon = (ImageView) findViewById(R.id.company_logo);
        byte[] image_data = database.getCompanyImage();
        if (image_data != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(database.getCompanyImage(), 0, database.getCompanyImage().length);
            companyicon.setImageBitmap(bmp);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.company);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
            companyicon.setImageBitmap(bmp);
        }

        //SendCommand();

        txtemployeecode = (EditText) findViewById(R.id.employee_code);
        btnsubmit = (Button) findViewById(R.id.btnemployeesubmit);
        btnsubmit.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if (database.IsUserExists(txtemployeecode.getText().toString())) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("employeecode", txtemployeecode.getText().toString());
                    editor.commit();
                    startActivity(new Intent(context, Action.class));
                } else {
      			/*AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
      	        alertDialog.setTitle("Alert Dialog");
      	        alertDialog.setMessage("Emplooyee Code doesn't exist.");
      	        alertDialog.setIcon(R.drawable.logo);
      	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
      	                public void onClick(DialogInterface dialog, int which) {
      	                }
      	        });
      	        alertDialog.show();*/
                    AlertDialogManager alert = new AlertDialogManager();

                    if (Language.equals("fr")) {
                        alert.showAlertDialog(activity, "Alert", "Le code employ\u00E9 n'existe pas.", false);
                    } else {
                        alert.showAlertDialog(activity, "Alert", "Employee Code doesn't exist.", false);
                    }
                }
            }
        });
        if (Language.equals("fr")) {
            txtemployeecode.setHint("Code de l'employ\u00E9");
            btnsubmit.setText("Soumettre");
        } else if (Language.equals("en")) {
            txtemployeecode.setHint("Employee code");
            btnsubmit.setText("Submit");
        }
    }
    /*@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if(keyCode == KeyEvent.KEYCODE_BACK)  
        {  
            Log.d("Test", "Back button pressed!");  
        }  
        else if(keyCode == KeyEvent.KEYCODE_HOME)  
        {  
            Log.d("Test", "Home button pressed!");  
        }  
        return super.onKeyDown(keyCode, event);  
    } */

    @Override
    public void onBackPressed() {

    }

    void SendCommand() {
        if (Common.CheckConnection(context)) {
            ArrayList<HashMap<String, String>> requests = new ArrayList<HashMap<String, String>>();
            requests = database.getAllRequests();
            if (requests.size() > 0) {
                for (int i = 0; i < requests.size(); i++) {
                    HashMap<String, String> requestparams = requests.get(i);
                    SubmitOfflineAction submitofflineaction = new SubmitOfflineAction(context, requestparams.get(Database.KEY_REQUEST), requestparams.get(Database.KEY_DATA), requestparams.get(Database.KEY_ID));
                    submitofflineaction.start();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SendCommand();
    }
}
