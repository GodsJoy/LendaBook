package com.example.android.lendabook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public final static String default_saved_user_pref = "default";
    public final static int default_no_pref = 0;
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.errorTV) TextView errorTV;
    @BindView(R.id.register_BTN) Button register;
    @BindView(R.id.email_ET) EditText emailET;
    @BindView(R.id.username_ET) EditText usernameET;
    @BindView(R.id.password_ET) EditText passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences pref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        /*SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.existing_user_field), default_saved_user_pref);
        editor.commit();*/
        String existing = pref.getString(getString(R.string.existing_user_field), default_saved_user_pref);
        if(!existing.equals(default_saved_user_pref)){
            //new UserAccountActivity().launchThis(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, UserAccountActivity.class);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            errorTV.setVisibility(View.INVISIBLE);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String email = emailET.getText().toString();
                    final String username = usernameET.getText().toString();
                    final String password = passwordET.getText().toString();
                    final String key = removeSpecialXters(email);
                    //ensure that all fields are properly filled
                    if(!(email.equals("") || username.equals("") || password.equals(""))) {

                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDBRef = mFirebaseDatabase.getReference().child("user");
                        final DatabaseReference entryRef = mDBRef.child(key);
                        Log.d("exists", "Test");
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Log.d("exists", "Does not already exists");
                                    entryRef.setValue(new User(email, username, password));

                                    //setting up shared preferences for user
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(getString(R.string.existing_user_field), entryRef.getKey());
                                    editor.putString(getString(R.string.existing_email_field), email);
                                    editor.putInt(getString(R.string.no_books_owned), default_no_pref);
                                    editor.putInt(getString(R.string.no_books_borrowed), default_no_pref);
                                    editor.commit();

                                    Intent intent = new Intent(MainActivity.this, UserAccountActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.d("exists", "Already exists");
                                    errorTV.setText(getString(R.string.email_exists));
                                    errorTV.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        entryRef.addListenerForSingleValueEvent(eventListener);
                    }
                    else{
                        errorTV.setText(getString(R.string.error_missing_fields));
                        errorTV.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

    }

    public static String removeSpecialXters(String key) {
        if (key == "")
            return "";

        // Replace '.' (not allowed in a Firebase key) with ',' (not allowed in an email address)
        key = key.toLowerCase();
        key = key.replace('.', ',');
        key = key.replace('$', ',');
        key = key.replace('[', ',');
        key = key.replace(']', ',');
        key = key.replace('#', ',');
        return key;
    }

}
