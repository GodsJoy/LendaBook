package com.example.android.lendabook;

/**
 * Created by ayomide on 11/13/18.
 * A Borrowed_Book POJO
 */
public class Borrowed_Book {
    private String bookName;
    private String imageUri;
    private String group;
    private String borrowerId;
    private int status; //status 0 means returned, 1 means still borrowed


    public Borrowed_Book(String bookName, String imageUri, String group, String borrowerId, int status) {
        this.bookName = bookName;
        this.imageUri = imageUri;
        this.group = group;
        this.borrowerId = borrowerId;
        this.status = status;
    }

    public Borrowed_Book(){}

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
