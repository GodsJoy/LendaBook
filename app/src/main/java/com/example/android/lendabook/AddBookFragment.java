package com.example.android.lendabook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ayomide on 11/9/18.
 */
public class AddBookFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.book_nameTV)
    EditText bookName;
    @BindView(R.id.chooseBTN)
    Button chooseBTN;
    @BindView(R.id.uploadBTN)
    Button uploadBTN;
    @BindView(R.id.book_uploadIV)
    ImageView bookImage;
    @BindView(R.id.group_spinner)
    Spinner groupSpinner;
    @BindView(R.id.successTV)
    TextView successTV;
    @BindView(R.id.errorTV)
    TextView errorTV;

    private AddBookActivity parentActivity;
    private ArrayList<String> all_groups = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private static final int RC_PHOTO_PICKER =  2;
    private String userId;
    private String userEmail;
    private Uri selectedImage;
    private String group;
    private InterstitialAd interstitialAd;

    public AddBookFragment(){

    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        ButterKnife.bind(this, rootView);

        parentActivity = (AddBookActivity)getActivity();
        //set visibility to GONE and INVISIBLE till data to display is ready
        bookImage.setVisibility(View.GONE);
        errorTV.setVisibility(View.INVISIBLE);
        successTV.setVisibility(View.INVISIBLE);
        //initialize InterstitialAd
        interstitialAd = new InterstitialAd(parentActivity);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        all_groups.add(parentActivity.getString(R.string.select_a_group));

        //get userId anf userEmail from SharedPreferences
        final SharedPreferences pref = parentActivity.getSharedPreferences(getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        userId = pref.getString(getString(R.string.existing_user_field),
                MainActivity.default_saved_user_pref);
        userEmail = pref.getString(getString(R.string.existing_email_field),
                MainActivity.default_saved_user_pref);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        //get reference to user-group
        DatabaseReference mDBRef = mFirebaseDatabase.getReference().child("user-group");
        mDBRef.orderByChild("user_id").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User_Group group = dataSnapshot.getValue(User_Group.class);
                all_groups.add(group.getGroup_name());
                Log.d("Group", group.getGroup_name());

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_spinner_item, all_groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(adapter);
        groupSpinner.setOnItemSelectedListener(this);

        //Launch image chooser to choose image for book
        chooseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        //upload book image to firebase storage and upload book details into firebase database
        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StorageReference imageRef = mFirebaseStorage.getReference().child("book_images")
                        .child(selectedImage.getLastPathSegment());
                imageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //String downloadUri = imageRef.getDownloadUrl()+"";
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                successTV.setVisibility(View.VISIBLE);
                                successTV.setText(getString(R.string.suc_add_book));

                                String downloadUrl = uri.toString();
                                String name = bookName.getText().toString();
                                DatabaseReference bookRef = mFirebaseDatabase.getReference().child("book").child(name+userId);
                                bookRef.setValue(new Book(name, group, downloadUrl, userId, userEmail));
                                //increment the number of books owned
                                int no_books_owned = pref.getInt(getString(R.string.no_books_owned), MainActivity.default_no_pref);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt(getString(R.string.no_books_owned), no_books_owned+1);
                                editor.commit();
                                BookUpdateService.startActionUpdateWidget(parentActivity);
                                //Load interstitialAd once book is uploaded
                                if (interstitialAd.isLoaded()) {
                                    interstitialAd.show();
                                    Log.d("TAG1", "The interstitial loaded.");
                                } else {
                                    Log.d("TAG1", "The interstitial wasn't loaded yet.");
                                }
                            }

                            //Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                errorTV.setVisibility(View.VISIBLE);
                                errorTV.setText(getString(R.string.error_add_book));
                            }
                        });

                    }
                });

            }
        });

        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            bookImage.setVisibility(View.VISIBLE);
            selectedImage = data.getData();

            String imagePath = selectedImage.toString();

            Picasso.with(parentActivity).load(imagePath).into(bookImage);
            Log.d("Image", imagePath);
            /*uri.setVisibility(View.VISIBLE);
            uri.setText(imagePath);*/

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        group = (String)parent.getItemAtPosition(i);
        Log.d("Spinner", "group");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
