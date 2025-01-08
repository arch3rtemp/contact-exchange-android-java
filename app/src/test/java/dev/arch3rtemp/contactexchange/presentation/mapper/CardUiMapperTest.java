package dev.arch3rtemp.contactexchange.presentation.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.ui.util.TimeConverter;

public class CardUiMapperTest {

    private final CardUiMapper mapper = new CardUiMapper(new TimeConverter());

    @Test
    public void invokeFromUiModel_returnsCard() {
        var card = mapper.fromUiModel(TestData.testMyCardUi);
        assertEquals(TestData.testMyCard, card);
    }

    @Test
    public void invokeToUiModel_returnsCardUi() {
        var cardUi = mapper.toUiModel(TestData.testMyCard);
        assertEquals(TestData.testMyCardUi, cardUi);
    }

    @Test
    public void invokeFromUiModelList_returnsCardList() {
        var cardList = mapper.fromUiModelList(TestData.testCardsUi);
        assertEquals(TestData.testCards, cardList);
    }

    @Test
    public void invokeToUiModelList_returnsCardUiList() {
        var cardUiList = mapper.toUiModelList(TestData.testCards);
        assertEquals(TestData.testCardsUi, cardUiList);
    }
}
