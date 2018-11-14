package com.example.android.lendabook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayomide on 11/12/18.
 */
public class SearchBookFragment extends Fragment
        implements AdapterView.OnItemSelectedListener{
    @BindView(R.id.search_spinner)
    Spinner searchSpinner;
    @BindView(R.id.searchBTN)
    Button searchBTN;
    @BindView(R.id.available_booksRV)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_errorTV)
    TextView errorTV;
    SearchBooksAdapter mAdapter;

    private SearchBookActivity parentActivity;
    private ArrayList<String> all_groups = new ArrayList<>();
    private ArrayList<Book> all_books = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private String selectedGroup;

    public SearchBookFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_group, container, false);
        ButterKnife.bind(this, rootView);
        parentActivity = (SearchBookActivity)getActivity();
        mAdapter = new SearchBooksAdapter(parentActivity, parentActivity);
        int columnCount = parentActivity.getResources().getInteger(R.integer.list_column_count);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(parentActivity, columnCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        all_groups.add(parentActivity.getString(R.string.select_a_group));
        SharedPreferences pref = parentActivity.getSharedPreferences(getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        String userId = pref.getString(getString(R.string.existing_user_field),
                MainActivity.default_saved_user_pref);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDBRef = mFirebaseDatabase.getReference().child("user-group");
        mDBRef.orderByChild("user_id").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User_Group group = dataSnapshot.getValue(User_Group.class);
                all_groups.add(group.getGroup_name());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*if(all_groups.size() == 0)
            all_groups.add("No group");*/
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_spinner_item, all_groups);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(spinnerAdapter);
        searchSpinner.setOnItemSelectedListener(this);

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedGroup.equals(parentActivity.getString(R.string.select_a_group))) {
                    errorTV.setVisibility(View.INVISIBLE);
                    DatabaseReference bookRef = mFirebaseDatabase.getReference().child("book");
                    bookRef.orderByChild("group").equalTo(selectedGroup).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Book book = dataSnapshot.getValue(Book.class);
                            all_books.add(book);
                            mAdapter.setBook(all_books);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    errorTV.setText(parentActivity.getString(R.string.error_missing_fields));
                    errorTV.setVisibility(View.VISIBLE);
                }

            }
        });


        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        selectedGroup = (String)parent.getItemAtPosition(i);
        Log.d("Spinner", "group");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
