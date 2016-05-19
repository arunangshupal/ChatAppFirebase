package com.firebasedemo.arunangshupal.chatappfirebase.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arunangshu Pal on 5/18/2016.
 */
public class MessageCurd {

    Context mContext;
    PostsDatabaseHelper helper;
    private static String TAG=MessageCurd.class.getSimpleName();

    private static final String TABLE_MESSAGE = "message_table";
    //  private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String MESSAGE_ID = "msg_id";
    private static final String KEY_TO = "contact_to";
    private static final String KEY_FROM = "contact_from";
    private static final String KEY_MESSAGE= "send_message";
    private static final String KEY_TIME= "send_time";

    public MessageCurd(Context mContext){
        this.mContext=mContext;
        helper=PostsDatabaseHelper.getInstance(mContext);
    }



    public long addMessage(MessageBean messageBean) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = helper.getWritableDatabase();
        long messageId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FROM, messageBean.getFromPhoneNumber());
            values.put(KEY_TO, messageBean.getToPhoneNumber());
            values.put(KEY_MESSAGE, messageBean.getMessage());
            values.put(KEY_TIME,messageBean.getMessage());



                // user with this userName did not already exist, so insert new user
                messageId = db.insertOrThrow(TABLE_MESSAGE, null, values);
                db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return messageId;
    }

    public List<MessageBean> getAllMessages() {
        List<MessageBean> messages = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_MESSAGE
                        );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setToPhoneNumber(cursor.getLong(cursor.getColumnIndex(KEY_TO)));
                    messageBean.setFromPhoneNumber(cursor.getLong(cursor.getColumnIndex(KEY_FROM)));
                    messageBean.setSysTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));
                    messageBean.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));

                    messages.add(messageBean);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return messages;
    }

    public ArrayList<MessageBean> getAllMessagesForSpecificPhoneNumber(Long phoneNo) {
        ArrayList<MessageBean> messages = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s=%s OR %s=%s",
                        TABLE_MESSAGE,
                        KEY_TO,
                        phoneNo,
                        KEY_FROM,
                        phoneNo
                );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setToPhoneNumber(cursor.getLong(cursor.getColumnIndex(KEY_TO)));
                    messageBean.setFromPhoneNumber(cursor.getLong(cursor.getColumnIndex(KEY_FROM)));
                    messageBean.setSysTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));
                    messageBean.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));

                    messages.add(messageBean);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return messages;
    }
}