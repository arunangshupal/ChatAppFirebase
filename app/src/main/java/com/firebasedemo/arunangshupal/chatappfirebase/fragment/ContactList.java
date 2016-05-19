package com.firebasedemo.arunangshupal.chatappfirebase.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebasedemo.arunangshupal.chatappfirebase.R;
import com.firebasedemo.arunangshupal.chatappfirebase.activity.HomeActivity;
import com.firebasedemo.arunangshupal.chatappfirebase.adapter.ContactAdapter;
import com.firebasedemo.arunangshupal.chatappfirebase.bean.ContactBean;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AllCurdData;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppConstant;
import com.firebasedemo.arunangshupal.chatappfirebase.util.AppUrl;
import com.firebasedemo.arunangshupal.chatappfirebase.util.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private HomeActivity homeActivity;
    private  static  final  String TAG=ContactList.class.getSimpleName();
    ArrayList<ContactBean> contactToDisplayList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView contactList;
    AllCurdData allCurdData;
    public ContactList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactList.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactList newInstance(String param1, String param2) {
        ContactList fragment = new ContactList();
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
        final View view= inflater.inflate(R.layout.fragment_contact_list, container, false);
        contactList=(ListView)view.findViewById(R.id.contact_list);
        allCurdData=AllCurdData.getInstance();
        if(allCurdData.getContacts().size()==0){
            getContactListFromServer();
        }
        else{
            contactToDisplayList=allCurdData.getContacts();
            displayContactList();
        }



        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatFragment chatFragment=new ChatFragment();
                Bundle bundle=new Bundle();
                String contactStr=new Gson().toJson(contactToDisplayList.get(position));
                bundle.putString(AppConstant.CONTACT_BEAN_KEY,contactStr);
                chatFragment.setArguments(bundle);
                homeActivity.pushFragment(chatFragment);
            }
        });



        return view;
    }

    private void getContactListFromServer(){
        Firebase firebaseRef=homeActivity.getFirebaseRef().child(AppUrl.USER);
        final HashSet<ContactBean> fireBaseContactList=new HashSet<>();
        final HashSet<ContactBean> contactToDisplay=new HashSet<>();
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    // Log.d(TAG+"_",ds.getValue().toString());
                    Log.e(TAG,ds.getValue().toString());
                    ContactBean bean=  ds.getValue(ContactBean.class);
                    fireBaseContactList.add(bean);

                }
                HashSet<ContactBean> phoneContact=Utility.readPhoneContacts(getContext());
                for(ContactBean cb:phoneContact){
                    if(!fireBaseContactList.add(cb)){
                        contactToDisplay.add(cb);
                    }

                }
                contactToDisplayList=new ArrayList<>();
                contactToDisplayList.addAll(contactToDisplay);
                allCurdData.setContacts(contactToDisplayList);
                displayContactList();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private  void displayContactList(){
        ContactAdapter contactAdapter=new ContactAdapter(getContext(),contactToDisplayList );

        contactList.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
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
           /* throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
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
