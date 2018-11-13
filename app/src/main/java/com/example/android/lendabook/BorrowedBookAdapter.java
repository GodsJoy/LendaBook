package com.example.android.lendabook;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayomide on 11/13/18.
 * Adapter to display Borrowed books details in BorrowedBookFragment
 */
public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.BorrowedBookListViewHolder> {
    private Context mContext;
    private ArrayList<Borrowed_Book> borrowed_books;

    public BorrowedBookAdapter(Context context){
        mContext = context;
    }

    @Override
    public BorrowedBookListViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Log.d("Book len", " book len");
        int layoutID = R.layout.a_borrowed_book;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new BorrowedBookListViewHolder(view);
    }

    public class BorrowedBookListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.abookImage)
        ImageView bookImage;

        @BindView(R.id.abookName)
        TextView bookName;

        @BindView(R.id.agroup)
        TextView group;

        @BindView(R.id.return_BTN)
        Button returnBtn;

        private BorrowedBookListViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public void onBindViewHolder(BorrowedBookListViewHolder holder, final int position) {
        final Borrowed_Book bb = borrowed_books.get(position);
        Picasso.with(mContext).load(bb.getImageUri()).into(holder.bookImage);
        //Log.d("BookName", aBook.getName()+" "+aBook.getGroup());
        holder.bookName.setText(bb.getBookName());
        holder.group.setText(bb.getGroup());
        holder.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //change status to 0 and refresh view since book has been returned
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = firebaseDatabase.getReference().child("borrowed-book");
                DatabaseReference updatedBRef = dbRef.child(bb.getBookName()+bb.getBorrowerId());
                Map<String, Object> status = new HashMap<>();
                status.put("status", 0);
                updatedBRef.updateChildren(status);

                SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref),
                        Context.MODE_PRIVATE);

                //remove returned book from list and reset books
                borrowed_books.remove(position);
                setBook(borrowed_books);

                //decrement no of books borrowed
                int no_books_borrowed = pref.getInt(mContext.getString(R.string.no_books_borrowed), MainActivity.default_no_pref);
                if(no_books_borrowed > 0) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt(mContext.getString(R.string.no_books_borrowed), no_books_borrowed-1);
                    editor.commit();
                }
                BookUpdateService.startActionUpdateWidget(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Log.d("Book len", books.size()+" book len");
        if(borrowed_books == null)
            return 0;
        else
            return borrowed_books.size();
    }

    public void setBook(ArrayList<Borrowed_Book> books){
        this.borrowed_books = books;
        notifyDataSetChanged();
    }

}
