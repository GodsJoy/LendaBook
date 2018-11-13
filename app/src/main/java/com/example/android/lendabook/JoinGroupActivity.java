package com.example.android.lendabook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
/*Activity used for joining group*/

public class JoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.group_name_ET) EditText groupNameET;
    @BindView(R.id.join_BTN)
    Button joinBTN;
    @BindView(R.id.join_errorTV)
    TextView errorTV;
    @BindView(R.id.join_successTV) TextView successTV;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDBRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        ButterKnife.bind(this);

        errorTV.setVisibility(View.INVISIBLE);
        successTV.setVisibility(View.INVISIBLE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDBRef = mFirebaseDatabase.getReference().child("user-group");

        joinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String groupName = groupNameET.getText().toString();
                //Group name must be selected to join a group
                if(!groupName.equals("")) {
                    final String key = MainActivity.removeSpecialXters(groupName);
                    SharedPreferences pref = getSharedPreferences(getString(R.string.shared_pref),
                            Context.MODE_PRIVATE);
                    final String creatorId = pref.getString(getString(R.string.existing_user_field),
                            MainActivity.default_saved_user_pref);

                    mDBRef = mFirebaseDatabase.getReference().child("user-group").child(key + creatorId);

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Log.d("exists", "Does not already exists");
                                mDBRef.setValue(new User_Group(creatorId, key, groupName));

                                successTV.setText(getString(R.string.successfully_joined_group));
                                successTV.setVisibility(View.VISIBLE);
                                groupNameET.setText("");
                            } else {
                                Log.d("exists", "Already exists");
                                errorTV.setText(getString(R.string.already_a_member_of_group));
                                errorTV.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDBRef.addListenerForSingleValueEvent(eventListener);

                }
                else{
                    errorTV.setText(getString(R.string.error_missing_fields));
                    errorTV.setVisibility(View.VISIBLE);
                }



            }
        });

    }

}
