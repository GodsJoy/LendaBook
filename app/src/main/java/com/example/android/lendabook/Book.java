package com.example.android.lendabook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ayomide on 11/10/18.
 * A book POJO
 */
public class Book implements Parcelable {
    private String name;
    private String group;
    private String imageUri;
    private String ownerId;
    private String ownerEmail;

    public Book(){}

    public Book(String name, String group, String imageUri, String ownerId, String ownerEmail) {
        this.name = name;
        this.group = group;
        this.imageUri = imageUri;
        this.ownerId = ownerId;
        this.ownerEmail = ownerEmail;
    }

    private Book(Parcel in){
        this.name = in.readString();
        this.group = in.readString();
        this.imageUri = in.readString();
        this.ownerId = in.readString();
        this.ownerEmail = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(group);
        parcel.writeString(imageUri);
        parcel.writeString(ownerId);
        parcel.writeString(ownerEmail);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
