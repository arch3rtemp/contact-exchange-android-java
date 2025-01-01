package dev.arch3rtemp.contactexchange.presentation.ui.create;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.databinding.FragmentCardCreateBinding;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.ui.base.BaseFragment;

@AndroidEntryPoint
public class CreateCardFragment extends BaseFragment<CreateCardContract.CreateCardEvent, CreateCardContract.CreateCardEffect, CreateCardContract.CreateCardState, FragmentCardCreateBinding, CreateCardPresenter> {

    @Inject
    CreateCardPresenter presenter;

    private int currentColor = R.color.light_navy;

    @Override
    protected FragmentCardCreateBinding bindLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return FragmentCardCreateBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CreateCardPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        initCard();
        setListeners();
    }

    @Override
    protected void renderState(CreateCardContract.CreateCardState state) {
    }

    @Override
    protected void renderEffect(CreateCardContract.CreateCardEffect effect) {
        if (effect instanceof CreateCardContract.CreateCardEffect.ShowMessage showMessage) {
            Toast.makeText(requireContext(), showMessage.message(), Toast.LENGTH_SHORT).show();
        } else if (effect instanceof CreateCardContract.CreateCardEffect.NavigateOnSuccess) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private void initCard() {
        getBinding().tvColorLightNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);
    }

    private void setListeners() {
        getBinding().tvColorLightNavy.setOnClickListener((v) -> {
            defaultColorsView();
            getBinding().tvColorLightNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.light_navy);
        });
        getBinding().tvColorAquaMarine.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorAquaMarine.setBackgroundResource(R.drawable.shape_selected_card_color_aqua_marine_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.aqua_marine);
        });
        getBinding().tvColorUglyYellow.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorUglyYellow.setBackgroundResource(R.drawable.shape_selected_card_color_ugly_yellow_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.ugly_yellow);
        });
        getBinding().tvColorShamrockGreen.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorShamrockGreen.setBackgroundResource(R.drawable.shape_selected_card_color_shamrock_green_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.shamrock_green);
        });
        getBinding().tvColorBlackThree.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorBlackThree.setBackgroundResource(R.drawable.shape_selected_card_color_black_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.black_three);
        });
        getBinding().tvColorPumpkin.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorPumpkin.setBackgroundResource(R.drawable.shape_selected_card_color_pumpkin_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.pumpkin);
        });
        getBinding().tvColorDarkishPurple.setOnClickListener((v) ->  {
            defaultColorsView();
            getBinding().tvColorDarkishPurple.setBackgroundResource(R.drawable.shape_selected_card_color_darkish_purple_bg);
            setBackgroundColorWithAnimation(currentColor, R.color.darkish_purple);
        });
        getBinding().btnCreate.setOnClickListener((v) ->  {
            var card = getDataFromFields();
            presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(card));
        });
    }

    private void setBackgroundColorWithAnimation(@ColorRes int start, @ColorRes int end) {
        int startColor = getColor(start);
        int endColor = getColor(end);
        var valueAnimator = ValueAnimator.ofArgb(startColor, endColor);
        valueAnimator
                .setDuration(200)
                .addUpdateListener((color) -> {
                    int animatedColor = Integer.parseInt(color.getAnimatedValue().toString());
                    getBinding().clCreate.getBackground().setColorFilter(new PorterDuffColorFilter(animatedColor, PorterDuff.Mode.SRC_IN));
                });
        currentColor = end;
        valueAnimator.start();
    }

    private int getColor(@ColorRes int resourceColor) {
        return ContextCompat.getColor(requireContext(), resourceColor);
    }

    private void defaultColorsView() {
        getBinding().tvColorLightNavy.setBackgroundResource(R.drawable.shape_default_card_color_light_navy_bg);
        getBinding().tvColorAquaMarine.setBackgroundResource(R.drawable.shape_default_card_color_aqua_marine_bg);
        getBinding().tvColorUglyYellow.setBackgroundResource(R.drawable.shape_default_card_color_ugly_yellow_bg);
        getBinding().tvColorShamrockGreen.setBackgroundResource(R.drawable.shape_default_card_color_shamrock_green_bg);
        getBinding().tvColorBlackThree.setBackgroundResource(R.drawable.shape_default_card_color_black_bg);
        getBinding().tvColorPumpkin.setBackgroundResource(R.drawable.shape_default_card_color_pumpkin_bg);
        getBinding().tvColorDarkishPurple.setBackgroundResource(R.drawable.shape_default_card_color_darkish_purple_bg);
    }

    private Card getDataFromFields() {
        return new Card(
                0,
                Objects.requireNonNull(getBinding().etFullName.getText()).toString(),
                Objects.requireNonNull(getBinding().etCompany.getText()).toString(),
                Objects.requireNonNull(getBinding().etPosition.getText()).toString(),
                Objects.requireNonNull(getBinding().etEmail.getText()).toString(),
                Objects.requireNonNull(getBinding().etTel.getText()).toString(),
                Objects.requireNonNull(getBinding().etTelOffice.getText()).toString(),
                System.currentTimeMillis(),
                getColor(currentColor),
                true
        );
    }
}
