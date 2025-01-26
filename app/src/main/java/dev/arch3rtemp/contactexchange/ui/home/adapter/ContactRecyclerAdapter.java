package dev.arch3rtemp.contactexchange.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.contactexchange.ui.home.adapter.holder.CardHolder;
import dev.arch3rtemp.contactexchange.ui.home.adapter.holder.CommonViewHolder;
import dev.arch3rtemp.contactexchange.ui.home.adapter.holder.ContactHolder;
import dev.arch3rtemp.contactexchange.ui.home.adapter.listener.IContactClickListener;
import dev.arch3rtemp.contactexchange.ui.home.adapter.listener.IDeleteClickListener;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    public IContactClickListener clickListener;
    public IDeleteClickListener deleteListener;
    private final List<Contact> contacts = new ArrayList<>();
    private final int MY_CARD = 197;
    private final int SCANNED_CONTACT = 198;

    public ContactRecyclerAdapter() {}

    @Override
    public int getItemViewType(int position) {
        if (contacts.get(position).getIsMy()) {
            return MY_CARD;
        } else {
            return SCANNED_CONTACT;
        }
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return switch (viewType) {
            case MY_CARD -> new CardHolder(inflater.inflate(R.layout.card_list_item, parent, false), clickListener);
            case SCANNED_CONTACT -> new ContactHolder(inflater.inflate(R.layout.contact_list_item, parent, false), clickListener, deleteListener);
            default -> throw new IllegalStateException("Unexpected value: " + viewType);
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.setData(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateItems(List<Contact> contacts) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ContactDiffCallback(this.contacts, contacts));
        this.contacts.clear();
        this.contacts.addAll(contacts);
        diffResult.dispatchUpdatesTo(this);
    }

    public void clearAdapter() {
        contacts.clear();
    }

    public void removeItem(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Contact contact, int position) {
        contacts.add(position, contact);
        notifyItemInserted(position);
    }

    public void setContactClickListener(IContactClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDeleteClickListener(IDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }
}
