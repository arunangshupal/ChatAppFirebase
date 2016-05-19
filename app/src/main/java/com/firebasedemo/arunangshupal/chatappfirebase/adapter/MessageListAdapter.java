package com.firebasedemo.arunangshupal.chatappfirebase.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Arunangshu Pal on 5/16/2016.
 */
public class MessageListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<MessageBean> messageList;
    ContactBean myContact;
    public static final int FRIEND_MESSAGE_TYPE = 0;
    public static final int MY_MESSAGE_TYPE = 1;

    public MessageListAdapter(Context mContext, ArrayList<MessageBean> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
        myContact=new Gson().fromJson(Utility.getSharePrefData(mContext, AppConstant.USER_DETAILS_KEY,""),ContactBean.class);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getFromPhoneNumber().equals(myContact.getPhoneNumber())){
            return MY_MESSAGE_TYPE;
        }else{
            return FRIEND_MESSAGE_TYPE;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyMessageViewHolder myMessageViewHolder;
        FriendMessageViewHolder friendMessageViewHolder;
        MessageBean msg=messageList.get(position);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        if(msg.getFromPhoneNumber().equals(myContact.getPhoneNumber())) {
        //    if (convertView == null) {
                convertView = inflater.inflate(R.layout.own_message, parent, false);
                myMessageViewHolder = new MyMessageViewHolder();
                myMessageViewHolder.txtUserName = (TextView) convertView.findViewById(R.id.user_name);
                myMessageViewHolder.txtMessage = (TextView) convertView.findViewById(R.id.message);
                myMessageViewHolder.txtTime = (TextView) convertView.findViewById(R.id.msg_time);
                convertView.setTag(myMessageViewHolder);
            /*} else {
                myMessageViewHolder=(MyMessageViewHolder) convertView.getTag();

            }*/
            myMessageViewHolder.txtUserName.setText(msg.getFromPhoneNumber().toString());
            myMessageViewHolder.txtMessage.setText(msg.getMessage());
            myMessageViewHolder.txtTime.setText(msg.getSysTime().toString());
        }
        else{
          //  if (convertView == null) {
                convertView = inflater.inflate(R.layout.friend_message, parent, false);
                friendMessageViewHolder=new FriendMessageViewHolder();
                friendMessageViewHolder.txtUserName= (TextView) convertView.findViewById(R.id.user_name);
                friendMessageViewHolder.txtMessage= (TextView) convertView.findViewById(R.id.message);
                friendMessageViewHolder.txtTime= (TextView) convertView.findViewById(R.id.msg_time);
                convertView.setTag(friendMessageViewHolder);
            /*}
            else{
                friendMessageViewHolder=(FriendMessageViewHolder) convertView.getTag();

            }*/
            friendMessageViewHolder.txtUserName.setText(msg.getFromPhoneNumber().toString());
            friendMessageViewHolder.txtMessage.setText(msg.getMessage());
            friendMessageViewHolder.txtTime.setText(msg.getSysTime().toString());


        }
        return convertView;
    }

    static class MyMessageViewHolder {
        TextView txtUserName, txtMessage, txtTime;

    }
    static class FriendMessageViewHolder {
        TextView txtUserName,txtMessage,txtTime;
    }
}
