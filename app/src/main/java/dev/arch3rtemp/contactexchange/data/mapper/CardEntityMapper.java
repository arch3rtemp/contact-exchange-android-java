package dev.arch3rtemp.contactexchange.data.mapper;

import dev.arch3rtemp.contactexchange.data.model.CardEntity;
import dev.arch3rtemp.contactexchange.domain.model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class CardEntityMapper {

    @Inject
    public CardEntityMapper() {
    }

    public Card fromEntity(CardEntity entity) {
        return new Card(
                entity.getId(),
                entity.getName(),
                entity.getJob(),
                entity.getPosition(),
                entity.getEmail(),
                entity.getPhoneMobile(),
                entity.getPhoneOffice(),
                entity.getCreateDate(),
                entity.getColor(),
                entity.isMy()
        );
    }

    public CardEntity toEntity(Card card) {
        return new CardEntity(
                card.id(),
                card.name(),
                card.job(),
                card.position(),
                card.email(),
                card.phoneMobile(),
                card.phoneOffice(),
                card.createDate(),
                card.color(),
                card.isMy()
        );
    }

    public List<Card> fromEntityList(List<CardEntity> entities) {
        List<Card> cards = new ArrayList<>();
        for (CardEntity entity : entities) {
            cards.add(fromEntity(entity));
        }
        return Collections.unmodifiableList(cards);
    }

    public List<CardEntity> toEntityList(List<Card> cards) {
        List<CardEntity> entities = new ArrayList<>();
        for (Card card : cards) {
            entities.add(toEntity(card));
        }
        return Collections.unmodifiableList(entities);
    }
}
