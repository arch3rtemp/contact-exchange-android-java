package dev.arch3rtemp.contactexchange.di.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    public FragmentManager provideFragmentManager(FragmentActivity activity) {
        return activity.getSupportFragmentManager();
    }
}
