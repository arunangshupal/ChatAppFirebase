package com.firebasedemo.arunangshupal.chatappfirebase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;
import com.google.gson.Gson;

/**
 * Created by Arunangshu Pal on 5/17/2016.
 */
public class MessageBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG=MessageBroadcastReceiver.class.getSimpleName();
    public static final String PROCESS_RESPONSE = "com.firebasedemo.arunangshupal.chatappfirebaase.PROCESS_RESPONSE";
    private OnMessageReceived onMessageReceived;
    @Override
    public void onReceive(Context context, Intent intent) {
        String messageJson=intent.getStringExtra("RESPONSE_MESSAGE");
        Log.d(TAG,messageJson);
        onMessageReceived.onMessaageReceivedCallback(new Gson().fromJson(messageJson,MessageBean.class));
    }

    public void setOnMessageReceived(OnMessageReceived onMessageReceived){
        this.onMessageReceived=onMessageReceived;

    }
    public interface OnMessageReceived{
        public  void onMessaageReceivedCallback(MessageBean messageBean);
    }


}
