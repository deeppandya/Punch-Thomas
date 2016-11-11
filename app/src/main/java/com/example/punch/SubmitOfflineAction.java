package com.example.punch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

/**
 * Java class to submit the actiom in local database to send it later
 */

public class SubmitOfflineAction extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database database;
    SharedPreferences settings;
    ProgressDialog dialog;
    String companycode,url,Password,language,share,employee_id,action;
    GPSTracker gps;
    Double lng,lat,altitude;
    String imagedata=null;
    String requestid="";
	
    SubmitOfflineAction(Context context,String action,String data,String id)
    {
    	this.context=context;
    	this.action=action;
    	imagedata=data;
    	requestid=id;
    	database=new Database(context);
    }
    
	@Override
    public void run() 
	{
		Looper.prepare();
        HttpClient httpClient = new DefaultHttpClient();
			try {
				
			HttpPost httpPost = new HttpPost(action);
			List<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
			namevaluepair.add(new BasicNameValuePair("file", imagedata));
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
	      		/*Intent intent=new Intent(context, SuccessActivity.class);
	      		intent.putExtra("action", action);
	      		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	      		context.startActivity(intent);*/
	      		database.DeleteRequest(requestid);
	      	}
	  	}
	  	catch(Exception e)
	  	{
	  		e.getMessage();
	  	}
    }	
}
