package dev.arch3rtemp.contactexchange.di;

import android.content.Context;

import androidx.room.Room;

import dev.arch3rtemp.contactexchange.data.db.AppDatabase;
import dev.arch3rtemp.contactexchange.data.db.CardDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Singleton
    @Provides
    AppDatabase provideDb(@ApplicationContext Context context) {

        return Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "app_db"
                ).fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    CardDao provideContactDao(AppDatabase appDatabase) {
        return appDatabase.cardDao();
    }
}
