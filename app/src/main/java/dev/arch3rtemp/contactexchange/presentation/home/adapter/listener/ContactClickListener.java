package dev.arch3rtemp.contactexchange.presentation.home.adapter.listener;

import dev.arch3rtemp.contactexchange.domain.model.Card;

public interface ContactClickListener {
    void onContactClick(int id);
    void onDeleteClick(Card card);
}
