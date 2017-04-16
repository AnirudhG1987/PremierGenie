package com.premiergenie.pgbbay.FeeDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.premiergenie.pgbbay.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FeeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<FeeDetailsClass> mfeeDetailsList;

    private FirebaseRecyclerAdapter<FeeDetailsClass, FeeFragment.FeeDetailsHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    private ProgressBar spinner;
    private OnDataUpdatedListener mCallback;

    private String caller;

    private Double expFeeCounter=0.0;

    public FeeFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnDataUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        mfeeDetailsList = new ArrayList<>();

        String sName = "", date = "";
        if(getArguments()!=null) {
            sName = getArguments().getString("sName");
            date = getArguments().getString("date");
        }

         mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("feeDetails");


        spinner=(ProgressBar)rootview.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);



        Query latestAttendance;
        if(sName!=null) {
            latestAttendance = mfiredatabaseRef.orderByChild("studentName").equalTo(sName);
        }
        else {
            latestAttendance = mfiredatabaseRef.orderByChild("datePaid");
        }



        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeeDetailsClass feeDetailsClass = dataSnapshot.getValue(FeeDetailsClass.class);
                feeDetailsClass.setKey(dataSnapshot.getKey());

                // Need attendance only for this year.
                int currYear = Calendar.getInstance().get(Calendar.YEAR);
                if(feeDetailsClass.getDatePaid().contains(""+currYear)) {

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                        String dateInString = feeDetailsClass.getDatePaid();

                        Date date = formatter.parse(dateInString);
                        feeDetailsClass.setDatePaid(formatter.format(date));
                        mfiredatabaseRef.child(feeDetailsClass.getKey()).setValue(feeDetailsClass);
                        //System.out.println(formatter.format(date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mfeeDetailsList.add(0,feeDetailsClass);
                    expFeeCounter += feeDetailsClass.getAmountPaid();
                    mCallback.onDataUpdated(expFeeCounter);
                }
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FeeDetailsClass feeDetailsClass = dataSnapshot.getValue(FeeDetailsClass.class);
                feeDetailsClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mfeeDetailsList.size();i++){
                    FeeDetailsClass f = mfeeDetailsList.get(i);
                    if(f.getKey() != null && f.getKey().contains(feeDetailsClass.getKey()))
                    {
                        break;
                    }
                }
                mfeeDetailsList.set(i,feeDetailsClass);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FeeDetailsClass feeDetailsClass = dataSnapshot.getValue(FeeDetailsClass.class);
                feeDetailsClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mfeeDetailsList.size();i++){
                    FeeDetailsClass f = mfeeDetailsList.get(i);
                    if(f.getKey() != null && f.getKey().contains(feeDetailsClass.getKey()))
                    {
                        break;
                    }
                }
                mfeeDetailsList.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FeeDetailsEditorActivity.class);
                intent.putExtra("caller",caller);
                startActivity(intent);
            }

        });

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        if(adapter!=null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();

        return rootview;

    }




    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<FeeDetailsClass, FeeFragment.FeeDetailsHolder>(
                FeeDetailsClass.class, R.layout.activity_recycler_item, FeeFragment.FeeDetailsHolder.class, mfiredatabaseRef) {

            @Override
            public FeeFragment.FeeDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_recycler_item, parent, false);
                return new FeeFragment.FeeDetailsHolder(itemView);
            }

            @Override
            protected void populateViewHolder(FeeFragment.FeeDetailsHolder v, FeeDetailsClass model, int position) {

                v.fcour.setText(model.getCourseName());
                v.famountPaid.setText(model.getAmountPaid());
                v.fdatePaid.setText(model.getDatePaid());
                v.fname.setText(model.getStudentName());

            }

            @Override
            public void onBindViewHolder(FeeFragment.FeeDetailsHolder holder, int position) {
                FeeDetailsClass itemFeeDetails = mfeeDetailsList.get(position);
                holder.bindAttendance(itemFeeDetails);
            }

            @Override
            public int getItemCount() {
                return mfeeDetailsList.size();
            }

        };
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null) {
            adapter.cleanup();
        }
    }


    public interface OnDataUpdatedListener{
        void onDataUpdated(double d);
    }


    public static class FeeDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView fname;
        public TextView fcour;
        public TextView fmonthGiven;
        public TextView fdatePaid;
        public TextView famountPaid;

        private FeeDetailsClass mfeeDetails;

        public FeeDetailsHolder(View v) {
            super(v);

            fname = (TextView) v.findViewById(R.id.text2);
            fdatePaid = (TextView) v.findViewById(R.id.text1);
            fcour = (TextView) v.findViewById(R.id.text3);
            famountPaid = (TextView) v.findViewById(R.id.text4);

            v.setOnClickListener(this);
        }

        public void bindAttendance(FeeDetailsClass feeDetailsClass) {

            mfeeDetails = feeDetailsClass;
            fcour.setText(mfeeDetails.getCourseName());
            famountPaid.setText(String.format("%d",mfeeDetails.getAmountPaid()));
            fdatePaid.setText(mfeeDetails.getDatePaid());
            fname.setText(mfeeDetails.getStudentName());
           // amonthGiven.setText(mfeeDetails.getMonthGiven());


        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, FeeDetailsEditorActivity.class);
            intent.putExtra("name", mfeeDetails.getStudentName());
            intent.putExtra("amountPaid", mfeeDetails.getAmountPaid());
            intent.putExtra("course", mfeeDetails.getCourseName());
            intent.putExtra("datePaid", mfeeDetails.getDatePaid());
            intent.putExtra("key", mfeeDetails.getKey());

            context.startActivity(intent);


        }
    }
}
