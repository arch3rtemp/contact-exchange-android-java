package dev.arch3rtemp.contactexchange.ui.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.ui.util.TimeConverter;

public class ContactToUiMapperTest {

    private final ContactToUiMapper mapper = new ContactToUiMapper(new TimeConverter());

    @Test
    public void invokeFromUiModel_returnsCard() {
        var card = mapper.fromUi(TestData.testMyContactUi);
        assertEquals(TestData.testMyContact, card);
    }

    @Test
    public void invokeToUiModel_returnsCardUi() {
        var cardUi = mapper.toUi(TestData.testMyContact);
        assertEquals(TestData.testMyContactUi, cardUi);
    }

    @Test
    public void invokeFromUiModelList_returnsCardList() {
        var cardList = mapper.fromUiList(TestData.testContactsUi);
        assertEquals(TestData.testContacts, cardList);
    }

    @Test
    public void invokeToUiModelList_returnsCardUiList() {
        var cardUiList = mapper.toUiList(TestData.testContacts);
        assertEquals(TestData.testContactsUi, cardUiList);
    }
}
