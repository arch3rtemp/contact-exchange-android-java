package dev.arch3rtemp.contactexchange.di.component.app;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.util.AppSchedulerProvider;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;

@Module
public interface RxModule {

    @Binds
    SchedulerProvider bindRxSchedulers(AppSchedulerProvider impl);
}
