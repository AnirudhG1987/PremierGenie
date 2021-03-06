package com.premiergenie.pgbbay.Expenses;

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
import com.premiergenie.pgbbay.FeeDetails.FeeFragment;
import com.premiergenie.pgbbay.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<ExpenseDetailsClass> mexpDetailsList;

    private FirebaseRecyclerAdapter<ExpenseDetailsClass, ExpenseFragment.ExpDetailsHolder> adapter;

    private DatabaseReference mfiredatabaseRef;
    private FeeFragment.OnDataUpdatedListener mCallback;

    private ProgressBar spinner;

    private String caller;

    private Double expFeeCounter=0.0;

    public ExpenseFragment(){}


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (FeeFragment.OnDataUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        mexpDetailsList = new ArrayList<>();


         mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("expenses");


        spinner=(ProgressBar)rootview.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);



        Query latestAttendance;
   //     if(sName!=null) {
     //       latestAttendance = mfiredatabaseRef.orderByChild("studentName").equalTo(sName);
        //}
       // else {
            latestAttendance = mfiredatabaseRef.orderByChild("datePaid");
        //}



        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ExpenseDetailsClass expenseDetailsClass = dataSnapshot.getValue(ExpenseDetailsClass.class);
                expenseDetailsClass.setKey(dataSnapshot.getKey());
                if (expenseDetailsClass.getDatePaid().length() < 10) {

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                        String dateInString = expenseDetailsClass.getDatePaid();

                        Date date = formatter.parse(dateInString);
                        expenseDetailsClass.setDatePaid(formatter.format(date));
                        mfiredatabaseRef.child(expenseDetailsClass.getKey()).setValue(expenseDetailsClass);
                        //System.out.println(formatter.format(date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }


                mexpDetailsList.add(0,expenseDetailsClass);
                expFeeCounter += expenseDetailsClass.getAmountPaid();
                mCallback.onDataUpdated(expFeeCounter);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ExpenseDetailsClass expenseDetailsClass = dataSnapshot.getValue(ExpenseDetailsClass.class);
                expenseDetailsClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mexpDetailsList.size();i++){
                    ExpenseDetailsClass f = mexpDetailsList.get(i);
                    if(f.getKey() != null && f.getKey().contains(expenseDetailsClass.getKey()))
                    {
                        break;
                    }
                }
                mexpDetailsList.set(i, expenseDetailsClass);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ExpenseDetailsClass expenseDetailsClass = dataSnapshot.getValue(ExpenseDetailsClass.class);
                expenseDetailsClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mexpDetailsList.size();i++){
                    ExpenseDetailsClass f = mexpDetailsList.get(i);
                    if(f.getKey() != null && f.getKey().contains(expenseDetailsClass.getKey()))
                    {
                        break;
                    }
                }
                mexpDetailsList.remove(i);
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

                Intent intent = new Intent(getActivity(), ExpenseDetailsEditorActivity.class);
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

        adapter = new FirebaseRecyclerAdapter<ExpenseDetailsClass, ExpenseFragment.ExpDetailsHolder>(
                ExpenseDetailsClass.class, R.layout.activity_recycler_item, ExpenseFragment.ExpDetailsHolder.class, mfiredatabaseRef) {

            @Override
            public ExpenseFragment.ExpDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_recycler_item, parent, false);

                return new ExpenseFragment.ExpDetailsHolder(itemView);
            }

            @Override
            protected void populateViewHolder(ExpenseFragment.ExpDetailsHolder v, ExpenseDetailsClass model, int position) {

                v.etype.setText(model.getExpenseType());
                v.eamountPaid.setText(model.getAmountPaid());
                v.edatePaid.setText(model.getDatePaid());
                v.ename.setText(model.getExpenseDetail());

            }

            @Override
            public void onBindViewHolder(ExpenseFragment.ExpDetailsHolder holder, int position) {
                ExpenseDetailsClass itemexpDetails = mexpDetailsList.get(position);
                holder.bindAttendance(itemexpDetails);
            }

            @Override
            public int getItemCount() {
                return mexpDetailsList.size();
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


    public static class ExpDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        private TextView ename;
        private TextView etype;
        private TextView emonthGiven;
        private TextView edatePaid;
        private TextView eamountPaid;

        private ExpenseDetailsClass mexpDetails;

        private ExpDetailsHolder(View v) {
            super(v);

            ename = (TextView) v.findViewById(R.id.text2);
            edatePaid = (TextView) v.findViewById(R.id.text1);
            etype = (TextView) v.findViewById(R.id.text3);
            eamountPaid = (TextView) v.findViewById(R.id.text4);

            v.setOnClickListener(this);
        }

        private void bindAttendance(ExpenseDetailsClass expenseDetailsClass) {

            mexpDetails = expenseDetailsClass;
            etype.setText(mexpDetails.getExpenseType());
            eamountPaid.setText(String.format("%d",mexpDetails.getAmountPaid()));
            edatePaid.setText(mexpDetails.getDatePaid());
            ename.setText(mexpDetails.getExpenseDetail());
        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, ExpenseDetailsEditorActivity.class);
            intent.putExtra("expense", mexpDetails.getExpenseDetail());
            intent.putExtra("amountPaid", mexpDetails.getAmountPaid());
            intent.putExtra("type", mexpDetails.getExpenseType());
            intent.putExtra("datePaid", mexpDetails.getDatePaid());
            intent.putExtra("key", mexpDetails.getKey());

            context.startActivity(intent);


        }
    }
}
