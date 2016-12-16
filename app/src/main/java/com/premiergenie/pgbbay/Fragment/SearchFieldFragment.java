package com.premiergenie.pgbbay.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.premiergenie.pgbbay.R;

public class SearchFieldFragment extends Fragment {

    public SearchFieldFragment() {
        // Required empty public constructor
    }

    OnSubmitSelectedListener mCallback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSubmitSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_search_field, container, false);

        final EditText sNameEditText = (EditText) rootview.findViewById(R.id.searchSName);
        final EditText sDateEditText = (EditText) rootview.findViewById(R.id.searchDate);

        Button b = (Button) rootview.findViewById(R.id.searchSubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New Intent
                mCallback.onSubmitClicked(sDateEditText.getText().toString(), sNameEditText.getText().toString());
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
        void onSubmitClicked(String d, String s);
    }

}
