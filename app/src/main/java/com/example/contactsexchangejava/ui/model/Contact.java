package com.example.contactsexchangejava.ui.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Entity
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String job;

    private String position;

    private String email;

    @ColumnInfo(name = "phone_mobile")
    private String phoneMobile;

    @ColumnInfo(name = "phone_office")
    private String phoneOffice;

    @ColumnInfo(name = "create_date")
    private String createDate;

    private int color;

    @ColumnInfo(name = "is_me")
    private boolean isMe;

    public Contact(String name, String surname, String job, String pos, String email, String phone1, String phone2, int color, boolean me) {
        firstName = name;
        lastName = surname;
        this.job = job;
        position = pos;
        this.email = email;
        phoneMobile = phone1;
        phoneOffice = phone2;
        this.color = color;
        this.isMe = me;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            createDate = today.format(DateTimeFormatter.ofPattern("dd MMM"));
        } else {
            Calendar date = Calendar.getInstance();
            createDate = date.toString();
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJob() {
        return job;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public String getCreateDate() {
        return createDate;
    }

    public int getColor() {
        return color;
    }

    public boolean getMe() { return isMe; }
}
