package com.firebasedemo.arunangshupal.chatappfirebase.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;
import com.firebasedemo.arunangshupal.chatappfirebase.fragment.ChatFragment;
import com.firebasedemo.arunangshupal.chatappfirebase.fragment.Register;
import com.firebasedemo.arunangshupal.chatappfirebase.receiver.MessageBroadcastReceiver;
import com.firebasedemo.arunangshupal.chatappfirebase.receiver.MessageReceiver;
import com.firebasedemo.arunangshupal.chatappfirebase.service.MessageListener;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AllCurdData;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppUrl;
import com.firebasedemo.arunangshupal.chatappfirebase.util.MessageCurd;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class HomeActivity extends AppCompatActivity implements
        MessageReceiver.Receiver,Register.OnRegister
{

    private Stack<Fragment> fragmentStack;
    Firebase firebaseRef;
    MessageReceiver messageReceiver;
    MessageBroadcastReceiver receiver;
    AllCurdData allCurdData;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        allCurdData=AllCurdData.getInstance();

        IntentFilter filter = new IntentFilter(MessageBroadcastReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MessageBroadcastReceiver();

        registerReceiver(receiver, filter);

        receiver.setOnMessageReceived(new MessageBroadcastReceiver.OnMessageReceived() {
            @Override
            public void onMessaageReceivedCallback(MessageBean messageBean) {
                Log.d(TAG,messageBean.getMessage());
                ArrayList<MessageBean> messages=  getMessagesFromAllCurdDataContactMessagesHashMap(messageBean.getFromPhoneNumber());
                messages.add(messageBean);
                if(onMessageReceive!=null) {
                    onMessageReceive.onMessageReceivedCallBack(messageBean);
                }
            }
        });


        fragmentStack=new Stack<>();

        Firebase.setAndroidContext(this);
        firebaseRef=new Firebase(AppUrl.FIREBASE_URL);
        String userDetails=Utility.getSharePrefData(this, AppConstant.USER_DETAILS_KEY,null);
        String message=null;
        Intent intent=getIntent();
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                message=bundle.getString("MESSAGE");
            }
        }

      //  Utility.readPhoneContacts(this);
        Fragment fragment;
        if(message!=null && userDetails!=null){
            Bundle bundle=new Bundle();
            bundle.putString("MESSAGE",message);
            bundle.putString(AppConstant.USER_DETAILS_KEY,userDetails);
            fragment= new ChatFragment();
            fragment.setArguments(bundle);

        }
        else{
           fragment=new Register();
        }


        pushFragment(fragment);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//      //  setSupportActionBar(toolbar);
//        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Suleiman Ali Shakir");
//        ImageView header = (ImageView) findViewById(R.id.header);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.lc);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//               int  mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
//                collapsingToolbar.setContentScrimColor(mutedColor);
//            }
//        });


    }



    public Firebase getFirebaseRef() {
        return firebaseRef;
    }
    public void registerListioner(String phoneNo){
        messageReceiver=new MessageReceiver(new Handler());
        messageReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, MessageListener.class);
        intent.putExtra("PHONE_NO",phoneNo);
        startService(intent);
    }

    public  void pushFragment(Fragment fragment){
        fragmentStack.push(fragment);
        replaceFragment(fragment);
    }

    public void popFragment(){
        fragmentStack.pop();
        Fragment fragment=fragmentStack.peek();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        try{
        popFragment();
        }
        catch (Exception e){
            finish();
           // e.printStackTrace();
        }

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG,"orr "+resultData.getString("result"));
    }

    @Override
    public void startService(String phoneNo) {
        Log.d(TAG,"Inside startServicImpl");
        registerListioner(phoneNo);
    }
    private static String TAG="HomeActivity";

    private OnMessageReceive onMessageReceive;
   public  void setOnMessageReceived(OnMessageReceive onMessageReceived){
       this.onMessageReceive=onMessageReceived;

   }
    public  interface  OnMessageReceive{
        public void onMessageReceivedCallBack(MessageBean messageBean);
    }

    public  ArrayList<MessageBean> getMessagesFromAllCurdDataContactMessagesHashMap(Long phone){
        ArrayList<MessageBean>  messageBeanArrayList;
        HashMap<Long,ArrayList<MessageBean>> phoneMessagesList=allCurdData.getMessageListPhoneNumberHashmap();
        messageBeanArrayList=phoneMessagesList.get(phone);
        if(messageBeanArrayList==null){
            messageBeanArrayList=new MessageCurd(this).getAllMessagesForSpecificPhoneNumber(phone);
            phoneMessagesList.put(phone,messageBeanArrayList);
        }
        return  messageBeanArrayList;
    }

}
