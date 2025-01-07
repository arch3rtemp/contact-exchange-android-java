package dev.arch3rtemp.contactexchange.data.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.arch3rtemp.contactexchange.TestData;

public class CardEntityMapperTest {

    private final CardEntityMapper mapper = new CardEntityMapper();

    @Test
    public void invokeFromEntity_returnsCard() {
        var card = mapper.fromEntity(TestData.testMyCardEntity);
        assertEquals(TestData.testMyCard, card);
    }

    @Test
    public void invokeToEntity_returnsCardEntity() {
        var cardEntity = mapper.toEntity(TestData.testMyCard);
        assertEquals(TestData.testMyCardEntity, cardEntity);
    }

    @Test
    public void invokeFromEntityList_returnsCardList() {
        var cardList = mapper.fromEntityList(TestData.testCardsEntity);
        assertEquals(TestData.testCards, cardList);
    }

    @Test
    public void invokeToEntityList_returnsCardEntityList() {
        var cardEntityList = mapper.toEntityList(TestData.testCards);
        assertEquals(TestData.testCardsEntity, cardEntityList);
    }
}
