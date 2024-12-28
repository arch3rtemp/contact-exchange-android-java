package dev.arch3rtemp.contactexchange.presentation.mapper;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import javax.inject.Inject;

public class JsonToCardMapper {

    @Inject
    public JsonToCardMapper() {}

    public Card fromJson(String rawCard) throws JSONException {
        var cardJsonObj = new JSONObject(rawCard);
        return new Card(
                0,
                cardJsonObj.getString("name"),
                cardJsonObj.getString("job"),
                cardJsonObj.getString("position"),
                cardJsonObj.getString("email"),
                cardJsonObj.getString("phoneMobile"),
                cardJsonObj.getString("phoneOffice"),
                new Date(),
                cardJsonObj.getInt("color"),
                false
        );
    }

    public String toJson(Card card) {
        return new Gson().toJson(card);
    }
}
