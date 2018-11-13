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
 */
public class BorrowedBooksFragment extends Fragment {
    @BindView(R.id.borrowed_book_listRV)
    RecyclerView recyclerView;
    private BorrowedBookAdapter adapter;
    private ArrayList<Borrowed_Book> borrowed_books = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbRef;
    private UserAccountActivity mParent;


    public BorrowedBooksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.borrowed_books, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new BorrowedBookAdapter(mParent);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mParent, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        mParent = (UserAccountActivity) getActivity();
        SharedPreferences pref = mParent.getSharedPreferences(getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        String userId = pref.getString(getString(R.string.existing_user_field),
                MainActivity.default_saved_user_pref);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference().child("borrowed-book");
        dbRef.orderByChild("borrowerId").equalTo(userId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Borrowed_Book aBook = dataSnapshot.getValue(Borrowed_Book.class);
                        if (aBook.getStatus() == 1) {
                            borrowed_books.add(aBook);
                            adapter.setBook(borrowed_books);
                        }
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
        return rootView;

    }

    public void setParentActivity(UserAccountActivity parent){
        mParent = parent;
    }
}
