package com.firebasedemo.arunangshupal.chatappfirebase.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Arunangshu Pal on 5/18/2016.
 */
public class PostsDatabaseHelper extends SQLiteOpenHelper {

    private static  PostsDatabaseHelper sInstance;
    private Context mContext;

    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_MESSAGE = "message_table";
  //  private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String MESSAGE_ID = "msg_id";
    private static final String KEY_TO = "contact_to";
    private static final String KEY_FROM = "contact_from";
    private static final String KEY_MESSAGE= "send_message";
    private static final String KEY_TIME= "send_time";
    private static final String IS_DB_CREATED="IS_DB_CREATED";


    // ...

    public static synchronized PostsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Boolean isDbCreated=Boolean.getBoolean(Utility.getSharePrefData(mContext,IS_DB_CREATED,"false"));
        if(!isDbCreated) {
            String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE +
                    "(" +
                    MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TO + " NUMBER," +
                    KEY_FROM + " NUMBER," +
                    KEY_MESSAGE + " TEXT, " +
                    KEY_TIME + " NUMBER" +
                    ")";

            db.execSQL(CREATE_MESSAGE_TABLE);
            Utility.setSharePrefData(mContext, IS_DB_CREATED, "true");
        }
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
         //   db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

}
