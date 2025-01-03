package dev.arch3rtemp.contactexchange.presentation.ui.create;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.ui.base.UiEffect;
import dev.arch3rtemp.ui.base.UiEvent;
import dev.arch3rtemp.ui.base.UiState;

public interface CreateCardContract {

    sealed interface CreateCardEvent extends UiEvent permits CreateCardEvent.OnCreateButtonPressed {
        record OnCreateButtonPressed(Card card) implements CreateCardEvent {}
    }

    sealed interface CreateCardEffect extends UiEffect permits CreateCardEffect.ShowMessage, CreateCardEffect.NavigateUp {
        record ShowMessage(String message) implements CreateCardEffect {}
        final class NavigateUp implements CreateCardEffect {}
    }

    sealed interface CreateCardState extends UiState permits CreateCardState.Idle, CreateCardState.Error, CreateCardState.Success {
        final class Idle implements CreateCardState {}
        final class Error implements CreateCardState {}
        final class Success implements CreateCardState {}
    }
}
