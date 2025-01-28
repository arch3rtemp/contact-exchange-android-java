package dev.arch3rtemp.contactexchange;

import android.app.Application;

import dev.arch3rtemp.contactexchange.di.app.AppComponent;
import dev.arch3rtemp.contactexchange.di.app.DaggerAppComponent;

public class App extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Dagger component
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();

        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
