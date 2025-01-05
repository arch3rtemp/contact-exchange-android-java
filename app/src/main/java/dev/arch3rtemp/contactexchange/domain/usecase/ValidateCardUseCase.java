package dev.arch3rtemp.contactexchange.domain.usecase;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.model.Card;

public final class ValidateCardUseCase {

    @Inject
    public ValidateCardUseCase() {}

    public boolean invoke(Card card) {
        return !(card.name().isBlank() &&
                card.job().isBlank() &&
                card.position().isBlank() &&
                card.email().isBlank() &&
                card.phoneMobile().isBlank() &&
                card.phoneOffice().isBlank());
    }
}
