package dev.arch3rtemp.contactexchange.di.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.di.ActivityScope;
import dev.arch3rtemp.contactexchange.router.Router;

@Module
public class RouterModule {

    @ActivityScope
    @Provides
    public FragmentManager provideFragmentManager(FragmentActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @ActivityScope
    @Provides
    public Router provideRouter(FragmentManager fragmentManager) {
        return new Router(fragmentManager);
    }
}
