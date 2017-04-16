package com.premiergenie.pgbbay.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.premiergenie.pgbbay.R;
import com.premiergenie.pgbbay.Students.StudentClass;

import java.util.ArrayList;
import java.util.List;

public class SearchFieldFragment extends Fragment {

    private Spinner mStudentsSpinner;
    private String mStudent;
    private View rootview;

    private TextView mCounterText;

    private EditText mDateEditText;


    ArrayAdapter<String> studentsSpinnerAdapter;


   private List<String> mstudentsList;


    public SearchFieldFragment() {
        // Required empty public constructor
    }

    OnSubmitSelectedListener mCallback;
    OnDateSelectedListener mDateCallBack;
    OnClearSelectedListener mClearCallback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnSubmitSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitSelectedListener");
        }

        try {
            mClearCallback = (OnClearSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClearSelectedListener");
        }


        try {
            mDateCallBack = (OnDateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Date selected");
        }
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_search_field, container, false);

        mStudentsSpinner = (Spinner) rootview.findViewById(R.id.searchSName);

        mCounterText = (TextView) rootview.findViewById(R.id.counterText);

        setupStudentsSpinner();

        mDateEditText = (EditText) rootview.findViewById(R.id.searchSDate);
        mDateEditText.setInputType(InputType.TYPE_NULL);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateCallBack.onDateClicked();
            }
        });




        Button b = (Button) rootview.findViewById(R.id.searchSubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New Intent
                mCallback.onSubmitClicked(mStudent,mDateEditText.getText().toString());
            }
        });

        Button c = (Button) rootview.findViewById(R.id.refreshButton);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New Intent
                mClearCallback.onClearClicked();
            }
        });

        return rootview;
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    public interface OnSubmitSelectedListener{
        void onSubmitClicked(String s, String d);
    }

    public interface OnClearSelectedListener{
        void onClearClicked();
    }

    public interface OnDateSelectedListener{
        void onDateClicked();
    }

    public void setCounterText(String s) {

        mCounterText.setText(s);
    }


    public void clearDateText() {

        mDateEditText.setText("");
    }

    public void setDateText(String s) {
        mDateEditText.setText(s);
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
                studentsSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mstudentsList);

                studentsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mStudentsSpinner.setAdapter(studentsSpinnerAdapter);
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
}
