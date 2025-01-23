package dev.arch3rtemp.contactexchange.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import dev.arch3rtemp.contactexchange.constants.IsMe;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Entity(tableName = "contact_table")
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String job;

    private String position;

    private String email;

    @ColumnInfo(name = "phone_mobile")
    private String phoneMobile;

    @ColumnInfo(name = "phone_office")
    private String phoneOffice;

    @ColumnInfo(name = "created_at")
    private String createdAd;

    private int color;

    @ColumnInfo(name = "is_my")
    private int isMy;

    public Contact(String name, String job, String position, String email, String phoneMobile, String phoneOffice, int color, int isMy) {
        this.name = name;
        this.job = job;
        this.position = position;
        this.email = email;
        this.phoneMobile = phoneMobile;
        this.phoneOffice = phoneOffice;
        this.color = color;
        this.isMy = isMy;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            createdAd = today.format(DateTimeFormatter.ofPattern("dd MMM"));
        } else {
            Calendar date = Calendar.getInstance();
            createdAd = date.toString();
        }
    }

    public Contact(JSONObject jsonData) throws JSONException {
        this(jsonData.getString("name"),
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

    public void setName(String name) {
        this.name = name;
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

    public void setCreatedAd(String createdAd) {
        this.createdAd = createdAd;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIsMy(int isMy) {
        this.isMy = isMy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getCreatedAd() {
        return createdAd;
    }

    public int getColor() {
        return color;
    }

    public int getIsMy() {
        return isMy;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String formatInitials() {
        if (name.contains(" ")) {
            var spaceIndex = name.indexOf(" ") + 1;
            var firstLetter = name.substring(0, 1);
            var secondLetter = name.substring(spaceIndex, spaceIndex + 1);
            return firstLetter + secondLetter;
        } else {
            return name.substring(0, 1);
        }
    }
}
