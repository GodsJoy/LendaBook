package com.example.android.lendabook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestBookActivity extends AppCompatActivity {
    public static final int DEFAULT_POS = -1;

    @BindView(R.id.bookImage)
    ImageView bookImage;
    @BindView(R.id.requestBTN)
    Button requestBTN;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final Book aBook = intent.getParcelableExtra(SearchBooksAdapter.BOOK_EXTRA);
        int pos = intent.getIntExtra(SearchBooksAdapter.BOOK_POS, DEFAULT_POS);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if(pos != DEFAULT_POS){
            bookImage.setTransitionName("image"+pos);
            Picasso.with(this).load(aBook.getImageUri()).into(bookImage);
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.move));
            requestBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getSharedPreferences(getString(R.string.shared_pref),
                            Context.MODE_PRIVATE);
                    String userEmail = pref.getString(getString(R.string.existing_email_field),
                            MainActivity.default_saved_user_pref);
                    String userId = pref.getString(getString(R.string.existing_user_field),
                            MainActivity.default_saved_user_pref);

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{aBook.getOwnerEmail()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body, aBook.getName(), userEmail));
                        try {
                            //send email to book owner
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            mDBRef = mFirebaseDatabase.getReference().child("borrowed-book").child(aBook.getName()+userId);
                            mDBRef.setValue(new Borrowed_Book(aBook.getName(), aBook.getImageUri(), aBook.getGroup(), userId, 1));

                            //update no_books_borrowed in shared preference
                            int no_books_borrowed = pref.getInt(getString(R.string.no_books_borrowed), MainActivity.default_no_pref);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt(getString(R.string.no_books_borrowed), no_books_borrowed+1);
                            editor.commit();
                            BookUpdateService.startActionUpdateWidget(RequestBookActivity.this);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Log.d("EmailError", "Email not sent");
                        }
                        //Toast.makeText(RequestBookActivity.this, "got it", Toast.LENGTH_LONG).show();

                    //}
                }
            });


        }
    }
}
