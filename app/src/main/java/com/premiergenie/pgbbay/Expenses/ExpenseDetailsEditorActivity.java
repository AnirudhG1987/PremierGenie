package com.premiergenie.pgbbay.Expenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.premiergenie.pgbbay.R;

public class ExpenseDetailsEditorActivity extends AppCompatActivity {

    private EditText mExpEditText;
    private EditText mDateEditText;
    private EditText mAmountPaidEditText;
   private String mKey;

    private int year_x,month_x,day_x;

    private DatabaseReference mfiredatabaseRef;

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day ){
            year_x=year;
            month_x=month +1;
            day_x=day;

            mDateEditText.setText(year_x + "-" + String.format("%02d",month_x) + "-" + String.format("%02d",day_x));

        }

    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_details_editor);



        String sName = "", date = "";

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("expenses");

        mExpEditText = (EditText) findViewById(R.id.edit_fsname);
        TextView expTxtView = (TextView) findViewById(R.id.stuexptxtview);
        expTxtView.setText("Expense Detail");
        TextView etypeTxtView = (TextView) findViewById(R.id.courexptxtview);
        etypeTxtView.setText("Expense Type");

        final Calendar cal = Calendar.getInstance();

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        mDateEditText = (EditText) findViewById(R.id.edit_fdate);
        mDateEditText.setInputType(InputType.TYPE_NULL);

        mAmountPaidEditText = (EditText) findViewById(R.id.edit_famount);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle("Edit Exp Detail");
            mExpEditText.setText(extras.getString("expense"));
            mDateEditText.setText(extras.getString("datePaid"));
            mAmountPaidEditText.setText(String.format("%d",extras.getInt("amountPaid")));
            mKey = extras.getString("key");
        }
        else
        {
            setTitle("Add Exp Detail");
        }

    }

    @Override

    protected Dialog onCreateDialog(int id){
        return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x );
    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }

    private void insertData(){


        ExpenseDetailsClass expenseDetailsClass = new ExpenseDetailsClass(mDateEditText.getText().toString(),
                mExpEditText.getText().toString(), Integer.parseInt(mAmountPaidEditText.getText().toString() ) );

        mfiredatabaseRef.push().setValue(expenseDetailsClass);
    }


    private void clearData(){
        mExpEditText.setText("");
        mAmountPaidEditText.setText("");
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                insertData();
                clearData();
                Toast.makeText(this,"Exp Detail Added Successfully",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete:
                if(mKey!=null ){
                    mfiredatabaseRef.child(mKey).removeValue();
                    clearData();
                    Toast.makeText(this, "Exp Detail Deleted", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_udpate:
                mfiredatabaseRef.child(mKey).setValue(new ExpenseDetailsClass(mDateEditText.getText().toString(),
                        mExpEditText.getText().toString(),  Integer.parseInt(mAmountPaidEditText.getText().toString() ) ));
                clearData();
                Toast.makeText(this, "Exp Detail Updated",Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
