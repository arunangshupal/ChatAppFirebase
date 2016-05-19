package com.firebasedemo.arunangshupal.chatappfirebase.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.activity.HomeActivity;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;

import java.util.ArrayList;

/**
 * Created by Arunangshu Pal on 5/15/2016.
 */


public class ContactAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ContactBean> contactList;

    public ContactAdapter(Context mContext, ArrayList<ContactBean> contactList) {
        this.mContext = mContext;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder contactViewHolder;
        if(convertView==null){

            // inflate the layout
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.contact_block, parent, false);

            // well set up the ViewHolder
            contactViewHolder = new ContactViewHolder();
            contactViewHolder.txtUserName=(TextView)convertView.findViewById(R.id.user_name);
            contactViewHolder.txtUserNumber=(TextView)convertView.findViewById(R.id.phone_no) ;
            contactViewHolder.txtStatus=(TextView)convertView.findViewById(R.id.status) ;;

            // store the holder with the view.
            convertView.setTag(contactViewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            contactViewHolder = (ContactViewHolder) convertView.getTag();
        }
        ContactBean contactBean=contactList.get(position);
        contactViewHolder.txtUserName.setText(contactBean.getUserName());
        contactViewHolder.txtUserNumber.setText(contactBean.getPhoneNumber().toString());
        contactViewHolder.txtStatus.setText("Available");
        return convertView;
    }

    static class ContactViewHolder {
        TextView txtUserName,txtUserNumber,txtStatus;
    }
}
