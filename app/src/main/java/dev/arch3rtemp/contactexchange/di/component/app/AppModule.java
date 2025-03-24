package dev.arch3rtemp.contactexchange.di.component.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.App;

@Module(includes = {DbModule.class, RxModule.class})
public class AppModule {

    @Provides
    Context provideAppContext(App app) {
        return app.getApplicationContext();
    }
}
