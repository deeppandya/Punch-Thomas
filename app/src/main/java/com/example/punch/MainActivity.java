package com.example.punch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


/**
 * Java class to register company and start the application
 */
public class MainActivity extends AppCompatActivity {

    static Context context;
    Database database;
    AppCompatButton btnsubmit;
    EditText txtcompanycode, txtpassword, txturl, txtcompanyimage;
    String language, companycode, password, url;
    RadioButton rdenglish, rdfrench;
    Double lng, lat, altitude;
    GPSTracker gps;
    Activity activity;
    byte[] imagedata;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private int REQUEST_LOCATION = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        activity = this;
        context = activity.getBaseContext();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ImageView imgLogo = (ImageView) toolbar.findViewById(R.id.imgLogo);
        imgLogo.setVisibility(View.GONE);

        AppCompatButton btnRefresh = (AppCompatButton) toolbar.findViewById(R.id.btnRefresh);
        btnRefresh.setVisibility(View.GONE);

        AppCompatButton btnReset = (AppCompatButton) toolbar.findViewById(R.id.btnReset);
        btnReset.setVisibility(View.GONE);

        AppCompatButton btnExit = (AppCompatButton) toolbar.findViewById(R.id.btnExit);
        btnExit.setVisibility(View.GONE);

        TextView txtActionMsg = (TextView) toolbar.findViewById(R.id.txtActionMsg);
        txtActionMsg.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("EXIT", false)) {
            finish();
        } else {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            companycode = settings.getString("companycode", "");
            password = settings.getString("password", "");
            language = settings.getString("language", "");
            url = settings.getString("url", "");
            if (companycode.equals("") || password.equals("") || url.equals("") || language.equals("")) {
                txtcompanycode = (EditText) findViewById(R.id.company_code);
                txtcompanycode.setText("DEMO");
                txtpassword = (EditText) findViewById(R.id.password);
                txtpassword.setText("!E*!");
                txturl = (EditText) findViewById(R.id.url);
                //txturl.setText("207.134.119.16:88");
                txturl.setText("granitedrc.no-ip.info:88");
                rdenglish = (RadioButton) findViewById(R.id.rdenglish);
                rdenglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (buttonView.isChecked()) {
                            language = "en";
                        }
                    }
                });
                rdfrench = (RadioButton) findViewById(R.id.rdfrench);
                rdfrench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (buttonView.isChecked()) {
                            language = "fr";
                        }
                    }
                });
                if (rdenglish.isChecked()) {
                    language = "en";
                } else if (rdfrench.isChecked()) {
                    language = "fr";
                }
                database = new Database(context);
                txtcompanyimage = (EditText) findViewById(R.id.company_image);
                txtcompanyimage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        GetImageFromGallery();
                    }
                });
                btnsubmit = (AppCompatButton) findViewById(R.id.btnsubmit);
                btnsubmit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        checkPermissions();
                    }

                });
            } else {
                startActivity(new Intent(context, EmployeeCode.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                gps = new GPSTracker(context);
                if (!gps.canGetLocation()) {
                    Toast.makeText(context, getResources().getString(R.string.location_permission), Toast.LENGTH_SHORT).show();
                    lng = 0.0;
                    lat = 0.0;
                    altitude = 0.0;
                    Toast.makeText(context, "Location services are unavailable.", Toast.LENGTH_LONG).show();
                    //gps.showSettingsAlert();
                } else {
                    lng = gps.getLongitude();
                    lat = gps.getLatitude();
                    altitude = gps.getAltitude();

                    registerToServer register = new registerToServer(context, txturl.getText().toString(), txtcompanycode.getText().toString(), txtpassword.getText().toString());
                    register.execute();
                }
            }
        }
    }

    public void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        boolean isPermit = false;

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            isPermit = true;
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            isPermit = true;
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            isPermit = true;
            permissions.add(android.Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            isPermit = true;
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            isPermit = true;
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (isPermit) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), REQUEST_LOCATION);
        } else {
            gps = new GPSTracker(context);
            if (!gps.canGetLocation()) {
                Toast.makeText(context, getResources().getString(R.string.location_permission), Toast.LENGTH_SHORT).show();
                lng = 0.0;
                lat = 0.0;
                altitude = 0.0;
                Toast.makeText(context, "Location services are unavailable.", Toast.LENGTH_LONG).show();
                //gps.showSettingsAlert();
            } else {
                lng = gps.getLongitude();
                lat = gps.getLatitude();
                altitude = gps.getAltitude();

                registerToServer register = new registerToServer(context, txturl.getText().toString(), txtcompanycode.getText().toString(), txtpassword.getText().toString());
                register.execute();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("You are exiting the application.\nAre you sure ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public void GetImageFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            RandomAccessFile f;
            try {
                f = new RandomAccessFile(picturePath, "rw");
                imagedata = new byte[(int) f.length()];
                f.read(imagedata);
                f.close();
                txtcompanyimage.setText(picturePath);
            } catch (Exception ex) {
                ex.getMessage();
                txtcompanyimage.setText("Cannot assgin image form gallery.");
            }

        }
    }

    public class registerToServer extends AsyncTask<Void, Void, Void> {

        HttpResponse response;
        Context context;
        String urlString = "";
        String _data = "";
        String username;
        String password;

        public registerToServer(Context context, String urlString, String username, String password) {
            this.context = context;
            this.urlString = urlString;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(activity, R.style.AppTheme_ProgressDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Common.CheckConnection(context)) {


                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://" + urlString + "/erp/punch.nsf/(firstconnexion)?openagent&comp=" + username + "&password=" + password + "&time=" + Common.GetCurrentDate() + "&mac=" + Common.GetMACAddress(context) + "&lat=" + lat + "&lon=" + lng + "&alt=" + altitude + "&version=1.00&veri=1.00");

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String body = "";
                    while ((body = rd.readLine()) != null) {
                        _data = _data + "\n" + body;
                    }
                    Log.d("Http Response:", _data);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "No Connection\n\nYou have to be connected for the first time", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            try {
                Document doc = Jsoup.parse(_data);
                List<org.jsoup.nodes.Node> elements = doc.select("body").first().childNodes();
                String data = "";
                for (int i = 0; i < elements.size(); i++) {
                    data = data + elements.get(i).toString();
                }
                String str = data.replaceAll("[\t\n\r]", "");
                str = str.trim();
                boolean check = str.equals("0");
                if (check) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("companycode", txtcompanycode.getText().toString());
                    editor.putString("language", language);
                    editor.putString("url", txturl.getText().toString());
                    editor.putString("password", txtpassword.getText().toString());
                    editor.commit();
                    database.dropCompanyImageTable();
                    database.addCompanyImage(imagedata);
                    GetUsers users = new GetUsers(context, true);
                    users.start();
                    progressDialog.cancel();
                } else {
                    AlertDialogManager alert = new AlertDialogManager();
                    alert.showAlertDialog(context, "Alert", data, false);
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }
}
