package dev.arch3rtemp.contactexchange.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactHolder> implements Filterable {

    private final List<Contact> contacts = new ArrayList<>();
    private final List<Contact> contactsFull = new ArrayList<>();
    private boolean isMy = false;
    private final int MY_CARD = 197;
    private final int SCANNED_CONTACT = 198;

    public ContactRecyclerAdapter(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        contactsFull.addAll(contacts);
    }

    @Override
    public int getItemViewType(int position) {
        if (contacts.get(position).getIsMy()) {
            isMy = true;
            return MY_CARD;
        } else {
            isMy = false;
            return SCANNED_CONTACT;
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = switch (viewType) {
            case MY_CARD -> inflater.inflate(R.layout.card_list_item, parent, false);
            case SCANNED_CONTACT -> inflater.inflate(R.layout.contact_list_item, parent, false);
            default -> throw new IllegalStateException("Unexpected value: " + viewType);
        };

        return new ContactHolder(view, isMy, clickListener, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.setData(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addContacts(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        contacts.clear();
    }

    public void removeItem(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Contact contact, int position) {
        contacts.add(position, contact);
        notifyItemInserted(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredContacts = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredContacts.addAll(contactsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Contact row : contactsFull) {
                        if (row.getName().toLowerCase().contains(filterPattern) || row.getName().toLowerCase().contains(filterPattern)) {
                            filteredContacts.add(row);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredContacts;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contacts.clear();
                contacts.addAll((List<Contact>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setContactClickListener(IContactClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDeleteClickListener(IDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public IContactClickListener clickListener;
    public interface IContactClickListener {
        void onContactClicked(Contact contact, int contactPosition);
    }

    public IDeleteClickListener deleteListener;
    public interface IDeleteClickListener {
        void onDeleteClicked(Contact contact, int contactPosition);
    }
}
