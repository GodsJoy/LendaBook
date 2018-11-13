package com.example.android.lendabook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayomide on 11/4/18.
 * Displays User's books
 */
public class MyBooksFragment extends Fragment {
    private UserAccountActivity mParent;

    @BindView(R.id.book_listRV)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    FirebaseDatabase mFirebaseDatabase;

    ArrayList<Book> allBooks = new ArrayList<>();

    private BookListAdapter adapter;


    public MyBooksFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("MyBooks", "inflating my books");

        View rootView = inflater.inflate(R.layout.my_books_list, container, false);



        ButterKnife.bind(this, rootView);
        adapter = new BookListAdapter(mParent);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mParent, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        //FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        mParent = (UserAccountActivity) getActivity();
        SharedPreferences pref = mParent.getSharedPreferences(getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        String userId = pref.getString(getString(R.string.existing_user_field),
                MainActivity.default_saved_user_pref);
        Log.d("userID", userId);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mFirebaseDatabase.getReference().child("book");
        dbRef.orderByChild("ownerId").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book aBook = dataSnapshot.getValue(Book.class);
                allBooks.add(aBook);
                adapter.setBook(allBooks);
                Log.d("Book name", aBook.getName());
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



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mParent, AddBookActivity.class);
                mParent.startActivity(intent);
            }
        });

        return rootView;

    }

    public void setParentActivity(UserAccountActivity parent){
        mParent = parent;
    }

}
