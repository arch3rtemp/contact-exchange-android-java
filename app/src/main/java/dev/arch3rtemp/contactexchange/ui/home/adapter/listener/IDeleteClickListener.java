package dev.arch3rtemp.contactexchange.ui.home.adapter.listener;

import dev.arch3rtemp.contactexchange.db.models.Contact;

public interface IDeleteClickListener {
    void onDeleteClick(Contact contact, int contactPosition);
}
