package dev.arch3rtemp.contactexchange.ui.home.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;

public class ContactDiffCallback extends DiffUtil.Callback {
    private final List<ContactUi> oldList;
    private final List<ContactUi> newList;
    public ContactDiffCallback(List<ContactUi> oldList, List<ContactUi> newList) {
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
        var oldContactId = oldList.get(oldItemPosition).id();
        var newContactId = newList.get(newItemPosition).id();
        return oldContactId == newContactId;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        var oldContact = oldList.get(oldItemPosition);
        var newContact = newList.get(newItemPosition);
        return oldContact.equals(newContact);
    }
}
