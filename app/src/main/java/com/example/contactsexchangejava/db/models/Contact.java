package com.example.contactsexchangejava.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.contactsexchangejava.constants.IsMe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Iterator;

@Entity(tableName = "contact_table")
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
    private int isMe;

    public Contact(String firstName, String lastName, String job, String position, String email, String phoneMobile, String phoneOffice, int color, int isMe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.position = position;
        this.email = email;
        this.phoneMobile = phoneMobile;
        this.phoneOffice = phoneOffice;
        this.color = color;
        this.isMe = isMe;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            createDate = today.format(DateTimeFormatter.ofPattern("dd MMM"));
        } else {
            Calendar date = Calendar.getInstance();
            createDate = date.toString();
        }
    }

    public Contact(JSONObject jsonData) throws JSONException {
        this(jsonData.getString("firstName"),
                jsonData.getString("lastName"),
                jsonData.getString("job"),
                jsonData.getString("position"),
                jsonData.getString("email"),
                jsonData.getString("phoneMobile"),
                jsonData.getString("phoneOffice"),
                jsonData.getInt("color"),
                IsMe.NOT_ME);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIsMe(int isMe) {
        this.isMe = isMe;
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

    public int getIsMe() {
        return isMe;
    }

    @Override
    public String toString() {
//        return "Contact{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", job='" + job + '\'' +
//                ", position='" + position + '\'' +
//                ", email='" + email + '\'' +
//                ", phoneMobile='" + phoneMobile + '\'' +
//                ", phoneOffice='" + phoneOffice + '\'' +
//                ", createDate='" + createDate + '\'' +
//                ", color=" + color +
//                ", isMe=" + isMe +
//                '}';

        return new Gson().toJson(this);
    }
}
