package com.premiergenie.pgbbay.Attendance;

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

import java.util.ArrayList;


public class AttendanceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<AttendanceClass> mattendanceList;

    private FirebaseRecyclerAdapter<AttendanceClass, AttendanceFragment.AttendanceHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    private ProgressBar spinner;


   public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        mattendanceList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("attendance");

        spinner=(ProgressBar)rootview.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        String sName = "", date = "";
        if(getArguments()!=null) {
             sName = getArguments().getString("sName");
             date = getArguments().getString("date");
        }

        Query latestAttendance;
        if(sName!=null) {
            latestAttendance = mfiredatabaseRef.orderByChild("studentName").equalTo(sName);
        }
        else {
            latestAttendance = mfiredatabaseRef;
        }

        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());
                mattendanceList.add(attendanceClass);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mattendanceList.size();i++){
                    AttendanceClass a = mattendanceList.get(i);
                    if(a.getKey() != null && a.getKey().contains(attendanceClass.getKey()))
                    {
                        break;
                    }
                }
                mattendanceList.set(i,attendanceClass);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());int i=0;
                for(;i<mattendanceList.size();i++){
                    AttendanceClass a = mattendanceList.get(i);
                    if(a.getKey() != null && a.getKey().contains(attendanceClass.getKey()))
                    {
                        break;
                    }
                }
                mattendanceList.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AttendanceEditorActivity.class);
                startActivity(intent);
            }

        });

        if(adapter!=null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();

        return rootview;

    }




    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<AttendanceClass, AttendanceFragment.AttendanceHolder>(
                AttendanceClass.class, R.layout.activity_recycler_item, AttendanceFragment.AttendanceHolder.class, mfiredatabaseRef) {

            @Override
            public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_recycler_item, parent, false);

                return new AttendanceHolder(itemView);
            }


            @Override
            protected void populateViewHolder(AttendanceFragment.AttendanceHolder v, AttendanceClass model, int position) {

                v.acour.setText(model.getCourseName());
                v.ainst.setText(model.getInstructorName());
                v.adate.setText(model.getDate());
                v.aname.setText(model.getStudentName());
            }

            @Override
            public void onBindViewHolder(AttendanceFragment.AttendanceHolder holder, int position) {
                    AttendanceClass itemAttendance = mattendanceList.get(position);
                    holder.bindAttendance(itemAttendance);
            }

            @Override
            public int getItemCount() {
               return mattendanceList.size();
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


    public static class AttendanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView aname;
        public TextView acour;
        public TextView ainst;
        public TextView adate;
        private AttendanceClass mattendance;

        public AttendanceHolder(View v) {
            super(v);

            adate = (TextView) v.findViewById(R.id.text1);
            aname = (TextView) v.findViewById(R.id.text2);
            acour = (TextView) v.findViewById(R.id.text3);
            ainst = (TextView) v.findViewById(R.id.text4);

            v.setOnClickListener(this);
        }

        public void bindAttendance(AttendanceClass attendance) {

            mattendance = attendance;
            acour.setText(attendance.getCourseName());
            ainst.setText(attendance.getInstructorName());
            adate.setText(attendance.getDate());
            aname.setText(attendance.getStudentName());

        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, AttendanceEditorActivity.class);
            intent.putExtra("name", mattendance.getStudentName());
            intent.putExtra("instructor", mattendance.getInstructorName());
            intent.putExtra("course", mattendance.getCourseName());
            intent.putExtra("date", mattendance.getDate());
            intent.putExtra("key", mattendance.getKey());
            context.startActivity(intent);
        }
    }

}
