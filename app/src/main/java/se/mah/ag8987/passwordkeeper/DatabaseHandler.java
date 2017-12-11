package se.mah.ag8987.passwordkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import se.mah.ag8987.passwordkeeper.Model.Application;

/**
 * Created by LinusHakansson on 2017-09-07.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String DB_NAME ="economymanager.db";
    private static final int DB_VERSION = 2;
    private static final String TAG ="DATABASEHANDLER";
    // INCOME TABLE
    private static final String PASSWORD_TABLE = "passwords";
    private static final String PASSWORD_COLUMN_ID = "_id";
    private static final String PASSWORD_COLUMN_KEY = "_key";
    private static final String PASSWORD_COLUMN_TITLE = "_title";
    private static final String PASSWORD_COLUMN_PASSWORD = "_password";




// CREATE INCOME TABLE
    public static final String CREATE_TABLE_PASSWORD = "create table " + PASSWORD_TABLE + "("
            + PASSWORD_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PASSWORD_COLUMN_KEY + " TEXT,"
            + PASSWORD_COLUMN_TITLE +  " TEXT,"
            + PASSWORD_COLUMN_PASSWORD + " TEXT" + ");";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PASSWORD);
        Log.i(TAG, "OnCreate: " + "SKAPAT databas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Upgrading");
        db.execSQL(" DROP TABLE IF EXISTS " + PASSWORD_TABLE);
        onCreate(db);
    }

    public boolean addPassword( Application application){
        Log.d(TAG, "addPassword: AddedPassword--------");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PASSWORD_COLUMN_KEY, application.getKey());
        values.put(PASSWORD_COLUMN_TITLE, application.getName());
        values.put(PASSWORD_COLUMN_PASSWORD, application.getPassword());


        long id = db.insert(PASSWORD_TABLE, null, values);

        if(id == -1){
            return false;
        }
        db.close();
        return true;
    }


    public List<Application> getApplications(){
        List<Application> appList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + PASSWORD_TABLE + ";";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            Log.d(TAG, "getAllIncomes: " + cursor.getCount());
            while (!cursor.isAfterLast()){
                String name = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN_TITLE));
                String password  = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN_PASSWORD));
                String key = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN_KEY));


                appList.add(new Application(name, password, key));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        Log.d(TAG, "getAllIncomes end of method: " + appList.size());
        return appList;
    }

    public boolean deletePassword(Application a){
        SQLiteDatabase dbase = this.getWritableDatabase();
//        if(dbase.delete(PASSWORD_TABLE, PASSWORD_COLUMN_TITLE  + " = "   + "'" +  a.getName() + "'" + " and " + PASSWORD_COLUMN_PASSWORD + " = " + "'" + a.getPassword() + "'", null) >0){

            if(dbase.delete(PASSWORD_TABLE, PASSWORD_COLUMN_TITLE  + " = "   + "'" +  a.getName() + "'" , null) >0){
            Log.d(TAG, "deletePassword: Deleted");
            return true;
        }
        Log.d(TAG, "deletePassword: Something went wrong");
        return false;
    }

    public boolean updatePassword(Application a){
        SQLiteDatabase dbase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PASSWORD_COLUMN_PASSWORD, a.getPassword());
        values.put(PASSWORD_COLUMN_KEY, a.getKey());

//        return dbase.update(PASSWORD_TABLE, values, PASSWORD_COLUMN_PASSWORD + " = " +  "'" +  a.getPassword() +  "'"  +" and " + PASSWORD_COLUMN_TITLE + " = " +   "'" + a.getName() +  "'" , null ) > 0;

        return dbase.update(PASSWORD_TABLE, values, PASSWORD_COLUMN_TITLE + " = " +  "'" +  a.getName() +  "'" , null ) > 0;

    }
}
