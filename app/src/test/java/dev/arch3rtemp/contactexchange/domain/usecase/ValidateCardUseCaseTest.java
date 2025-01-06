package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.model.Card;

public class ValidateCardUseCaseTest {

    private ValidateCardUseCase validateCard;

    @Before
    public void setup() {
        validateCard = new ValidateCardUseCase();
    }

    @Test
    public void invokeWithValidCard_returnsTrue() {
        var result = validateCard.invoke(TestData.testMyCard);
        assertTrue(result);
    }

    @Test
    public void invokeWithBlankCard_returnsFalse() {
        var result = validateCard.invoke(new Card(0, "", "", "", "", "", "", -1, -1, false));
        assertFalse(result);
    }
}
