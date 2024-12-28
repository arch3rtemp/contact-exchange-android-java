package dev.arch3rtemp.contactexchange.presentation.util;

import dev.arch3rtemp.contactexchange.domain.model.Card;

import javax.inject.Inject;

public class CardBlankChecker {

    @Inject
    public CardBlankChecker() {}

    public boolean check(Card card) {
        return !(card.name().isBlank() && card.job().isBlank() && card.position().isBlank() && card.email().isBlank() && card.phoneMobile().isBlank() && card.phoneOffice().isBlank());
    }
}
