package com.premiergenie.pgbbay;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.premiergenie.pgbbay.Attendance.AttendanceFragment;
import com.premiergenie.pgbbay.Expenses.ExpenseFragment;
import com.premiergenie.pgbbay.FeeDetails.FeeFragment;
import com.premiergenie.pgbbay.Fragment.SearchFieldFragment;

import java.text.DecimalFormat;


public class DataDisplayActivity extends FragmentActivity
        implements SearchFieldFragment.OnSubmitSelectedListener,
        SearchFieldFragment.OnClearSelectedListener, SearchFieldFragment.OnDateSelectedListener,
        FeeFragment.OnDataUpdatedListener
{


    private int year_x,month_x,day_x;


    private String caller;

    private android.support.v4.app.FragmentTransaction transaction;

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day ){
            year_x=year;
            month_x=month +1;
            day_x=day;

            String dateSelected = year_x + "-" + String.format("%02d",month_x) + "-" + String.format("%02d",day_x);

            SearchFieldFragment articleFrag = (SearchFieldFragment)
                    getSupportFragmentManager().findFragmentById(R.id.search_fragment);

            if (articleFrag != null) {
               articleFrag.setDateText(dateSelected);
            }

        }

    };


    @Override
    protected Dialog onCreateDialog(int id){
        return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x );
    }


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
    public void onSubmitClicked(String studentName, String dateSelected) {

        fragmentReresher();
        Fragment fragment = setupFragment();
        Bundle b = new Bundle();
        b.putString("sName", studentName);
        b.putString("sDate",dateSelected);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.result_fragment, fragment).commit();

    }

    @Override
    public void onClearClicked() {

        SearchFieldFragment articleFrag = (SearchFieldFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_fragment);


        if (articleFrag != null) {
            articleFrag.clearDateText();
        }

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

    @Override
    public void onDateClicked() {

        final Calendar cal = Calendar.getInstance();

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialog(0);


    }
}


