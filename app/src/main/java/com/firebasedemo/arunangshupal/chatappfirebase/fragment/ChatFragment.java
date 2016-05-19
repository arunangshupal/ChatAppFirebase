package com.firebasedemo.arunangshupal.chatappfirebase.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.activity.HomeActivity;
import com.firebasedemo.arunangshupal.chatappfirebase.adapter.MessageListAdapter;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.MessageBean;
import com.firebasedemo.arunangshupal.chatappfirebase.receiver.MessageReceiver;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppUrl;
import com.firebasedemo.arunangshupal.chatappfirebase.util.MessageCurd;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ContactBean contactBean,myContact;
    private HomeActivity homeActivity;
    private OnFragmentInteractionListener mListener;
    private ArrayList<MessageBean> messageList;
    MessageListAdapter messageListAdapter;
    MessageBean messageBean;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle=getArguments();
        Gson gson=new Gson();
        contactBean=gson.fromJson(bundle.getString(AppConstant.CONTACT_BEAN_KEY),ContactBean.class);
        if(contactBean==null){
            messageBean=gson.fromJson(bundle.getString("MESSAGE"),MessageBean.class);
            contactBean=new ContactBean(messageBean.getFromPhoneNumber().toString(),messageBean.getFromPhoneNumber());
        }
        myContact=gson.fromJson(Utility.getSharePrefData(getContext(),AppConstant.USER_DETAILS_KEY,""),ContactBean.class);
        messageList=homeActivity.getMessagesFromAllCurdDataContactMessagesHashMap(contactBean.getPhoneNumber());
        if(messageBean!=null){
            messageList.add(messageBean);
            messageBean=null;
        }
        messageListAdapter=new MessageListAdapter(homeActivity,messageList);
        homeActivity.setOnMessageReceived(new HomeActivity.OnMessageReceive() {
            @Override
            public void onMessageReceivedCallBack(MessageBean messageBean) {
               /* if(messageBean.getFromPhoneNumber().equals())
                messageList.add(messageBean);*/
                messageListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        homeActivity.setTitle(contactBean.getUserName());
        ListView chatList=(ListView)view.findViewById(R.id.message_chat);

        chatList.setAdapter(messageListAdapter);
        final EditText msgTxt=(EditText)view.findViewById(R.id.message_text);
        ImageButton send=(ImageButton) view.findViewById(R.id.send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msgTxt.getText().toString())) {
                    MessageBean msg = new MessageBean();
                    msg.setFromPhoneNumber(myContact.getPhoneNumber());
                    msg.setToPhoneNumber(contactBean.getPhoneNumber());
                    msg.setMessage(msgTxt.getText().toString());
                    msg.setSysTime(System.currentTimeMillis());
                    messageList.add(msg);
                    messageListAdapter.notifyDataSetChanged();
                    Firebase firebaseRef = homeActivity.getFirebaseRef().child(AppUrl.MESSAGE_INBOX).child(msg.getToPhoneNumber().toString());
                    firebaseRef.push().setValue(msg);
                    new MessageCurd(homeActivity).addMessage(messageBean);
                    msgTxt.setText("");
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity=(HomeActivity)context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
