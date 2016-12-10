package com.premiergenie.pgbbay.FeeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.premiergenie.pgbbay.DividerItemDecoration;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;

public class FeeDetailsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<FeeDetailsClass> mfeeDetailsList;

    private FirebaseRecyclerAdapter<FeeDetailsClass, FeeDetailsActivity.FeeDetailsHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    private static final int URL_LOADER = 0;

    protected void onCreate(Bundle savedInstanceState) {

        mfeeDetailsList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("feeDetails");

      /*  mfiredatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mfeeDetailsList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {

                    FeeDetailsClass feeDetailsClass = data.getValue(FeeDetailsClass.class);
                    feeDetailsClass.setKey(data.getKey());
                    mfeeDetailsList.add(feeDetailsClass);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
        */

        mfiredatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeeDetailsClass feeDetailsClass = dataSnapshot.getValue(FeeDetailsClass.class);
                feeDetailsClass.setKey(dataSnapshot.getKey());
                mfeeDetailsList.add(feeDetailsClass);
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
                // System.out.println("THis is the index "+i);
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

        setContentView(R.layout.activity_recyclerview);
        //setContentView(R.layout.activity_fee__details);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFeeDetails);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeeDetailsActivity.this, FeeDetailsEditorActivity.class);
                startActivity(intent);
            }

        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        //mRecyclerView = (RecyclerView) findViewById(R.id.feeDetailsList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<FeeDetailsClass, FeeDetailsActivity.FeeDetailsHolder>(
                FeeDetailsClass.class, R.layout.fee_view, FeeDetailsActivity.FeeDetailsHolder.class, mfiredatabaseRef) {
            @Override
            protected void populateViewHolder(FeeDetailsActivity.FeeDetailsHolder v, FeeDetailsClass model, int position) {

                v.fcour.setText(model.getCourseName());
                v.famountPaid.setText(model.getAmountPaid());
                v.fdatePaid.setText(model.getDatePaid());
                v.fname.setText(model.getStudentName());

            }

            @Override
            public void onBindViewHolder(FeeDetailsActivity.FeeDetailsHolder holder, int position) {
                FeeDetailsClass itemFeeDetails = mfeeDetailsList.get(position);
                holder.bindAttendance(itemFeeDetails);
            }

        };
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart(){
        if(adapter!=null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
        //mfiredatabaseRef.remove;
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

            fname = (TextView) v.findViewById(R.id.feename);
            fdatePaid = (TextView) v.findViewById(R.id.feedate);
            fcour = (TextView) v.findViewById(R.id.feecourse);
            famountPaid = (TextView) v.findViewById(R.id.feeAmount);

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
