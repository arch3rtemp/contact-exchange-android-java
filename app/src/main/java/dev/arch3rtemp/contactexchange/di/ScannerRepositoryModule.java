package dev.arch3rtemp.contactexchange.di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;
import dev.arch3rtemp.contactexchange.data.repository.ScannerRepositoryImpl;
import dev.arch3rtemp.contactexchange.domain.repository.ScannerRepository;

@Module
@InstallIn(ActivityComponent.class)
public interface ScannerRepositoryModule {

    @Binds
    @ActivityScoped
    ScannerRepository bindRepository(ScannerRepositoryImpl impl);
}
