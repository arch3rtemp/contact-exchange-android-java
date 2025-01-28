package dev.arch3rtemp.contactexchange.di.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.di.AppScope;

@Module(includes = {DbModule.class})
public class AppModule {

    @AppScope
    @Provides
    Context provideAppContext(App app) {
        return app.getApplicationContext();
    }
}
