package dev.arch3rtemp.contactexchange.data.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class DbTypeConverters {

    @TypeConverter
    public Date timestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public long dateToTimestamp(Date date) {
        return date.getTime();
    }
}
