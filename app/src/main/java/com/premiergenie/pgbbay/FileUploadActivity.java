package com.premiergenie.pgbbay;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.premiergenie.pgbbay.Fragment.SearchFieldFragment;
import com.premiergenie.pgbbay.Fragment.SearchResultFragment;


public class FileUploadActivity  extends FragmentActivity implements SearchFieldFragment.OnSubmitSelectedListener {


    private EditText sNameEditTxt;

    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileupload);

        sNameEditTxt = (EditText) findViewById(R.id.searchSName);

        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        SearchResultFragment srf = new SearchResultFragment();
        transaction.add(R.id.result_fragment,srf,"Result_Frag");
        transaction.commit();

         /*

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://premier-genie.appspot.com");
        StorageReference worksheetRef = storageRef.child("Worksheet");

        final StorageReference testRef = storageRef.child("Worksheet/Physics Term 2 Test.pdf");

        Button download = (Button)findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {

                testRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

/*

                Uri file = Uri.fromFile(new File("path/to/folderName/file.jpg"));
                UploadTask uploadTask = testRef.putFile(file);

            }

        });

*/


    }


    @Override
    public void onSubmitClicked(String date, String studentName) {
       /* Fragment srf = null;
        srf = getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        */

        sNameEditTxt.setText("");
        sNameEditTxt.clearFocus();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.result_fragment);
        if (f!=null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }

        SearchResultFragment srf = new SearchResultFragment();
        Bundle b = new Bundle();
        b.putString("sName",studentName);
        srf.setArguments(b);

        getSupportFragmentManager().beginTransaction().replace(R.id.result_fragment, srf).commit();

    }
}
