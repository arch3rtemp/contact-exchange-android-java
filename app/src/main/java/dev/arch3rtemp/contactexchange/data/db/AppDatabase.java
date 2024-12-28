package dev.arch3rtemp.contactexchange.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dev.arch3rtemp.contactexchange.data.model.CardEntity;

@Database(entities = CardEntity.class, version = 1, exportSchema = false)
@TypeConverters(DbTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CardDao cardDao();
}
