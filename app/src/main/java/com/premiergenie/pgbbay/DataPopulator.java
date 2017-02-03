package com.premiergenie.pgbbay;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh on 1/7/2017.
 */

public class DataPopulator {

    private static DataPopulator instance = null;


    public static List<String> mcoursesList;
    public static ArrayAdapter<String> coursesSpinnerAdapter;

    private void DataPopulator(){}


    public static void populateCoursesSpinner(final Spinner mCoursesSpinner, final Context context) {

        if(instance==null){
            instance = new DataPopulator();
        }


        FirebaseDatabase.getInstance().getReference("courses").orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mcoursesList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String courseItem = data.getValue(String.class);
                    mcoursesList.add(courseItem);
                }
                coursesSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mcoursesList);

                coursesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mCoursesSpinner.setAdapter(coursesSpinnerAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    /*
    private void setupStudentsSpinner() {


        Query query = FirebaseDatabase.getInstance().getReference("students").orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mstudentsList = new ArrayList<>();
                System.out.println(dataSnapshot.toString());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    StudentClass studentClass = data.getValue(StudentClass.class);
                    mstudentsList.add(studentClass.getFirstName()+" "+studentClass.getLastName());
                }
                studentsSpinnerAdapter = new ArrayAdapter<>(AttendanceEditorActivity.this, android.R.layout.simple_spinner_item, mstudentsList);

                studentsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mStudentsSpinner.setAdapter(studentsSpinnerAdapter);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String studentName = extras.getString("name");
                    if (!studentName.equals(null)) {

                        int spinnerPosition = mstudentsList.indexOf(studentName);
                        mStudentsSpinner.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mStudentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    mStudent = selection;
                }

            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {

                mStudent = "Other"; // Unknown

            }

        });


    }

    private void setupInstructorSpinner() {
        final List<String> instructorsList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("instructors").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String courseItem = data.getValue().toString();
                    instructorsList.add(courseItem);
                }
                instructorSpinnerAdapter = new ArrayAdapter(AttendanceEditorActivity.this,
                        android.R.layout.simple_spinner_item,instructorsList);

                instructorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mInstructorSpinner.setAdapter(instructorSpinnerAdapter);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {

                    String instValue = extras.getString("instructor");
                    if (!instValue.equals(null)) {
                        int spinnerPosition = instructorSpinnerAdapter.getPosition(instValue);
                        mInstructorSpinner.setSelection(spinnerPosition);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        mInstructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    mInstructor = selection;
                }

            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {

                mInstructor = "Other"; // Unknown

            }

        });

    }

  */

}
