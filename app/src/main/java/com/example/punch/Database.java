package com.example.punch;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Java class to save offline information locally
 */

public class Database extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Users";

	// Contacts table name
	private static final String TABLE_USERS = "user";
	private static final String TABLE_REQUESTS = "requests";
	private static final String TABLE_COMPANY_IMAGE = "company_image";

	// Contacts Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_USER = "user";
	public static final String KEY_FIRSTNAME = "firstname";
	public static final String KEY_LASTNAME = "lastname";
	
	//Table columns for requests
	public static final String KEY_REQUEST = "user";
	public static final String KEY_DATA = "firstname";
	
	//Table columns for company_image
	public static final String KEY_COMPANY_IMAGE = "image_data";
	
	
	// TRIPS table create statement
	private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_USER + " VARCHAR(100), " + KEY_FIRSTNAME + " VARCHAR(100),"+ KEY_LASTNAME + " VARCHAR(100))";
	private static final String CREATE_TABLE_REQUESTS = "CREATE TABLE " + TABLE_REQUESTS + " ( " + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_REQUEST + " VARCHAR(10000), " + KEY_DATA + " VARCHAR(10000))";
	private static final String CREATE_TABLE_COMPANY_IMAGE = "CREATE TABLE " + TABLE_COMPANY_IMAGE + " ( " + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_COMPANY_IMAGE + " BLOB)";
	
	
	public Database(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//db.execSQL(CREATE_TABLE_TRIPS);
		//db.execSQL(CREATE_TABLE_CITIES);
		//db.execSQL(CREATE_TABLE_SETTINGS);
		//db.execSQL("INSERT INTO " + TABLE_SETTINGS + " (twenty_four_hour_clock, weather_update, temperature_units, distance_units, alert_status, colors) VALUES('1', '0', 'C', 'K', 'ALL', '');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}
	
	public void dropUserTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL(CREATE_TABLE_USERS);
		//db.close();
	}
	
	public void dropRequestTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
		db.execSQL(CREATE_TABLE_REQUESTS);
		//db.close();
	}
	
	public void dropCompanyImageTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_IMAGE);
		db.execSQL(CREATE_TABLE_COMPANY_IMAGE);
	}
	
	public void createTables()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			db.execSQL(CREATE_TABLE_USERS);
			db.execSQL(CREATE_TABLE_REQUESTS);
			db.execSQL(CREATE_TABLE_COMPANY_IMAGE);
		}
		catch(Exception ex)
		{
			Log.i("Createtable", ex.getMessage());
		}
	}	
	
	/*public void addUser(String user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER, user);
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}*/
	
	void addUser(String user,String Firstname,String Lastname) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, id);
		values.put(KEY_USER, user);
		values.put(KEY_FIRSTNAME, Firstname);
		values.put(KEY_LASTNAME, Lastname);

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		//db.close(); // Closing database connection
	}
	
	public ArrayList<HashMap<String,String>> getAllUsers() {
		
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String,String> user = new HashMap<String,String>();
				user.put(KEY_ID, cursor.getString(0));
				user.put(KEY_USER, cursor.getString(1));
				user.put(KEY_FIRSTNAME, cursor.getString(2));
				user.put(KEY_LASTNAME, cursor.getString(3));
				list.add(user);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return contact list
		return list;
	}

	// Getting contacts Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	public boolean IsUserExists(String User) {
		String countQuery = "SELECT * FROM " + TABLE_USERS+" WHERE "+KEY_USER+"='"+User+"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor.getCount()>0)
		{
			cursor.close();
			return true;
		}
		else
		{
			cursor.close();
			return false;
		}
		
	}
	
	public HashMap<String, String> GetUserById(String User) {
		HashMap<String,String> user = null;
		if(IsUserExists(User))
		{
			String countQuery = "SELECT * FROM " + TABLE_USERS+" WHERE "+KEY_USER+"='"+User+"'";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			if (cursor.moveToFirst()) {
				do {
					user = new HashMap<String,String>();
					user.put(KEY_ID, cursor.getString(0));
					user.put(KEY_USER, cursor.getString(1));
					user.put(KEY_FIRSTNAME, cursor.getString(2));
					user.put(KEY_LASTNAME, cursor.getString(3));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return user;
	}
	
	void addRequest(String Request,String Data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, id);
		values.put(KEY_REQUEST, Request);
		values.put(KEY_DATA, Data);

		// Inserting Row
		db.insert(TABLE_REQUESTS, null, values);
		//db.close(); // Closing database connection
	}
	
	public ArrayList<HashMap<String,String>> getAllRequests() {
		
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_REQUESTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String,String> request = new HashMap<String,String>();
				request.put(KEY_ID, cursor.getString(0));
				request.put(KEY_REQUEST, cursor.getString(1));
				request.put(KEY_DATA, cursor.getString(2));
				list.add(request);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return contact list
		return list;
	}
	
public void DeleteRequest(String key) {
	
		String selectQuery = "DELETE FROM " + TABLE_REQUESTS+" WHERE "+KEY_ID+"='"+key+"'";
		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(selectQuery);
		}
		catch(Exception ex)
		{
			ex.getMessage();
		}

	}
	
	void addCompanyImage(byte[] data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.put(KEY_COMPANY_IMAGE, data);

		db.insert(TABLE_COMPANY_IMAGE, null, values);
		
		//db.close(); // Closing database connection
	}
	
	public byte[] getCompanyImage() {
		
		byte[] imagedata=null;
		SQLiteDatabase db = this.getWritableDatabase();
		//Cursor cursor = db.rawQuery(selectQuery, null);
		Cursor cursor=db.query(TABLE_COMPANY_IMAGE, null, null, null, null, null, null, null);

		try
		{
// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					imagedata=cursor.getBlob(1);
				} while (cursor.moveToNext());
			}
		}
		catch (Exception ex)
		{
			Log.e("getBlob",ex.getMessage());
		}
		cursor.close();
		// return contact list
		return imagedata;
	}
	
}
