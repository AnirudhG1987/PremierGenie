package com.premiergenie.pgbbay.FeeDetails;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;
import java.util.List;

public class FeeDetailsEditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mDateEditText;
    private EditText mAmountPaidEditText;
    private String mCourse;
    private String mKey;
    private Spinner mCoursesSpinner;
    ArrayAdapter<String> coursesSpinnerAdapter;
    private List<String> mcoursesList;

    private int year_x,month_x,day_x;

    private DatabaseReference mfiredatabaseRef;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_details_editor);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("feeDetails");


        mNameEditText = (EditText) findViewById(R.id.edit_fsname);

        final Calendar cal = Calendar.getInstance();

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        mDateEditText = (EditText) findViewById(R.id.edit_fdate);
        mAmountPaidEditText = (EditText) findViewById(R.id.edit_famount);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });




         mCoursesSpinner = (Spinner) findViewById(R.id.fee_Spinner);
        setupCourseSpinner();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle("Edit Fee Detail");
            mNameEditText.setText(extras.getString("name"));
            mDateEditText.setText(extras.getString("datePaid"));
            mAmountPaidEditText.setText(String.format("%d",extras.getInt("amountPaid")));
            mKey = extras.getString("key");
        }
        else
        {
            setTitle("Add Fee Detail");
        }

    }

    @Override

    protected Dialog onCreateDialog(int id){
        return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x );
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day ){
            year_x=year;
            month_x=month +1;
            day_x=day;

            mDateEditText.setText(day_x+"-"+month_x+"-"+year_x);

        }

    };

    private void setupCourseSpinner() {

        FirebaseDatabase.getInstance().getReference("courses").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mcoursesList = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String courseItem = data.getValue(String.class);
                    // mcoursesList.add(courseItem);
                    mcoursesList.add(courseItem);
                }

                coursesSpinnerAdapter = new ArrayAdapter<>(FeeDetailsEditorActivity.this,android.R.layout.simple_spinner_item, mcoursesList);

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

                mCourse = ""; // Unknown

            }

        });

    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }

    private void insertData(){

        FeeDetailsClass feeDetailsClass = new FeeDetailsClass(mDateEditText.getText().toString(),
                mNameEditText.getText().toString(), mCourse, Integer.parseInt(mAmountPaidEditText.getText().toString() ) );

        mfiredatabaseRef.push().setValue(feeDetailsClass);
    }


    private void clearData(){
        mNameEditText.setText("");
        mDateEditText.setText("");
        // set spinner position for courses
        mCoursesSpinner.setSelection(0);
        mAmountPaidEditText.setText("");
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                insertData();
                clearData();
                Toast.makeText(this,"Fee Detail Added Successfully",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete:
                if(mKey!=null ){
                    mfiredatabaseRef.child(mKey).removeValue();
                    clearData();
                    Toast.makeText(this, "Fee Detail Deleted", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_udpate:
                mfiredatabaseRef.child(mKey).setValue(new FeeDetailsClass(mDateEditText.getText().toString(),
                        mNameEditText.getText().toString(), mCourse, Integer.parseInt(mAmountPaidEditText.getText().toString() ) ));
                clearData();
                Toast.makeText(this,"Fee Detail Updated",Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
