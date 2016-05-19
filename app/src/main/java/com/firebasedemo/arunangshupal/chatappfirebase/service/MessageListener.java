package com.firebasedemo.arunangshupal.chatappfirebase.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;
import com.firebasedemo.arunangshupal.chatappfirebase.activity.HomeActivity;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;
import com.firebasedemo.arunangshupal.chatappfirebase.receiver.MessageBroadcastReceiver;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppUrl;
import com.firebasedemo.arunangshupal.chatappfirebase.util.ForegroundCheckTask;
import com.firebasedemo.arunangshupal.chatappfirebase.util.MessageCurd;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by Arunangshu Pal on 5/16/2016.
 */

public class MessageListener extends IntentService {
    Firebase firebaseRef;
    private static String TAG=MessageListener.class.getSimpleName();
    private static String phoneNumber;



    public MessageListener() {
        super(MessageListener.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //ContactBean contactBean=new Gson().fromJson(Utility.getSharePrefData(this, AppConstant.USER_DETAILS_KEY,""),ContactBean.class);

        phoneNumber=intent.getStringExtra("PHONE_NO");
        Log.d(TAG,"Inside Listner "+phoneNumber);
        Firebase.setAndroidContext(this);
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final Intent mIntent=intent;

        firebaseRef=new Firebase(AppUrl.FIREBASE_URL).child(AppUrl.MESSAGE_INBOX).child(phoneNumber);
        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,dataSnapshot.toString());

                MessageBean messageBean=dataSnapshot.getValue(MessageBean.class);
                new MessageCurd(getApplicationContext()).addMessage(messageBean);
                Bundle bundle=new Bundle();
                bundle.putString("result", new Gson().toJson(messageBean));
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MessageBroadcastReceiver.PROCESS_RESPONSE);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                //  broadcastIntent.putExtra(RESPONSE_STRING, responseString);
                broadcastIntent.putExtra("RESPONSE_MESSAGE", new Gson().toJson(messageBean));
                sendBroadcast(broadcastIntent);
                // Use like this:
                boolean foregroud=false ;
                try {
                    foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(!foregroud) {
                    sendNotification(messageBean);
                }

                firebaseRef.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void sendNotification(MessageBean message) {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("MESSAGE",new Gson().toJson(message));
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] pattern= {3000};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setVibrate(pattern)
                .setTicker("Hii User")

                .setContentTitle(message.getFromPhoneNumber()+"")
                .setContentText(message.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setNumber(7);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
