package com.firebasedemo.arunangshupal.chatappfirebase.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.activity.HomeActivity;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.service.MessageListener;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppUrl;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Register.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG=Register.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OnRegister mRegister;
    private HomeActivity homeActivity;
    private EditText phoneNumber,userName;
    private TextView txtWelcomeUser,txtPhoneNumber;
    private Button register,logoutBtn;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);

        homeActivity.setTitle("Let's Chat");

        final LinearLayout signUpForm=(LinearLayout)view.findViewById(R.id.reg_form) ;
        final LinearLayout userDetails=(LinearLayout)view.findViewById(R.id.user_details) ;
        final String userDatailsData=Utility.getSharePrefData(homeActivity,AppConstant.USER_DETAILS_KEY,null);
        if(userDatailsData==null) {
            userDetails.setVisibility(View.GONE);
            signUpForm.setVisibility(View.VISIBLE);
            phoneNumber = (EditText) view.findViewById(R.id.phone_no);
            userName = (EditText) view.findViewById(R.id.name);
        }else{
            userDetails.setVisibility(View.VISIBLE);
            signUpForm.setVisibility(View.GONE);
            ContactBean contactBean=new Gson().fromJson(userDatailsData,ContactBean.class);
            txtWelcomeUser=(TextView)view.findViewById(R.id.welcome_user);
            txtPhoneNumber=(TextView)view.findViewById(R.id.user_phone_number);
            txtWelcomeUser.setText("Welcome "+contactBean.getUserName());
            txtPhoneNumber.setText(contactBean.getPhoneNumber().toString());
            logoutBtn=(Button)view.findViewById(R.id.logout);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.setSharePrefData(homeActivity,AppConstant.USER_DETAILS_KEY,null);
                    userDetails.setVisibility(View.GONE);
                    signUpForm.setVisibility(View.VISIBLE);
                }
            });


        }

        register=(Button)view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(userDatailsData)) {
                    homeActivity.pushFragment(new HomeFragment());
                    ContactBean contactBean=new Gson().fromJson(userDatailsData,ContactBean.class);
                    homeActivity.registerListioner(contactBean.getPhoneNumber()+"");
                } else {
                    final String phoneNumberStr = phoneNumber.getText().toString();
                    Log.d("Phone no", phoneNumberStr);
                    final String userNameStr = userName.getText().toString();
                    Log.d("Name", userNameStr);
                    Firebase firebaseRef = new Firebase(AppUrl.FIREBASE_URL);
                    final ContactBean contactBean = new ContactBean(userNameStr, Long.parseLong(phoneNumberStr));
                    final String contactBeanStr = new Gson().toJson(contactBean);
                    firebaseRef.child("User").child(phoneNumberStr).setValue(contactBean, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            //if(firebaseError!=null) {
                            Utility.setSharePrefData(homeActivity, AppConstant.USER_DETAILS_KEY, contactBeanStr);
                            homeActivity.registerListioner(phoneNumberStr);
                            homeActivity.pushFragment(new HomeFragment());

                            //}


                        }
                    });
                }
            }
        });
        return  view;
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
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    private void setmRegister(OnRegister mRegister){
        this.mRegister=mRegister;

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
    public interface OnRegister{
        void startService(String phoneNo);
    }
}
