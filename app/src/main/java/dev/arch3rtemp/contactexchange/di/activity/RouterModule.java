package dev.arch3rtemp.contactexchange.di.activity;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.router.RouterImpl;

@Module
public interface RouterModule {

    @Binds
    Router bindRouter(RouterImpl impl);
}
