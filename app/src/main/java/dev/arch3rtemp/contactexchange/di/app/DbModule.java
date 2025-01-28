package dev.arch3rtemp.contactexchange.di.app;

import android.content.Context;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.di.AppScope;

@Module
public class DbModule {

    @AppScope
    @Provides
    AppDatabase provideDb(Context context) {

        return Room.databaseBuilder(
                        context,
                        AppDatabase.class,
                        "app_db"
                ).fallbackToDestructiveMigration()
                .build();
    }

    @AppScope
    @Provides
    ContactDao provideContactDao(AppDatabase appDatabase) {
        return appDatabase.contactDao();
    }
}
