package com.premiergenie.pgbbay.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    ArrayAdapter<String> studentsSpinnerAdapter;

   private List<String> mstudentsList;


    public SearchFieldFragment() {
        // Required empty public constructor
    }

    OnSubmitSelectedListener mCallback;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_search_field, container, false);

        mStudentsSpinner = (Spinner) rootview.findViewById(R.id.searchSName);

        setupStudentsSpinner();

        Button b = (Button) rootview.findViewById(R.id.searchSubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New Intent
                mCallback.onSubmitClicked(mStudent);
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
        void onSubmitClicked(String s);
    }

    public interface OnClearSelectedListener{
        void onClearClicked();
    }

    public void setCounterText(String s) {
        TextView t = (TextView) rootview.findViewById(R.id.counterText);
        t.setText(s);
    }


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
