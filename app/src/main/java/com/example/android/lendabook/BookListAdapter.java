package com.example.android.lendabook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayomide on 11/10/18.
 * Adapter to display book list in MyBookFragment
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    private Context mContext;

    private ArrayList<Book> books;

    public BookListAdapter(Context context){
        mContext = context;
    }

    @Override
    public BookListViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Log.d("Book len", " book len");
        int layoutID = R.layout.a_book;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new BookListViewHolder(view);
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.abookImage)
        ImageView bookImage;

        @BindView(R.id.abookName)
        TextView bookName;

        @BindView(R.id.agroup)
        TextView group;

        private BookListViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public void onBindViewHolder(BookListViewHolder holder, int position) {
        Book aBook = books.get(position);
        Picasso.with(mContext).load(aBook.getImageUri()).into(holder.bookImage);
        //Log.d("BookName", aBook.getName()+" "+aBook.getGroup());
        holder.bookName.setText(aBook.getName());
        holder.group.setText(aBook.getGroup());
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
