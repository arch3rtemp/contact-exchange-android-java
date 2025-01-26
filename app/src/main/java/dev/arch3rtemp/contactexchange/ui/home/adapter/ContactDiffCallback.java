package dev.arch3rtemp.contactexchange.ui.home.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;

public class ContactDiffCallback extends DiffUtil.Callback {
    private final List<Contact> oldList;
    private final List<Contact> newList;
    public ContactDiffCallback(List<Contact> oldList, List<Contact> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }


    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        var oldContactId = oldList.get(oldItemPosition).getId();
        var newContactId = newList.get(newItemPosition).getId();
        return oldContactId == newContactId;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        var oldContact = oldList.get(oldItemPosition);
        var newContact = newList.get(newItemPosition);
        return oldContact.equals(newContact);
    }
}
