package com.premiergenie.pgbbay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.premiergenie.pgbbay.Students.StudentsActivity;

public class PGBbay_DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgbbay__database);

        TextView students = (TextView)findViewById(R.id.Students);
        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(PGBbay_DatabaseActivity.this, StudentsActivity.class);
                intent.putExtra("caller", "Student");
                startActivity(intent);

            }
        });


        TextView attendance = (TextView)findViewById(R.id.Attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, DataDisplayActivity.class);
                intent.putExtra("caller", "Attendance");
                startActivity(intent);

            }
        });

        TextView fee = (TextView)findViewById(R.id.Fee_Details);
        fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, DataDisplayActivity.class);
                intent.putExtra("caller", "Fee");
                startActivity(intent);

            }
        });

        TextView exp = (TextView)findViewById(R.id.Expenses);
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, DataDisplayActivity.class);
                intent.putExtra("caller", "Expenses");
                startActivity(intent);

            }
        });


        TextView toDO = (TextView)findViewById(R.id.TODO);
        toDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(PGBbay_DatabaseActivity.this, DataDisplayActivity.class);
                intent.putExtra("caller", "TODO");
                startActivity(intent);

            }
        });

    }
}
