package com.example.punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.GregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Java class to get users from server and save it in local database
 */

public class GetUsers extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database data;

    SharedPreferences settings;
    String companycode,url,Password,language;
    boolean first;
    ProgressDialog dialog;
    GPSTracker gps;
    Double lng,lat,altitude;
	
    GetUsers(Context context,Boolean first)
    {
    	this.context=context;
    	this.first=first;
    	settings = PreferenceManager.getDefaultSharedPreferences(context);
    	data=new Database(context);
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
        	//gps.showSettingsAlert();
  		}
    }
    
	@Override
    public void run() 
	{
		Looper.prepare();		
	 	companycode=settings.getString("companycode", "");
	 	url=settings.getString("url", "");
	 	Password=settings.getString("password", "");
        language=settings.getString("language", "");
        HttpClient httpClient = new DefaultHttpClient();
        //HttpGet httpPost = new HttpGet("http://207.34.244.247:88/erp/punch.nsf/(firstconnexion)?openagent&comp="+txtcompanycode.getText()+"&share=oui&time=2014-10-13T21:00:00-05:00&mac=%3Cadresse%20mac%3E&lat=latitude&lon=lontitude&alt=altitude&version=1.00&veri=1.00");
        HttpGet httpPost = new HttpGet("http://"+url+"/erp/punch.nsf/(getIDUsers)?openagent&comp="+companycode+"&password="+Password+"&time="+Common.GetCurrentDate()+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&version=1.00&veri=1.00");
        
        try {
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));            
            while ((body = rd.readLine()) != null) 
            {
            _data=_data+"\n"+body;
            }
            xmldata(_data);
            Log.d("Http Response:", _data);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        //Looper.loop();
	}
	
	public void xmldata(String html)
    {
		try
    	{
    		Document doc = Jsoup.parse(_data);
        	Elements elements = doc.select("body").first().children();

        	DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
        	 DocumentBuilder db = dbf.newDocumentBuilder();
        	 InputSource is = new InputSource();
        	 StringReader sr=new StringReader(elements.toString());
        	 is.setCharacterStream(sr);
        	 org.w3c.dom.Document xmldoc = db.parse(is);
        	 NodeList nodes = xmldoc.getElementsByTagName("user");
        	 data.dropUserTable();
        	 for (int temp = 0; temp < nodes.getLength(); temp++) {
        		 
        			Node nNode = nodes.item(temp);
        	 
        			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        	 
        				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
        				String fname=eElement.getAttribute("firstname");
        				String lname=eElement.getAttribute("lastname");
        				data.addUser(eElement.getTextContent().toString().replaceAll("(\\r|\\n)", "").trim(),fname,lname);
        			}
        	 }
        	 if(first)
             {
        		 data.dropRequestTable();
        		 Intent intent=new Intent(context, EmployeeCode.class);
        		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);
             }
        	 //scheduleAlarm();
    	}
    	catch(Exception e)
    	{
    		e.getMessage();
    	}
    }
	
	public void scheduleAlarm()
    {
            // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time, 
            // we fetch  the current time in milliseconds and added 1 day time
            // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day        
            Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;

            // create an Intent and set the class which will execute when Alarm triggers, here we have
            // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
            // alarm triggers and 
            //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
       
            // create the object
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //set the alarm for particular time
            alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            Toast.makeText(context, "Alarm Scheduled for Tommrrow", Toast.LENGTH_LONG).show();
         
    }
	
}