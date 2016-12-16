package com.premiergenie.pgbbay.Attendance;

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
import com.google.firebase.database.Query;
import com.premiergenie.pgbbay.DividerItemDecoration;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;


public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<AttendanceClass> mattendanceList;

    private FirebaseRecyclerAdapter<AttendanceClass, AttendanceHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    protected void onCreate(Bundle savedInstanceState) {

       mattendanceList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("attendance");

        Query latestAttendance = mfiredatabaseRef.orderByChild("studentName");
        //mfiredatabaseRef.addChildEventListener(new ChildEventListener() {
        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());
                mattendanceList.add(attendanceClass);
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

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recyclerview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AttendanceActivity.this, AttendanceEditorActivity.class);
                startActivity(intent);
            }

        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

         mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<AttendanceClass, AttendanceHolder>(
                AttendanceClass.class, R.layout.attendance_view, AttendanceHolder.class, mfiredatabaseRef) {
            @Override
            protected void populateViewHolder(AttendanceHolder v, AttendanceClass model, int position) {

                v.acour.setText(model.getCourseName());
                v.ainst.setText(model.getInstructorName());
                v.adate.setText(model.getDate());
                v.aname.setText(model.getStudentName());

            }

            @Override
            public void onBindViewHolder(AttendanceHolder holder, int position) {
                AttendanceClass itemAttendance = mattendanceList.get(position);
                holder.bindAttendance(itemAttendance);
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



