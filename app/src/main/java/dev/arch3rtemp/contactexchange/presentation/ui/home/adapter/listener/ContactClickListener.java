package dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.listener;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;

public interface ContactClickListener {
    void onContactClick(int id);
    void onDeleteClick(CardUi card);
}
