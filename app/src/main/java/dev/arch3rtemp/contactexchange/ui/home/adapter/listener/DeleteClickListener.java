package dev.arch3rtemp.contactexchange.ui.home.adapter.listener;

import dev.arch3rtemp.contactexchange.db.models.Contact;

public interface DeleteClickListener {
    void onDeleteClick(Contact contact, int contactPosition);
}
