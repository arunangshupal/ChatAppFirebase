package com.firebasedemo.arunangshupal.chatappfirebase.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.TableColumn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Arunangshu Pal on 5/14/2016.
 */
public class Utility {
    private static SharedPreferences getSharePref(Context mContext){
        return mContext.getSharedPreferences(AppConstant.SHARED_PREF_KEY,0);

    }

    public static void setSharePrefData(Context mContext,String key, String val){
        SharedPreferences preferences=getSharePref(mContext);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString(key,val);
        editor.apply();

    }

    public static String getSharePrefData(Context mContext,String key, String defaultVal){
        SharedPreferences preferences=getSharePref(mContext);
        return  preferences.getString(key,defaultVal);

    }

    public static HashSet<ContactBean> readPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        HashSet<ContactBean> contactList=new HashSet<>();
        if (contactsCount > 0)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext())
                    {
                        int phoneType 		= pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo 	= pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        if(phoneNo.length()<10){
                            continue;
                        }
                        Long  phoneNolng=getActualPhoneNo(phoneNo);
                        if(phoneNolng==null){
                            continue;
                        }
                        contactList.add(new ContactBean(contactName,phoneNolng));
                       /* switch (phoneType)
                        {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.e(contactName + ": TYPE_MOBILE", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.e(contactName + ": TYPE_HOME", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.e(contactName + ": TYPE_WORK", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                Log.e(contactName + ": TYPE_WORK_MOBILE", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.e(contactName + ": TYPE_OTHER", " " + phoneNo);
                                break;
                            default:
                                break;
                        }*/
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }

        return contactList;
    }

    private static Long getActualPhoneNo(String phooneNo) {
        try {
            phooneNo=phooneNo.replaceAll("[() +-]","");
            phooneNo=phooneNo.substring(phooneNo.length()-10,phooneNo.length());
            return Long.parseLong(phooneNo);
        }
        catch (Exception e){
            return null;
        }

    }

    private static void createTable(SQLiteDatabase dbRef, String tableName, List<TableColumn> columns){
       String query="CREATE TABLE IF NOT EXISTS "+tableName+"(";
        int i;
        for(i=0;i<columns.size()-1;i++){
            query+=columns.get(i).getColName()+" "+columns.get(i).getDataType()+",";
        }
        query+=columns.get(i).getColName()+" "+columns.get(i).getDataType()+");";
        dbRef.execSQL(query);
    }
}
