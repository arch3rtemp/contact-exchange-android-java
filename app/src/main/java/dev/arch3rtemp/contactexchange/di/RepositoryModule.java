package dev.arch3rtemp.contactexchange.di;

import dev.arch3rtemp.contactexchange.data.repository.RepositoryImpl;
import dev.arch3rtemp.contactexchange.domain.repository.Repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public interface RepositoryModule {

    @Binds
    @Singleton
    Repository bindRepository(RepositoryImpl impl);
}
