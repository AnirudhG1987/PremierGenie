package com.premiergenie.pgbbay.Students;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.premiergenie.pgbbay.DividerItemDecoration;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;

/**
 * Created by Anirudh on 11/16/2016.
 */

public class StudentsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<StudentClass> mstudentsList;

    private FirebaseRecyclerAdapter<StudentClass, StudentsActivity.StudentHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


    protected void onCreate(Bundle savedInstanceState) {

        mstudentsList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("students");

        Query query = mfiredatabaseRef.orderByChild("firstName");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudentClass studentClass = dataSnapshot.getValue(StudentClass.class);
                studentClass.setKey(dataSnapshot.getKey());
                mstudentsList.add(studentClass);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StudentClass studentClass = dataSnapshot.getValue(StudentClass.class);
                studentClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mstudentsList.size();i++){
                    StudentClass stu = mstudentsList.get(i);
                    if(stu.getKey() != null && stu.getKey().contains(studentClass.getKey()))
                    {
                        break;
                    }
                }
                mstudentsList.set(i,studentClass);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                StudentClass studentClass = dataSnapshot.getValue(StudentClass.class);
                studentClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mstudentsList.size();i++){
                    StudentClass stu = mstudentsList.get(i);
                    if(stu.getKey() != null && stu.getKey().contains(studentClass.getKey()))
                    {
                        break;
                    }
                }
                mstudentsList.remove(i);
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

                Intent intent = new Intent(StudentsActivity.this, StudentEditorActivity.class);
                startActivity(intent);
            }

        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        //mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<StudentClass, StudentHolder>(
                StudentClass.class, R.layout.activity_recycler_item, StudentHolder.class, mfiredatabaseRef) {

            @Override
            public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_recycler_item, parent, false);

                return new StudentHolder(itemView);
            }

            @Override
            protected void populateViewHolder(StudentHolder v, StudentClass model, int position) {

                v.sname.setText(model.getFirstName()+" "+model.getLastName());
                v.sgrade.setText(model.getGrade());
                v.scurr.setText(model.getCurriculum());
            }

            @Override
            public void onBindViewHolder(StudentHolder holder, int position) {
                StudentClass itemStudent = mstudentsList.get(position);
                holder.bindStudent(itemStudent);
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
    }


    public static class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView sname;
        public TextView sgrade;
        public TextView scurr;
        public TextView sschool;
        private StudentClass mstudent;

        public StudentHolder(View v) {
            super(v);

            sname = (TextView) v.findViewById(R.id.text1);
            sgrade = (TextView) v.findViewById(R.id.text2);
            scurr = (TextView) v.findViewById(R.id.text4);
            sschool = (TextView) v.findViewById(R.id.text3);

            v.setOnClickListener(this);
        }

        public void bindStudent(StudentClass student) {

            mstudent = student;
            sname.setText(mstudent.getFirstName()+" "+mstudent.getLastName());
            sgrade.setText(""+mstudent.getGrade());
            scurr.setText(mstudent.getCurriculum());
            sschool.setText(mstudent.getSchoolName().replaceAll("\\B.|\\P{L}", "").toUpperCase());

        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, StudentEditorActivity.class);
            intent.putExtra("fname", mstudent.getFirstName());
            intent.putExtra("lname", mstudent.getLastName());
            intent.putExtra("grade", ""+mstudent.getGrade());
            intent.putExtra("age", ""+mstudent.getAge());
            intent.putExtra("school", mstudent.getSchoolName());
            intent.putExtra("parent", mstudent.getParentName());
            intent.putExtra("email", mstudent.getPrimaryEmail());
            intent.putExtra("phone", mstudent.getPhoneNumber());
            intent.putExtra("key", mstudent.getKey());
            intent.putExtra("curriculum", mstudent.getCurriculum());
            context.startActivity(intent);
        }
    }

}
