package dev.arch3rtemp.contactexchange.presentation.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.arch3rtemp.contactexchange.TestData;

public class CardToJsonMapperTest {

    private final CardToJsonMapper mapper = new CardToJsonMapper();

    @Test
    public void invokeToJson_returnsGardUi() {
        var card = mapper.toJson(TestData.testMyCardUi);
        assertEquals(TestData.testCardJsonCompact, card);
    }
}
