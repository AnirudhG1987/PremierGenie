package com.premiergenie.pgbbay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.premiergenie.pgbbay.Attendance.AttendanceActivity;
import com.premiergenie.pgbbay.FeeDetails.FeeDetailsActivity;
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
                Toast.makeText(v.getContext(),"Open list of StudentsActivity",Toast.LENGTH_SHORT).show();
                // New Intent
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, StudentsActivity.class);
                startActivity(intent);

            }
        });


        TextView fee = (TextView)findViewById(R.id.Fee_Details);
        fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Open list of Fee Details",Toast.LENGTH_SHORT).show();
                // New Intent
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, FeeDetailsActivity.class);
                startActivity(intent);

            }
        });



        TextView attendance = (TextView)findViewById(R.id.Attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Open Attendance",Toast.LENGTH_SHORT).show();
                // New Intent
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, AttendanceActivity.class);
                startActivity(intent);

            }
        });

        TextView fileUpload = (TextView)findViewById(R.id.FileUpload);
        fileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Open FileUpload",Toast.LENGTH_SHORT).show();
                // New Intent
                Intent intent = new Intent(PGBbay_DatabaseActivity.this, FileUploadActivity.class);
                startActivity(intent);

            }
        });

    }
}
