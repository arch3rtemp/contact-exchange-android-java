package dev.arch3rtemp.contactexchange.di.component.activity;

import androidx.fragment.app.FragmentActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import dagger.Module;
import dagger.Provides;

@Module
public class ScannerModule {

    @Provides
    GmsBarcodeScannerOptions provideGmsScannerOptions() {
        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();
    }

    @Provides
    GmsBarcodeScanner provideGmsScanner(FragmentActivity activity, GmsBarcodeScannerOptions options) {
        return GmsBarcodeScanning.getClient(activity, options);
    }
}
