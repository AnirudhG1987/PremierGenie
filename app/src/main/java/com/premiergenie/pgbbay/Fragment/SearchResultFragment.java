package com.premiergenie.pgbbay.Fragment;

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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.premiergenie.pgbbay.Attendance.AttendanceClass;
import com.premiergenie.pgbbay.Attendance.AttendanceEditorActivity;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;


public class SearchResultFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<AttendanceClass> mattendanceList;

    private FirebaseRecyclerAdapter<AttendanceClass, SearchResultFragment.AttendanceHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


   public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mattendanceList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("attendance");

        String sName = "", date = "";
        if(getArguments()!=null) {
             sName = getArguments().getString("sName");
             date = getArguments().getString("date");
        }

        Query latestAttendance;
        if(!sName.isEmpty()) {
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
                System.out.println(attendanceClass.getStudentName()+attendanceClass.getDate());
                System.out.println(mattendanceList.size());
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



        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_search_result, container, false);

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList1);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(FileUploadActivity.this, AttendanceEditorActivity.class);
                //               startActivity(intent);
            }

        });

        if(adapter!=null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();

        return rootview;
    }




    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<AttendanceClass, SearchResultFragment.AttendanceHolder>(
                AttendanceClass.class, R.layout.attendance_view, SearchResultFragment.AttendanceHolder.class, mfiredatabaseRef) {

            @Override
            protected void populateViewHolder(SearchResultFragment.AttendanceHolder v, AttendanceClass model, int position) {

                v.acour.setText(model.getCourseName());
                v.ainst.setText(model.getInstructorName());
                v.adate.setText(model.getDate());
                v.aname.setText(model.getStudentName());
            }

            @Override
            public void onBindViewHolder(SearchResultFragment.AttendanceHolder holder, int position) {
                if (position < mattendanceList.size()) {
                    AttendanceClass itemAttendance = mattendanceList.get(position);
                    holder.bindAttendance(itemAttendance);
                }
            }
        };

        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onStart(){

        super.onStart();


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

            aname = (TextView) v.findViewById(R.id.aname);
            adate = (TextView) v.findViewById(R.id.adate);
            ainst = (TextView) v.findViewById(R.id.ainst);
            acour = (TextView) v.findViewById(R.id.acourse);

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
