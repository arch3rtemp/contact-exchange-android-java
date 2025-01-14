package dev.arch3rtemp.contactexchange.data.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import dev.arch3rtemp.contactexchange.TestData;

@RunWith(RobolectricTestRunner.class)
public class JsonToCardMapperTest {
    
    private final JsonToCardMapper mapper = new JsonToCardMapper();
    
    @Test
    public void invokeFromJson_withValidData_returnsCard() throws JSONException {
        var card = mapper.fromJson(TestData.testCardJson);
        assertEquals(0, card.id());
        assertEquals(TestData.testMyCard.name(), card.name());
        assertEquals(TestData.testMyCard.job(), card.job());
        assertEquals(TestData.testMyCard.position(), card.position());
        assertEquals(TestData.testMyCard.email(), card.email());
        assertEquals(TestData.testMyCard.phoneMobile(), card.phoneMobile());
        assertEquals(TestData.testMyCard.phoneOffice(), card.phoneOffice());
        assertEquals(TestData.testMyCard.color(), card.color());
        assertFalse(card.isMy());
    }

    @Test
    public void invokeFromJson_withInvalidData_throwsJSONException() {
        assertThrows(JSONException.class, () -> {
            mapper.fromJson("invalid string");
        });
    }
}
