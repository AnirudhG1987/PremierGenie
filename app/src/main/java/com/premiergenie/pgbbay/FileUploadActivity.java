package com.premiergenie.pgbbay;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.premiergenie.pgbbay.Fragment.SearchFieldFragment;
import com.premiergenie.pgbbay.Fragment.SearchResultFragment;

/**
 * Created by Anirudh on 11/25/2016.
 */

public class FileUploadActivity  extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileupload);

        SearchFieldFragment sff = new SearchFieldFragment();
        SearchResultFragment srf = new SearchResultFragment();

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.add(R.id.search_fragment, sff, "Frag_Top_tag");
        transaction.add(R.id.result_fragment, srf, "Frag_Bottom_tag");
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


}
