package com.example.contactsexchangejava.ui.card;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.qr.QrActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.WINDOW_SERVICE;

public class CardFragment extends Fragment implements ICardContract.View {

    View view;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private boolean isMe;
    Button delete;
    Button cancel;
    TextView tvName;
    TextView tvPosition;
    TextView tvEmail;
    TextView tvPhoneMobile;
    TextView tvPhoneOffice;
    ICardContract.Presenter presenter;
    AppCompatActivity activity;
    int id;
    ImageView ivQr;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        setListeners();
        if (!isMe) {
            clEdit = view.findViewById(R.id.cl_edit);
            clEdit.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        tvName = view.findViewById(R.id.tv_name);
        tvPosition = view.findViewById(R.id.tv_position);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhoneMobile = view.findViewById(R.id.tv_phone_mobile);
        tvPhoneOffice = view.findViewById(R.id.tv_phone_office);
        clEdit = view.findViewById(R.id.cl_edit);
        clDelete = view.findViewById(R.id.cl_delete);
        ivQr = view.findViewById(R.id.iv_bar_code);
        setPresenter(new CardPresenter(this));
        presenter.onViewCreated(getActivity());
        isMe = getArguments().getBoolean("isMe", false);
        id = getArguments().getInt("id", -1);
        getCard(id);
    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            CreateOrEditCardFragment editCardFragment = new CreateOrEditCardFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isCreate", false);
            bundle.putInt("id", id);
            editCardFragment.setArguments(bundle);
            FragmentTransaction transaction;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_main_frame_container, editCardFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        clDelete.setOnClickListener(v -> createDeleteDialog(v));
    }

    private void getCard(int id) {
        presenter.getContactById(id);
    }

    @Override
    public void onCardLoaded(Contact card) {
        String name = card.getFirstName() + " " + card.getLastName();
        tvName.setText(name);
        tvPosition.setText(card.getPosition());
        tvEmail.setText(card.getEmail());
        tvPhoneMobile.setText(card.getPhoneMobile());
        tvPhoneOffice.setText(card.getPhoneOffice());

        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(card.toString(), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            ivQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("TAG", e.toString());
        }

    }

    private void createDeleteDialog(View v) {
        AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).create();
        final View deletePopup = LayoutInflater.from(getContext()).inflate(R.layout.delete_popup, null, false);

        deleteDialog.setView(deletePopup);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
        delete = deletePopup.findViewById(R.id.btn_delete);
        cancel = deletePopup.findViewById(R.id.btn_cancel);


        delete.setOnClickListener(d -> {
            deleteDialog.dismiss();
            presenter.deleteContact(id);
            createDeletedFragment();
        });

        cancel.setOnClickListener(c -> deleteDialog.dismiss());
    }

    private void createDeletedFragment() {

        DeletedFragment deletedFragment = new DeletedFragment();
        FragmentTransaction transaction;
        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, deletedFragment);
        transaction.commit();
    }

    public static CardFragment getInstance() {
        return new CardFragment();
    }

    @Override
    public void setPresenter(ICardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        view = null;
        presenter.onDestroy();
    }
}