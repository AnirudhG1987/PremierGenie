package com.premiergenie.pgbbay;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.premiergenie.pgbbay.Attendance.AttendanceFragment;
import com.premiergenie.pgbbay.Expenses.ExpenseFragment;
import com.premiergenie.pgbbay.FeeDetails.FeeFragment;
import com.premiergenie.pgbbay.Fragment.SearchFieldFragment;

import java.text.DecimalFormat;


public class DataDisplayActivity extends FragmentActivity
        implements SearchFieldFragment.OnSubmitSelectedListener,
        SearchFieldFragment.OnClearSelectedListener,
        FeeFragment.OnDataUpdatedListener
{


    private String caller;

    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_data);

      caller = getIntent().getStringExtra("caller");

        setupFragment();


    }

    private Fragment setupFragment() {
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment;

        switch (caller) {
            case "Attendance":
                fragment = new AttendanceFragment();
                break;
            case "Expenses":
                fragment = new ExpenseFragment();
                break;
            case "Fee":
                fragment = new FeeFragment();
                break;
            default:
                fragment = new AttendanceFragment();
        }

        Bundle b = new Bundle();
        b.putString("caller", caller);
        fragment.setArguments(b);
        transaction.add(R.id.result_fragment, fragment, "Result_Frag");
        transaction.commit();
        return fragment;
    }


    @Override
    public void onSubmitClicked(String studentName) {

        fragmentReresher();
        Fragment fragment = setupFragment();
        Bundle b = new Bundle();
        b.putString("sName", studentName);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.result_fragment, fragment).commit();

    }

    @Override
    public void onClearClicked() {

        fragmentReresher();
        Fragment fragment = setupFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.result_fragment, fragment).commit();

    }

    private void fragmentReresher() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.result_fragment);
        if (f != null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }
    }

    @Override
    public void onDataUpdated(double d) {
        SearchFieldFragment articleFrag = (SearchFieldFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_fragment);

        if (articleFrag != null) {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            articleFrag.setCounterText(formatter.format(d));
        }
    }
}


