package dev.arch3rtemp.contactexchange.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class CardEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String job;
    String position;
    String email;
    @ColumnInfo(name = "phone_mobile")
    final String phoneMobile;
    @ColumnInfo(name = "phone_office")
    final String phoneOffice;
    @ColumnInfo(name = "created_at")
    final long createdAt;
    final int color;
    @ColumnInfo(name = "is_my")
    final boolean isMy;

    public CardEntity(int id, String name, String job, String position, String email, String phoneMobile, String phoneOffice, long createdAt, int color, boolean isMy) {
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

    public long getCreatedAt() {
        return createdAt;
    }

    public int getColor() {
        return color;
    }

    public boolean isMy() {
        return isMy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardEntity that = (CardEntity) o;
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

}
