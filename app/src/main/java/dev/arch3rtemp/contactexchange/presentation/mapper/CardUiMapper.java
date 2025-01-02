package dev.arch3rtemp.contactexchange.presentation.mapper;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.ui.util.TimeConverter;

public class CardUiMapper {

    private final TimeConverter timeConverter;

    @Inject
    public CardUiMapper(TimeConverter timeConverter, TimeConverter timeConverter1) {
        this.timeConverter = timeConverter1;
    }

    public Card fromUiModel(CardUi uiModel) {
        return new Card(
                uiModel.id(),
                uiModel.name(),
                uiModel.job(),
                uiModel.position(),
                uiModel.email(),
                uiModel.phoneMobile(),
                uiModel.phoneOffice(),
                uiModel.createdAt(),
                uiModel.color(),
                uiModel.isMy()
        );
    }

    public CardUi toUiModel(Card card) {
        return new CardUi(
                card.id(),
                card.name(),
                card.job(),
                card.position(),
                card.email(),
                card.phoneMobile(),
                card.phoneOffice(),
                card.createdAt(),
                timeConverter.convertLongToDateString(
                        card.createdAt(),
                        DATE_PATTERN,
                        Locale.getDefault(),
                        ZoneId.systemDefault()
                ),
                card.color(),
                card.isMy()
        );
    }

    public List<Card> fromUiModelList(List<CardUi> uiModels) {
        List<Card> cards = new ArrayList<>();
        for (CardUi uiModel : uiModels) {
            cards.add(fromUiModel(uiModel));
        }
        return Collections.unmodifiableList(cards);
    }

    public List<CardUi> toUiModelList(List<Card> cards) {
        List<CardUi> uiModels = new ArrayList<>();
        for (Card card : cards) {
            uiModels.add(toUiModel(card));
        }
        return Collections.unmodifiableList(uiModels);
    }

    private final static String DATE_PATTERN = "dd MMM yy";
}
