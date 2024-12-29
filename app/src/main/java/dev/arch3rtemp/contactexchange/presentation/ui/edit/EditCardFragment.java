package dev.arch3rtemp.contactexchange.presentation.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.contactexchange.databinding.FragmentCardEditBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.ui.base.BaseFragment;

@AndroidEntryPoint
public class EditCardFragment extends BaseFragment<EditCardContract.EditCardEvent, EditCardContract.EditCardEffect, EditCardContract.EditCardState, FragmentCardEditBinding, EditCardPresenter> {

    @Inject
    EditCardPresenter presenter;

    private EditCardFragmentArgs args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        args = EditCardFragmentArgs.fromBundle(getArguments());
    }

    @Override
    protected FragmentCardEditBinding bindLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return FragmentCardEditBinding.inflate(getLayoutInflater());
    }

    @Override
    protected EditCardPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(args.getId()));
        setListeners();
    }

    private void setListeners() {
        getBinding().btnUpdate.setOnClickListener((v -> {
            presenter.setEvent(new EditCardContract.EditCardEvent.OnUpdateButtonPressed(getDataFromFields()));
        }));
    }

    @Override
    protected void renderState(EditCardContract.EditCardState state) {
        if (state instanceof EditCardContract.EditCardState.Success success) {
            showCard(success.card());
        }
    }

    @Override
    protected void renderEffect(EditCardContract.EditCardEffect effect) {
        if (effect instanceof EditCardContract.EditCardEffect.Finish) {
            NavHostFragment.findNavController(this).navigateUp();
        } else if (effect instanceof EditCardContract.EditCardEffect.ShowError showError) {
            Toast.makeText(requireContext(), showError.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showCard(CardUi card) {
        getBinding().etFullName.setText(card.name());
        getBinding().etCompany.setText(card.job());
        getBinding().etPosition.setText(card.position());
        getBinding().etEmail.setText(card.email());
        getBinding().etTel.setText(card.phoneMobile());
        getBinding().etTelOffice.setText(card.phoneOffice());
        getBinding().clEdit.getBackground().setColorFilter(card.getSrcInColorFilter());
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
                -1,
                -1,
                true
        );
    }
}
