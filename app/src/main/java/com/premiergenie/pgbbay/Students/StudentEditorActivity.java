package com.premiergenie.pgbbay.Students;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.premiergenie.pgbbay.R;


/**
 * Created by Anirudh on 11/16/2016.
 */

public class StudentEditorActivity extends AppCompatActivity {

    private EditText mFirstNameEditText, mLastNameEditText, mGradeEditText, mAgeEditText,
            mSchoolNameEditText, mParentNameEditText, mPrimaryEmailEditText,mPhoneEditText,mCurriculumEditText;
    private String mKey;

   private DatabaseReference mfiredatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_editor);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("students");


        mFirstNameEditText = (EditText) findViewById(R.id.edit_sfname);
        mLastNameEditText = (EditText) findViewById(R.id.edit_slname);
        mGradeEditText = (EditText) findViewById(R.id.edit_grade);
        mAgeEditText = (EditText) findViewById(R.id.edit_age);
        mSchoolNameEditText = (EditText) findViewById(R.id.edit_scname);
        mParentNameEditText = (EditText) findViewById(R.id.edit_spname);
        mPrimaryEmailEditText = (EditText) findViewById(R.id.edit_semail);
        mPhoneEditText = (EditText) findViewById(R.id.edit_sphone);
        mCurriculumEditText = (EditText) findViewById(R.id.edit_sccurr);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle("Edit Student Detail");
            mFirstNameEditText.setText(extras.getString("fname"));
            mLastNameEditText.setText(extras.getString("lname"));
            mGradeEditText.setText(extras.getString("grade"));
            mAgeEditText.setText(extras.getString("age"));
            mSchoolNameEditText.setText(extras.getString("school"));
            mParentNameEditText.setText(extras.getString("parent"));
            mPrimaryEmailEditText.setText(extras.getString("email"));
            mPhoneEditText.setText(extras.getString("phone"));
            mCurriculumEditText.setText(extras.getString("curriculum"));
            mKey = extras.getString("key");
        }
        else
        {
            setTitle("Add Student");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }

    private void insertData(){

        StudentClass studentClass = new StudentClass(mFirstNameEditText.getText().toString(),
                mLastNameEditText.getText().toString(),mParentNameEditText.getText().toString(),
                mSchoolNameEditText.getText().toString(),Integer.parseInt(mGradeEditText.getText().toString()),
                Integer.parseInt(mAgeEditText.getText().toString()), mPrimaryEmailEditText.getText().toString(),
                mPhoneEditText.getText().toString(),mCurriculumEditText.getText().toString());

        mfiredatabaseRef.push().setValue(studentClass);
    }


    private void clearData(){
        mFirstNameEditText.setText("");
        mLastNameEditText.setText("");
        mParentNameEditText.setText("");
        mSchoolNameEditText.setText("");
        mGradeEditText.setText("");
        mAgeEditText.setText("");
        mPrimaryEmailEditText.setText("");
        mPhoneEditText.setText("");
        mCurriculumEditText.setText("");
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                insertData();
                clearData();
                Toast.makeText(this,"Student Added Successfully",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete:
                if(mKey!=null ){
                    mfiredatabaseRef.child(mKey).removeValue();
                    clearData();
                    Toast.makeText(this, "Student Deleted", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_udpate:
                mfiredatabaseRef.child(mKey).setValue(new StudentClass(mFirstNameEditText.getText().toString(),
                        mLastNameEditText.getText().toString(),mParentNameEditText.getText().toString(),
                        mSchoolNameEditText.getText().toString(),Integer.parseInt(mGradeEditText.getText().toString()),
                        Integer.parseInt(mAgeEditText.getText().toString()), mPrimaryEmailEditText.getText().toString(),
                        mPhoneEditText.getText().toString(),mCurriculumEditText.getText().toString()));

                clearData();
                Toast.makeText(this,"Student Updated",Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
