package dev.arch3rtemp.contactexchange.presentation.ui;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.ui.base.UiEffect;
import dev.arch3rtemp.ui.base.UiEvent;
import dev.arch3rtemp.ui.base.UiState;

public interface MainContract {

    sealed interface MainEvent extends UiEvent permits MainEvent.OnQrScanComplete, MainEvent.OnQrScanCanceled, MainEvent.OnQrScanFail, MainEvent.OnJsonParseFail {

        record OnQrScanComplete(Card card) implements MainEvent {}

        record OnQrScanCanceled(String message) implements MainEvent {}

        record OnQrScanFail(String message) implements MainEvent {}

        record OnJsonParseFail(String message) implements MainEvent {}
    }

    sealed interface MainEffect extends UiEffect permits MainEffect.ShowMessage {
        record ShowMessage(String message) implements MainEffect {}
    }

    final class MainState implements UiState {}
}
