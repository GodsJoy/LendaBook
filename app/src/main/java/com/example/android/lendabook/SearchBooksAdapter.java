package com.example.android.lendabook;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayomide on 11/12/18.
 * Adapter that displays available books in a recyclerview.
 */
public class SearchBooksAdapter extends RecyclerView.Adapter<SearchBooksAdapter.SearchBookViewHolder> {
    public static final String BOOK_EXTRA = "book_extra";
    public static final String BOOK_POS = "book_position";
    private Context mContext;
    private SearchBookActivity mParent;

    private ArrayList<Book> books;

    public SearchBooksAdapter(Context context, SearchBookActivity parent){
        mContext = context;
        mParent = parent;
    }

    @Override
    public SearchBookViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        int layoutID = R.layout.a_searched_book;
        View view = LayoutInflater.from(mContext)
                .inflate(layoutID, parent, false);
        return new SearchBookViewHolder(view);
    }

    public class SearchBookViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.bookImage)
        ImageView bookImage;

        @BindView(R.id.bookName)
        TextView bookName;

        @BindView(R.id.groupName)
        TextView groupName;

        @BindView(R.id.request_layout)
        LinearLayout requestLayout;

        private SearchBookViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public void onBindViewHolder(final SearchBookViewHolder holder, final int position) {
        final Book aBook = books.get(position);
        Picasso.with(mContext).load(aBook.getImageUri()).into(holder.bookImage);
        holder.bookImage.setTransitionName("image"+position);
        //Log.d("BookName", aBook.getName()+" "+aBook.getGroup());
        holder.bookName.setText(aBook.getName());
        holder.groupName.setText(aBook.getGroup());
        holder.requestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, RequestBookActivity.class);
                intent.putExtra(BOOK_EXTRA, aBook);
                intent.putExtra(BOOK_POS, position);

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        mParent, holder.bookImage, holder.bookImage.getTransitionName()).toBundle();
                mParent.startActivity(intent, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Log.d("Book len", books.size()+" book len");
        if(books == null)
            return 0;
        else
            return books.size();
    }

    public void setBook(ArrayList<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }
}
