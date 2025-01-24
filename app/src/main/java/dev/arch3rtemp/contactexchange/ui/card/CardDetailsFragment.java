package dev.arch3rtemp.contactexchange.ui.card;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.util.ColorUtils;
import dev.arch3rtemp.ui.util.DeviceSizeResolver;

public class CardDetailsFragment extends Fragment implements ICardContract.View {

    private ConstraintLayout clCard;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvEmail;
    private TextView tvPhoneMobile;
    private TextView tvPhoneOffice;
    private ImageView ivQr;
    private ICardContract.Presenter presenter;
    private int id;
    private Contact card;

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

        boolean isMy = requireArguments().getBoolean("isMy", false);
        id = requireArguments().getInt("id", -1);
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
        setPresenter(new CardDetailsPresenter(this));
        presenter.onViewCreated(getActivity());
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
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.card_scale_up, 0, 0, R.anim.card_scale_down)
                .replace(R.id.fl_main_frame_container, editCardFragment)
                .addToBackStack(null)
                .commit();
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
        var size = new DeviceSizeResolver().resolve(requireActivity().getWindowManager());
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
        AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).create();
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
        DeletedFragment deletedFragment = new DeletedFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, 0)
                .replace(R.id.fl_main_frame_container, deletedFragment)
                .commit();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static CardDetailsFragment getInstance() {
        return new CardDetailsFragment();
    }

    @Override
    public void setPresenter(ICardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
