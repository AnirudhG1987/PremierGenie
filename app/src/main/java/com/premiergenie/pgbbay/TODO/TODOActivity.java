package com.premiergenie.pgbbay.TODO;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.premiergenie.pgbbay.DividerItemDecoration;
import com.premiergenie.pgbbay.R;

import java.util.ArrayList;

/**
 * Created by Anirudh on 11/16/2016.
 */

public class TODOActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<TODOClass> mtasksList;

    private FirebaseRecyclerAdapter<TODOClass, TODOHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


    protected void onCreate(Bundle savedInstanceState) {

        mtasksList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("todo");

        mfiredatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TODOClass task = dataSnapshot.getValue(TODOClass.class);
                task.setKey(dataSnapshot.getKey());
                mtasksList.add(task);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TODOClass task = dataSnapshot.getValue(TODOClass.class);
                int i=0;

                for(;i<mtasksList.size();i++){
                    TODOClass t = mtasksList.get(i);
                    if(t.getKey() != null && t.getKey().contains(task.getKey()))
                    {
                        break;
                    }
                }
                mtasksList.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);

        mRecyclerView = (RecyclerView) findViewById(R.id.task_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        final EditText task_box = (EditText) findViewById(R.id.add_task_box);

        Button addTask = (Button) findViewById(R.id.add_task_button);

        addTask.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                TODOClass todo = new TODOClass(task_box.getText().toString());
                mfiredatabaseRef.push().setValue(todo);
                task_box.setText("");
            }
        });

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<TODOClass, TODOHolder>(
                TODOClass.class, R.layout.activity_recycler_item_todo, TODOHolder.class, mfiredatabaseRef) {

            @Override
            public TODOHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_recycler_item_todo, parent, false);

                return new TODOHolder(itemView);
            }

            @Override
            public void onBindViewHolder(TODOHolder holder, int position) {
                TODOClass itemTODO = mtasksList.get(position);
                holder.bindTask(itemTODO);
            }

            @Override
            protected void populateViewHolder(TODOHolder viewHolder, TODOClass model, int position) {

            }

        };
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart(){
        if(adapter!=null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }


    public static class TODOHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener, View.OnCreateContextMenuListener {
        //2

        public TextView taskTxtView;
        private TODOClass mtask;

        public TODOHolder(View v) {
            super(v);

            taskTxtView = (TextView) v.findViewById(R.id.task_item);
            v.setOnLongClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        public void bindTask(TODOClass task) {

            mtask = task;
            taskTxtView.setText(mtask.getTodo());
        }


        @Override
        public boolean onLongClick(View v) {
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "SMS");
        }
    }

}
