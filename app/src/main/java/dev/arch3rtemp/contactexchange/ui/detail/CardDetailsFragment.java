package dev.arch3rtemp.contactexchange.ui.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.createoredit.CreateOrEditCardFragment;
import dev.arch3rtemp.contactexchange.ui.result.ResultFragment;
import dev.arch3rtemp.ui.util.ColorUtils;
import dev.arch3rtemp.ui.util.DeviceSizeResolver;

public class CardDetailsFragment extends Fragment implements CardDetailsContract.View {

    @Inject
    CardDetailsContract.Presenter presenter;
    @Inject
    Router router;
    @Inject
    DeviceSizeResolver deviceSizeResolver;
    private int id;
    private Contact card;
    private ConstraintLayout clCard;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvEmail;
    private TextView tvPhoneMobile;
    private TextView tvPhoneOffice;
    private ImageView ivQr;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((App) requireActivity()
                .getApplication())
                .getAppComponent()
                .activityComponent()
                .create(requireActivity())
                .fragmentComponent()
                .create()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initPresenter();
        setListeners();

        boolean isMy = requireArguments().getBoolean(ARG_IS_MY, false);
        id = requireArguments().getInt(ARG_ID, -1);
        presenter.getContactById(id);
        if (!isMy) {
            clEdit.setVisibility(View.GONE);
        }
    }

    private void initUI(View view) {
        clCard = view.findViewById(R.id.cl_card);
        tvName = view.findViewById(R.id.tv_name);
        tvPosition = view.findViewById(R.id.tv_position);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhoneMobile = view.findViewById(R.id.tv_phone_mobile);
        tvPhoneOffice = view.findViewById(R.id.tv_phone_office);
        clEdit = view.findViewById(R.id.cl_edit);
        clDelete = view.findViewById(R.id.cl_delete);
        ivQr = view.findViewById(R.id.iv_qr);
    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            createEditFragment();
        });
        clDelete.setOnClickListener(this::createDeleteDialog);
    }

    private void initPresenter() {
        presenter.onCreate(this);
    }

    @Override
    public void onCardLoaded(Contact card) {
        this.card = card;
        setCardData();
        generateQr();
    }

    private void createEditFragment() {
        router.navigate(CreateOrEditCardFragment.newInstance(id, false), false, true);
    }

    private void setCardData() {
        Drawable cardBackground = clCard.getBackground();
        cardBackground.mutate().setColorFilter(ColorUtils.createSrcInColorFilter(card.getColor()));
        tvName.setText(card.getName());
        tvPosition.setText(card.getPosition());
        tvEmail.setText(card.getEmail());
        tvPhoneMobile.setText(card.getPhoneMobile());
        tvPhoneOffice.setText(card.getPhoneOffice());
    }

    private void generateQr() {
        var size = deviceSizeResolver.resolve(requireActivity().getWindowManager());
        int width = size.first;
        int height = size.second;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(card.toString(), null, QRGContents.Type.TEXT, smallerDimension);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap(0);
        // Setting Bitmap to ImageView
        ivQr.setImageBitmap(bitmap);
    }

    private void createDeleteDialog(View v) {
        var deleteDialog = new AlertDialog.Builder(getContext()).create();
        final View deletePopup = LayoutInflater.from(getContext()).inflate(R.layout.delete_popup, null, false);

        deleteDialog.setView(deletePopup);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
        var delete = deletePopup.findViewById(R.id.btn_delete);
        var cancel = deletePopup.findViewById(R.id.btn_cancel);


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
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                createDeletedFragment();
            }
        });
        animatorSet.setDuration(1000);
        animatorSet.play(moveX).with(moveY).with(scaleX).with(scaleY).with(alpha);
        animatorSet.start();
    }

    private void createDeletedFragment() {
        router.navigate(ResultFragment.newInstance(), false, false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    public static CardDetailsFragment newInstance(int id, boolean isMy) {

        var args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putBoolean(ARG_IS_MY, isMy);
        CardDetailsFragment fragment = new CardDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final String ARG_ID = "id";
    private static final String ARG_IS_MY = "isMy";
}
