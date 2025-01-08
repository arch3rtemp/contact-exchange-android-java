package dev.arch3rtemp.contactexchange.di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;
import dev.arch3rtemp.contactexchange.domain.util.AppSchedulerProvider;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;

@Module
@InstallIn(ActivityComponent.class)
public interface RxModule {

    @Binds
    @ActivityScoped
    SchedulerProvider bindSchedulersProvider(AppSchedulerProvider impl);
}
