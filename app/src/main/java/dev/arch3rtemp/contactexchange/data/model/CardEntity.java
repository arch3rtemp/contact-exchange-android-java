package dev.arch3rtemp.contactexchange.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "contact_table")
public class CardEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String job;
    String position;
    String email;
    @ColumnInfo(name = "phone_mobile")
    final
    String phoneMobile;
    @ColumnInfo(name = "phone_office")
    final
    String phoneOffice;
    @ColumnInfo(name = "create_date")
    final
    Date createDate;
    final int color;
    @ColumnInfo(name = "is_my")
    final
    boolean isMy;

    public CardEntity(int id, String name, String job, String position, String email, String phoneMobile, String phoneOffice, Date createDate, int color, boolean isMy) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.position = position;
        this.email = email;
        this.phoneMobile = phoneMobile;
        this.phoneOffice = phoneOffice;
        this.createDate = createDate;
        this.color = color;
        this.isMy = isMy;
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

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public int getColor() {
        return color;
    }

    public boolean isMy() {
        return isMy;
    }
}
