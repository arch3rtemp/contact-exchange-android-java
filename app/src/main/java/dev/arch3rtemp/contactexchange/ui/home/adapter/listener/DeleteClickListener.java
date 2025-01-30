package dev.arch3rtemp.contactexchange.ui.home.adapter.listener;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;

@FunctionalInterface
public interface DeleteClickListener {
    void onDeleteClick(ContactUi contact);
}
