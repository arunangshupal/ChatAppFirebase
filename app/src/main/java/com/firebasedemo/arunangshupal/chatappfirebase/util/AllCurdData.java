package com.firebasedemo.arunangshupal.chatappfirebase.util;

import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arunangshu Pal on 5/18/2016.
 */
public class AllCurdData {
    private static  AllCurdData allCurdData;
    private HashMap<Long,ContactBean> contactBeanHashMap;
    private HashMap<Long,ArrayList<MessageBean>> messageListPhoneNumberHashmap;
    private ArrayList<ContactBean> contacts;
    private  AllCurdData(){
        contactBeanHashMap=new HashMap<>();
        messageListPhoneNumberHashmap=new HashMap<>();
        contacts=new ArrayList<>();
    }
    public static AllCurdData getInstance(){
        if(allCurdData==null){
            allCurdData=new AllCurdData();
        }
        return  allCurdData;
    }

    public HashMap<Long, ContactBean> getContactBeanHashMap() {
        return contactBeanHashMap;
    }

    public void setContactBeanHashMap(HashMap<Long, ContactBean> contactBeanHashMap) {
        this.contactBeanHashMap = contactBeanHashMap;
    }

    public HashMap<Long, ArrayList<MessageBean>> getMessageListPhoneNumberHashmap() {
        return messageListPhoneNumberHashmap;
    }

    public void setMessageListPhoneNumberHashmap(HashMap<Long, ArrayList<MessageBean>> messageListPhoneNumberHashmap) {
        this.messageListPhoneNumberHashmap = messageListPhoneNumberHashmap;
    }

    public void setMessageListforPhoneNumber(Long phone,ArrayList<MessageBean> messages){
        if(messageListPhoneNumberHashmap!=null) {
            messageListPhoneNumberHashmap.put(phone, messages);
        }
    }

    public ArrayList<ContactBean> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactBean> contacts) {
        this.contacts = contacts;
        initContactHashMap();
    }

    private void initContactHashMap() {
        for(ContactBean cb:contacts){
            contactBeanHashMap.put(cb.getPhoneNumber(),cb);
        }
    }


}
