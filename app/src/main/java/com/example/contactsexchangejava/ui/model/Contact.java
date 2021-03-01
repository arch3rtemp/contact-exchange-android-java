package com.example.contactsexchangejava.ui.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Contact implements Serializable {

    private String firstName;
    private String lastName;
    private String job;
    private String position;
    private String email;
    private String phoneFirst;
    private String phoneSecond;
    private String formattedDate;
    private int color;
    private boolean isMe;

    public Contact(String name, String surname, String job, String pos, String email, String phone1, String phone2, int color, boolean me) {
        firstName = name;
        lastName = surname;
        this.job = job;
        position = pos;
        this.email = email;
        phoneFirst = phone1;
        phoneSecond = phone2;
        this.color = color;
        this.isMe = me;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            formattedDate = today.format(DateTimeFormatter.ofPattern("dd MMM"));
        } else {
            Calendar date = Calendar.getInstance();
            formattedDate = date.toString();
        }
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

    public String getPhoneFirst() {
        return phoneFirst;
    }

    public String getPhoneSecond() {
        return phoneSecond;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public int getColor() {
        return color;
    }

    public boolean getMe() { return isMe; }
}
