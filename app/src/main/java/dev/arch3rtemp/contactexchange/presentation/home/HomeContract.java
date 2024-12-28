package dev.arch3rtemp.contactexchange.presentation.home;

import dev.arch3rtemp.contactexchange.domain.model.Card;

import java.util.List;

import dev.arch3rtemp.ui.base.UiEffect;
import dev.arch3rtemp.ui.base.UiEvent;
import dev.arch3rtemp.ui.base.UiState;

public interface HomeContract {

    sealed interface HomeEvent extends UiEvent {
        final class OnCardsLoad implements HomeEvent {}
        final class OnContactsLoad implements HomeEvent {}
        record OnContactDeleted(Card card) implements HomeEvent {}
        record OnContactSaved(Card card) implements HomeEvent {}
        record OnSearchTyped(String query) implements HomeEvent {}
    }

    sealed interface HomeEffect extends UiEffect {
        record ShowError(String message) implements HomeEffect {}
        record ShowUndo(Card card) implements HomeEffect {}
    }

    sealed interface ViewState permits ViewState.Idle, ViewState.Empty, ViewState.Error, ViewState.Loading, ViewState.Success {
        final class Idle implements ViewState {}
        final class Empty implements ViewState {}
        final class Loading implements ViewState {}
        record Error(String message) implements ViewState {}
        record Success(List<Card> data) implements ViewState {}
    }

    record HomeState(ViewState myCards, ViewState scannedCards, String query) implements UiState {}
}
