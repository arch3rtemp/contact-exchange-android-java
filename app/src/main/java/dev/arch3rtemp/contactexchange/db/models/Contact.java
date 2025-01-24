package dev.arch3rtemp.contactexchange.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.util.Locale;

import dev.arch3rtemp.ui.util.TimeConverter;

@Entity(tableName = "contact_table")
public class Contact {
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
    private final long createdAt;
    private final int color;
    @ColumnInfo(name = "is_my")
    private final boolean isMy;

    public Contact(int id, String name, String job, String position, String email, String phoneMobile, String phoneOffice, long createdAt, int color, boolean isMy) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.position = position;
        this.email = email;
        this.phoneMobile = phoneMobile;
        this.phoneOffice = phoneOffice;
        this.createdAt = createdAt;
        this.color = color;
        this.isMy = isMy;
    }

    public Contact(JSONObject jsonData) throws JSONException {
        this(
                0,
                jsonData.getString("name"),
                jsonData.getString("job"),
                jsonData.getString("position"),
                jsonData.getString("email"),
                jsonData.getString("phoneMobile"),
                jsonData.getString("phoneOffice"),
                System.currentTimeMillis(),
                jsonData.getInt("color"),
                false
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getColor() {
        return color;
    }

    public boolean getIsMy() {
        return isMy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact that = (Contact) o;
        return id == that.id &&
                createdAt == that.createdAt &&
                color == that.color &&
                isMy == that.isMy &&
                name.equals(that.name) &&
                job.equals(that.job) &&
                position.equals(that.position) &&
                email.equals(that.email) &&
                phoneMobile.equals(that.phoneMobile) &&
                phoneOffice.equals(that.phoneOffice);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + job.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phoneMobile.hashCode();
        result = 31 * result + phoneOffice.hashCode();
        result = 31 * result + Long.hashCode(createdAt);
        result = 31 * result + Integer.hashCode(color);
        result = 31 * result + Boolean.hashCode(isMy);
        return result;
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

    public String getDateString() {
        return new TimeConverter().convertLongToDateString(createdAt, "dd MMM yy", Locale.getDefault(), ZoneId.systemDefault());
    }
}
