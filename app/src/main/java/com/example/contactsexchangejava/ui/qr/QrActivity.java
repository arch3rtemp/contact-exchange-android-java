package com.example.contactsexchangejava.ui.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookDoCoMoResultParser;
import com.google.zxing.client.result.ParsedResult;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, IQrContract.View {

    private ZXingScannerView scannerView;
    private IQrContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initUI();
        presenter.onViewCreated(this);
        startCamera();
    }

    private void initUI() {
        scannerView = findViewById(R.id.sv_qr);
        scannerView.setResultHandler(this);
        setPresenter(new QrPresenter(this));
    }

    private void startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                scannerView.startCamera();
            } else {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 999);
            }
        } else {
            scannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0)
            scannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Contact contact = null;
        try {
            contact = new Contact(new JSONObject(rawResult.getText()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        presenter.createContact(contact);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setPresenter(IQrContract.Presenter presenter) {
        this.presenter = presenter;
    }
}