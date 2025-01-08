package dev.arch3rtemp.contactexchange.presentation.mapper;

import com.google.gson.Gson;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;

public class CardToJsonMapper {

    private final Gson gson = new Gson();

    @Inject
    public CardToJsonMapper() {}

    public String toJson(CardUi card) {
        return gson.toJson(card);
    }
}
