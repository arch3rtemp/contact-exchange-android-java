package dev.arch3rtemp.contactexchange.presentation.card;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import dev.arch3rtemp.contactexchange.databinding.DeletePopupBinding;
import dev.arch3rtemp.contactexchange.databinding.FragmentCardBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.mapper.JsonToCardMapper;

import dev.arch3rtemp.ui.util.DeviceSizeResolver;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.ui.base.BaseFragment;

import javax.inject.Inject;

@AndroidEntryPoint
public class CardFragment extends BaseFragment<CardContract.CardEvent, CardContract.CardEffect, CardContract.CardState, FragmentCardBinding, CardPresenter> {

    @Inject
    CardPresenter presenter;

    private CardFragmentArgs args;

    @Inject
    JsonToCardMapper mapper;

    @Inject
    DeviceSizeResolver sizeResolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        args = CardFragmentArgs.fromBundle(getArguments());
    }

    @Override
    protected FragmentCardBinding bindLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return FragmentCardBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CardPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        setListeners();
    }

    @Override
    protected void renderState(CardContract.CardState state) {
        if (state instanceof CardContract.CardState.Idle) {
            presenter.setEvent(new CardContract.CardEvent.OnCardLoad(args.getId()));
        } else if (state instanceof CardContract.CardState.Loading) {
            showCardLoading();
        } else if (state instanceof CardContract.CardState.Error) {
            showCardError();
        } else if (state instanceof CardContract.CardState.Success data) {
            var card = data.card();
            showCardSuccess(card.isMy());
            setCardData(card);
            generateQr(card);
        }
    }

    @Override
    protected void renderEffect(CardContract.CardEffect effect) {
        if (effect instanceof CardContract.CardEffect.ShowError showError) {
            Toast.makeText(requireContext(), showError.message(), Toast.LENGTH_SHORT).show();
        } else if (effect instanceof CardContract.CardEffect.AnimateDeletion) {
            cardDeletionAnimation();
        }
    }

    private void setListeners() {
        getBinding().clEdit.setOnClickListener(this::goToEditFragment);
        getBinding().clDelete.setOnClickListener(this::createDeleteDialog);
    }

    private void goToEditFragment(View view) {
        NavHostFragment
                .findNavController(this)
                .navigate(CardFragmentDirections.actionCardFragmentToEditCardFragment(args.getId()));
    }

    private void createDeleteDialog(View view) {
        var dialogBinding = DeletePopupBinding.inflate(LayoutInflater.from(requireContext()));
        var dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialogBinding.btnDelete.setOnClickListener((v) -> {
            presenter.setEvent(new CardContract.CardEvent.OnCardDelete(args.getId()));
            dialog.dismiss();
        });

        dialogBinding.btnCancel.setOnClickListener((v) -> dialog.dismiss());
    }

    private void setCardData(Card card) {
        getBinding().tvName.setText(card.name());
        getBinding().tvPosition.setText(card.position());
        getBinding().tvEmail.setText(card.email());
        getBinding().tvPhoneMobile.setText(card.phoneMobile());
        getBinding().tvPhoneOffice.setText(card.phoneOffice());
        getBinding().clCard.getBackground().setColorFilter(new PorterDuffColorFilter(card.color(), PorterDuff.Mode.SRC_IN));
    }

    private void generateQr(Card card) {
        var size = sizeResolver.resolve(requireActivity().getWindowManager());

        int smallerDimension = Math.min(size.first, size.second);
        smallerDimension = smallerDimension * 3 / 4;

        var qrgEncoder = new QRGEncoder(mapper.toJson(card), null, QRGContents.Type.TEXT, smallerDimension);
        // Getting QR-Code as Bitmap
        var bitmap = qrgEncoder.getBitmap(0);
        // Setting Bitmap to ImageView
        getBinding().ivQr.setImageBitmap(bitmap);
    }

    private void cardDeletionAnimation() {
        var moveX = ObjectAnimator.ofFloat(getBinding().clCard, View.X, getBinding().clCard.getX(), getBinding().clCard.getX() + 0);
        var moveY = ObjectAnimator.ofFloat(getBinding().clCard, View.Y, getBinding().clCard.getY(), getBinding().clCard.getY() + 700);

        var scaleX = ObjectAnimator.ofFloat(getBinding().clCard, View.SCALE_X, 1f, 0.2f);
        var scaleY = ObjectAnimator.ofFloat(getBinding().clCard, View.SCALE_Y, 1f, 0.2f);

        var alpha = ObjectAnimator.ofFloat(getBinding().clCard, View.ALPHA, 1f, 0f);

        var animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                showCardDeleteSuccess();
                checkedSignAnimation();
            }
        });
        animatorSet.setDuration(1000);
        animatorSet.play(moveX).with(moveY).with(scaleX).with(scaleY).with(alpha);
        animatorSet.start();
    }

    private void checkedSignAnimation() {
        getBinding().llDeleted.setClipChildren(false);
        getBinding().llDeleted.setClipToPadding(false);

        var moveX = ObjectAnimator.ofFloat(getBinding().llDeleted, View.TRANSLATION_X, 200.0f, 0.0f);
        moveX.setDuration(400).setInterpolator(new DecelerateInterpolator());

        var scaleUpX = ObjectAnimator.ofFloat(getBinding().ivChecked, View.SCALE_X, 1f, 1.1f);
        scaleUpX.setDuration(300).setStartDelay(800);

        var scaleUpY = ObjectAnimator.ofFloat(getBinding().ivChecked, View.SCALE_Y, 1f, 1.1f);
        scaleUpY.setDuration(300).setStartDelay(800);

        var scaleDownX = ObjectAnimator.ofFloat(getBinding().ivChecked, View.SCALE_X, 1.1f, 1.0f);
        scaleDownX.setDuration(300).setStartDelay(1700);

        var scaleDownY = ObjectAnimator.ofFloat(getBinding().ivChecked, View.SCALE_Y, 1.1f, 1.0f);
        scaleDownY.setDuration(300).setStartDelay(1700);

        var animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    NavHostFragment.findNavController(CardFragment.this).navigateUp();
                }, 1000);
            }
        });
        animatorSet.play(scaleUpX).with(scaleUpY).with(scaleDownX).with(scaleDownY).with(moveX);
        animatorSet.start();
    }

    private void showCardDeleteSuccess() {
        getBinding().clButtons.setVisibility(View.INVISIBLE);
        getBinding().progressCircularCard.setVisibility(View.INVISIBLE);
        getBinding().ivCardError.setVisibility(View.INVISIBLE);
        getBinding().llDeleted.setVisibility(View.VISIBLE);
    }

    private void showCardLoading() {
        getBinding().progressCircularCard.setVisibility(View.VISIBLE);
        getBinding().clButtons.setVisibility(View.INVISIBLE);
        getBinding().ivCardError.setVisibility(View.INVISIBLE);
    }

    private void showCardError() {
        getBinding().ivCardError.setVisibility(View.VISIBLE);
        getBinding().clButtons.setVisibility(View.INVISIBLE);
        getBinding().progressCircularCard.setVisibility(View.INVISIBLE);
    }

    private void showCardSuccess(boolean isMy) {
        getBinding().clButtons.setVisibility(View.VISIBLE);
        getBinding().progressCircularCard.setVisibility(View.INVISIBLE);
        getBinding().ivCardError.setVisibility(View.INVISIBLE);
        if (isMy) getBinding().clEdit.setVisibility(View.VISIBLE);
    }
}
