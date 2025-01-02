package dev.arch3rtemp.contactexchange.di;

import dev.arch3rtemp.contactexchange.data.repository.CardRepositoryImpl;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public interface CardRepositoryModule {

    @Binds
    @Singleton
    CardRepository bindRepository(CardRepositoryImpl impl);
}
