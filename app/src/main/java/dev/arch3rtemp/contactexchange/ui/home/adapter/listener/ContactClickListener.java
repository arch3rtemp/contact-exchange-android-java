package dev.arch3rtemp.contactexchange.ui.home.adapter.listener;

import dev.arch3rtemp.contactexchange.db.models.Contact;

public interface ContactClickListener {
    void onContactClick(Contact contact, int contactPosition);
}
