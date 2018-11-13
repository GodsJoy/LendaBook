package com.example.android.lendabook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/*Activity to create a book group*/
public class CreateGroupActivity extends AppCompatActivity {
    @BindView(R.id.create_BTN) Button createGroup;
    @BindView(R.id.group_name_ET) EditText groupNameET;
    @BindView(R.id.group_desc_ET) EditText groupDescET;
    @BindView(R.id.group_errorTV) TextView errorTV;
    @BindView(R.id.group_successTV) TextView successTV;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        errorTV.setVisibility(View.INVISIBLE);
        successTV.setVisibility(View.INVISIBLE);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String groupName = groupNameET.getText().toString();
                if(!groupName.equals("")) {
                    final String desc = groupDescET.getText().toString();

                    //get userId from shared preferences. This is used as book ownerId
                    SharedPreferences pref = getSharedPreferences(getString(R.string.shared_pref),
                            Context.MODE_PRIVATE);
                    final String creatorId = pref.getString(getString(R.string.existing_user_field),
                            MainActivity.default_saved_user_pref);
                    final float currentDate = System.currentTimeMillis();
                    final String key = MainActivity.removeSpecialXters(groupName);
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDBRef = mFirebaseDatabase.getReference().child("group");
                    final DatabaseReference entryRef = mDBRef.child(key);

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //create a book only if it is not already existing
                            if (!dataSnapshot.exists()) {
                                Log.d("exists", "Does not already exists");
                                entryRef.setValue(new Group(groupName, desc, creatorId, currentDate));

                                //insert entry for this user in user-group table
                                DatabaseReference user_groupRef = mFirebaseDatabase.getReference().child("user-group").child(key + creatorId);
                                user_groupRef.setValue(new User_Group(creatorId, key, groupName));

                                successTV.setText(getString(R.string.successfully_created_group));
                                successTV.setVisibility(View.VISIBLE);
                                groupNameET.setText("");
                                groupDescET.setText("");
                            } else {
                                Log.d("exists", "Already exists");
                                errorTV.setText(getString(R.string.group_name_exists));
                                errorTV.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    entryRef.addListenerForSingleValueEvent(eventListener);
                    //Log.d("CreateGroup", "Data pushed to DB");
                }
                else{
                    errorTV.setText(getString(R.string.error_missing_fields));
                    errorTV.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
