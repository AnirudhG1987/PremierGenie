package com.premiergenie.pgbbay.Attendance;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.premiergenie.pgbbay.DataPopulator;
import com.premiergenie.pgbbay.R;
import com.premiergenie.pgbbay.Students.StudentClass;

import java.util.ArrayList;
import java.util.List;


public class AttendanceEditorActivity extends AppCompatActivity {

   // private EditText mNameEditText;
    private EditText mDateEditText;
    private String mInstructor, mCourse, mStudent, mKey;
    private Spinner mInstructorSpinner;
    private Spinner mCoursesSpinner;
    private Spinner mStudentsSpinner;
    ArrayAdapter<String> coursesSpinnerAdapter;
    ArrayAdapter<String> instructorSpinnerAdapter;
    ArrayAdapter<String> studentsSpinnerAdapter;

    private List<String> mcoursesList;
    private List<String> mstudentsList;
    private int year_x,month_x,day_x;

    private DatabaseReference mfiredatabaseRef;

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day ){
            year_x=year;
            month_x=month +1;
            day_x=day;

            mDateEditText.setText(year_x + "-" + month_x + "-" + day_x);

        }

    };

   @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_editor);

       mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("attendance");


       // mNameEditText = (EditText) findViewById(R.id.edit_sname);

       final Calendar cal = Calendar.getInstance();

       year_x = cal.get(Calendar.YEAR);
       month_x = cal.get(Calendar.MONTH);
       day_x = cal.get(Calendar.DAY_OF_MONTH);

       mDateEditText = (EditText) findViewById(R.id.edit_date);

       mDateEditText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                showDialog(0);
           }
       });


       mInstructorSpinner = (Spinner) findViewById(R.id.edit_inst);
       mCoursesSpinner = (Spinner) findViewById(R.id.course_Spinner);
       mStudentsSpinner = (Spinner) findViewById(R.id.student_Spinner);


       setupStudentsSpinner();
       setupCourseSpinner();
       setupInstructorSpinner();

       Bundle extras = getIntent().getExtras();
       if (extras != null) {
           setTitle("Edit Attendance");
           mDateEditText.setText(extras.getString("date"));
           mKey = extras.getString("key");
       }
       else
       {
           setTitle("Add Attendance");
       }

   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   coursesSpinnerAdapter.clear();
        instructorSpinnerAdapter.clear();
        studentsSpinnerAdapter.clear();
    }


    @Override

    protected Dialog onCreateDialog(int id){
        return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x );
    }




    private void setupStudentsSpinner() {


        Query query = FirebaseDatabase.getInstance().getReference("students").orderByChild("firstName");
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


    private void setupCourseSpinner() {

        //DataPopulator.populateCoursesSpinner(mCoursesSpinner,getApplicationContext());


        FirebaseDatabase.getInstance().getReference("courses").orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mcoursesList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String courseItem = data.getValue(String.class);
                    mcoursesList.add(courseItem);
                }
                coursesSpinnerAdapter = new ArrayAdapter<>(AttendanceEditorActivity.this, android.R.layout.simple_spinner_item, mcoursesList);

                coursesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mCoursesSpinner.setAdapter(coursesSpinnerAdapter);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {

                    // set spinner position for courses
                    String courseName = extras.getString("course");
                    if (!courseName.equals(null)) {
                        int spinnerPosition = mcoursesList.indexOf(courseName);
                        mCoursesSpinner.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        mcoursesList = DataPopulator.getInstance().mcoursesList;

        mCoursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    mCourse = selection;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCourse = "Other"; // Unknown
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



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }

    private void insertData(){

      // AttendanceClass attendanceClass = new AttendanceClass(mDateEditText.getText().toString(),
        //        mNameEditText.getText().toString(), mInstructor, mCourse  );
        AttendanceClass attendanceClass = new AttendanceClass(mDateEditText.getText().toString(),
                        mStudent, mInstructor, mCourse  );
        mfiredatabaseRef.push().setValue(attendanceClass);
    }


    private void clearData(){
       // mNameEditText.setText("");
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                insertData();
                clearData();
                Toast.makeText(this,"Attendance Added Successfully",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete:
                if(mKey!=null ){
                mfiredatabaseRef.child(mKey).removeValue();
                clearData();
                Toast.makeText(this, "Attendance Deleted", Toast.LENGTH_SHORT).show();
            }
                return true;

            case R.id.action_udpate:
              //  mfiredatabaseRef.child(mKey).setValue(new AttendanceClass(mDateEditText.getText().toString(),
                     //   mNameEditText.getText().toString(), mInstructor, mCourse  ));
                mfiredatabaseRef.child(mKey).setValue(new AttendanceClass(mDateEditText.getText().toString(),
                       mStudent, mInstructor, mCourse  ));
                clearData();
                Toast.makeText(this,"Attendance Updated",Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
