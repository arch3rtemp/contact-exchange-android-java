package dev.arch3rtemp.contactexchange.presentation.ui.card;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.ui.base.UiEffect;
import dev.arch3rtemp.ui.base.UiEvent;
import dev.arch3rtemp.ui.base.UiState;

public interface CardContract {

    sealed interface CardEvent extends UiEvent {
        record OnCardLoad(int id) implements CardEvent {}
        record OnCardDelete(int id) implements CardEvent {}
    }

    sealed interface CardEffect extends UiEffect {
        record ShowError(String message) implements CardEffect {}
        final class AnimateDeletion implements CardEffect {}
    }

    sealed interface CardState extends UiState {
        final class Idle implements CardState {}
        final class Loading implements CardState {}
        final class Error implements CardState {}
        record Success(CardUi card) implements CardState {}
    }
}
