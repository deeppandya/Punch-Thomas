package com.example.punch;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Base64;

/**
 * Java class to submit the action to the server
 */

public class SubmitAction extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database database;
    private String password;
    SharedPreferences settings;
    ProgressDialog dialog;
    String companycode,url,Password,language,share,employee_id,action,actionstring,firstname;
    boolean first;
    GPSTracker gps;
    Double lng,lat,altitude;
    byte[] imagedata=null;
	
    SubmitAction(Context context,String action,String actionstring,byte[] data)
    {
    	this.context=context;
    	this.action=action;
    	this.actionstring=actionstring;
    	imagedata=data;
    	settings = PreferenceManager.getDefaultSharedPreferences(context);
    	database=new Database(context);
    	gps=new GPSTracker(context);
    	if(gps.canGetLocation())
        {
        	lng=gps.getLongitude();
        	lat=gps.getLatitude();
        	altitude=gps.getAltitude();
        }
  		else
  		{
  			lng=0.0;
        	lat=0.0;
        	altitude=0.0;
        	gps.showSettingsAlert();
  		}
    }
    
	@SuppressWarnings("unused")
	@Override
    public void run() 
	{
		Looper.prepare();		
		share=settings.getString("share", "");
        language=settings.getString("language", "");
        companycode=settings.getString("companycode", "");
        employee_id=settings.getString("employeecode", "");
        url=settings.getString("url", "");
        password=settings.getString("password", "");
        //firstname=database.GetUserById(employee_id.trim()).get(Database.KEY_FIRSTNAME);
        HttpClient httpClient = new DefaultHttpClient();
		//if(Common.CheckConnection(context))
        /*if(false)
		{
			try {
	      String encodedString = Base64.encodeToString(imagedata, 0);
			HttpPost httpPost = new HttpPost("http://"+url+"/erp/punch.nsf/(punch)?openagent&version=1.00&flux=1.00&comp="+companycode+"&password="+password+"&id="+employee_id.replaceAll("[\t\n\r]", "").trim()+"&share="+share+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&time="+Common.GetCurrentDate()+"&action="+action+"&lang="+language);
			List<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
	      namevaluepair.add(new BasicNameValuePair("file", encodedString));
	      httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
	            HttpResponse response = httpClient.execute(httpPost);
	 
	            
	            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	            String body = "";
	            String _data="";
	            while ((body = rd.readLine()) != null) 
	            {
	            _data=_data+"\n"+body;
	            }
	            xmldata(_data);
	            Log.d("Http Response:", _data);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		else
		{*/
			try {
			String encodedString = Base64.encodeToString(imagedata, 0);
			String request="http://"+url+"/erp/punch.nsf/(punch)?openagent&version=1.00&flux=1.00&comp="+companycode+"&password="+password+"&id="+employee_id.replaceAll("[\t\n\r]", "").trim()+"&share="+share+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&time="+Common.GetCurrentDate()+"&action="+action+"&lang="+language;
			database.addRequest(request, encodedString);
			//context.startActivity(new Intent(context, EmployeeCode.class));
			Intent intent=new Intent(context, SuccessActivity.class);
      		intent.putExtra("actionstring", actionstring);
      		context.startActivity(intent);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		//}
        
        //Looper.loop();
	}

	public void xmldata(String html)
    {
		try
	  	{
	  		Document doc = Jsoup.parse(html);
	      	List<org.jsoup.nodes.Node> elements = doc.select("body").first().childNodes();
	      	String data = "";
	      	for (int i = 0; i < elements.size(); i++) {
					data=data+elements.get(i).toString();
				}
	      	String str=data.replaceAll("[\t\n\r]", "");
	      	str=str.trim();
	      	boolean check=str.equals("0");
	      	if(check)
	      	{
	      		Intent intent=new Intent(context, SuccessActivity.class);
	      		intent.putExtra("actionstring", actionstring);
	      		context.startActivity(intent);
	      		//context.startActivity(new Intent(context, EmployeeCode.class));
	      	}
	      	else
	      	{
	      		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	      		alertDialog.setTitle("Alert");
	      		alertDialog.setMessage(data);
	      		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int which) {
	            	  context.startActivity(new Intent(context, Action.class));
	              }
	      });
	      		alertDialog.show();
	      	}
	  	}
	  	catch(Exception e)
	  	{
	  		e.getMessage();
	  	}
    }	
}
