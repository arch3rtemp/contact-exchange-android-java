package dev.arch3rtemp.contactexchange.presentation.ui.home;

import java.util.List;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.ui.base.UiEffect;
import dev.arch3rtemp.ui.base.UiEvent;
import dev.arch3rtemp.ui.base.UiState;

public interface HomeContract {

    sealed interface HomeEvent extends UiEvent {
        record OnCardsLoad() implements HomeEvent {}
        record OnContactsLoad() implements HomeEvent {}
        record OnContactDeleted(CardUi card) implements HomeEvent {}
        record OnContactSaved(CardUi card) implements HomeEvent {}
        record OnSearchQuery(String query) implements HomeEvent {}
    }

    sealed interface HomeEffect extends UiEffect {
        record ShowError(String message) implements HomeEffect {}
        record ShowUndo(CardUi card) implements HomeEffect {}
    }

    sealed interface ViewState permits ViewState.Idle, ViewState.Empty, ViewState.Error, ViewState.Loading, ViewState.Success {
        record Idle() implements ViewState {}
        record Empty() implements ViewState {}
        record Loading() implements ViewState {}
        record Error(String message) implements ViewState {}
        record Success(List<CardUi> data) implements ViewState {}
    }

    record HomeState(ViewState myCards, ViewState scannedCards) implements UiState {}
}
