package com.example.contactsexchangejava.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contactsexchangejava.db.models.Contact;

@Database(entities = Contact.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

    private static AppDatabase appDatabase;

    public static AppDatabase getDBInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "contacts_db").build();
        }
        return appDatabase;
    }
}
