package com.example.contactsexchangejava.ui.card;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
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
import com.example.contactsexchangejava.constants.IsMe;
import com.example.contactsexchangejava.db.models.Contact;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.zxing.WriterException;

import java.time.Clock;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class CardFragment extends Fragment implements ICardContract.View {

    View view;
    private ConstraintLayout clCard;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private int isMe;
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
    Contact card;
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
        if (isMe == IsMe.NOT_ME) {
            clEdit.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        clCard = view.findViewById(R.id.cl_card);
        tvName = view.findViewById(R.id.tv_name);
        tvPosition = view.findViewById(R.id.tv_position);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhoneMobile = view.findViewById(R.id.tv_phone_mobile);
        tvPhoneOffice = view.findViewById(R.id.tv_phone_office);
        clEdit = view.findViewById(R.id.cl_edit);
        clDelete = view.findViewById(R.id.cl_delete);
        ivQr = view.findViewById(R.id.iv_qr);
        setPresenter(new CardPresenter(this));
        presenter.onViewCreated(getActivity());
        isMe = Objects.requireNonNull(getArguments()).getInt("isMe", 0);
        id = getArguments().getInt("id", -1);
        getCard(id);
    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            createEditFragment();
        });
        clDelete.setOnClickListener(this::createDeleteDialog);

    }

    private void getCard(int id) {
        presenter.getContactById(id);
    }

    @Override
    public void onCardLoaded(Contact card) {
        this.card = card;
        setCardData();
        generateQr();
    }



    private void createEditFragment() {
        CreateOrEditCardFragment editCardFragment = new CreateOrEditCardFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCreate", false);
        bundle.putInt("id", id);
        editCardFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.card_scale_up, 0, 0, R.anim.card_scale_down)
                .replace(R.id.fl_main_frame_container, editCardFragment)
                .addToBackStack(null)
                .commit();
    }

    private void setCardData() {
        Drawable cardBackground = clCard.getBackground();
        presenter.setBackgroundColorAndRetainShape(card.getColor(), cardBackground);
        if (card.getLastName().equals("N/A"))
            tvName.setText(card.getFirstName());
        else
            tvName.setText(String.format("%s %s", card.getFirstName(), card.getLastName()));
        tvPosition.setText(card.getPosition());
        tvEmail.setText(card.getEmail());
        tvPhoneMobile.setText(card.getPhoneMobile());
        tvPhoneOffice.setText(card.getPhoneOffice());
    }

    private void generateQr() {
        WindowManager manager = (WindowManager) Objects.requireNonNull(getActivity()).getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
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
            cardDeletionAnimation();

        });

        cancel.setOnClickListener(c -> deleteDialog.dismiss());
    }

    private void cardDeletionAnimation() {
        ObjectAnimator moveY = ObjectAnimator.ofFloat(clCard, View.Y, clCard.getY(), clCard.getY()+700 );
        ObjectAnimator moveX = ObjectAnimator.ofFloat(clCard, View.X, clCard.getX(), clCard.getX()+0 );

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clCard, View.SCALE_Y, 1f, 0.2f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clCard, View.SCALE_X, 1f, 0.2f);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(clCard, View.ALPHA, 1f,0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                createDeletedFragment();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.setDuration(1000);
        animatorSet.play(moveX).with(moveY).with(scaleX).with(scaleY).with(alpha);
        animatorSet.start();
    }

    private void createDeletedFragment() {
        DeletedFragment deletedFragment = new DeletedFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, 0)
                .replace(R.id.fl_main_frame_container, deletedFragment)
                .commit();
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