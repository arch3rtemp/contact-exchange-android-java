package dev.arch3rtemp.contactexchange.di;

import android.app.Activity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public class ScannerModule {

    @ActivityScoped
    @Provides
    GmsBarcodeScannerOptions provideGmsScannerOptions() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }

    @ActivityScoped
    @Provides
    GmsBarcodeScanner provideGmsScanner(Activity activity, GmsBarcodeScannerOptions options) {
        return GmsBarcodeScanning.getClient(activity, options);
    }
}
