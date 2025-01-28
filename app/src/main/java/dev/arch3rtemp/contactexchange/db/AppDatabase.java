package dev.arch3rtemp.contactexchange.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import dev.arch3rtemp.contactexchange.db.models.Contact;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
}
