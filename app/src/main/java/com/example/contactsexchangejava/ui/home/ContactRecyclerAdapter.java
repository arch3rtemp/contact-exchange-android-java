package com.example.contactsexchangejava.ui.home;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.constants.IsMe;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactHolder> implements Filterable {

    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> contactsFull = new ArrayList<>();
    private int isMe = 0;

    public ContactRecyclerAdapter(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        contactsFull.addAll(contacts);
    }

    @Override
    public int getItemViewType(int position) {
        if (contacts.get(position).getIsMe() == IsMe.ME) {
            isMe = IsMe.ME;
            return IsMe.ME;
        }
        else if (contacts.get(position).getIsMe() == IsMe.NOT_ME) {
            isMe = IsMe.NOT_ME;
            return IsMe.NOT_ME;
        }
        else
            return 0;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case IsMe.ME:
                view = inflater.inflate(R.layout.card_list_item, parent, false);
                break;
            case IsMe.NOT_ME:
                view = inflater.inflate(R.layout.contact_list_item, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return new ContactHolder(view);
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
                        if (row.getLastName().toLowerCase().contains(filterPattern) || row.getFirstName().toLowerCase().contains(filterPattern)) {
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

    class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvCard;
        private TextView tvInitials;
        private LinearLayout llInitials;
        private TextView tvName;
        private TextView tvPosition;
        private TextView tvAddDate;
        private LinearLayout llDelete;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout llItemRoot;


        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            if (isMe == IsMe.ME) {
                tvCard = itemView.findViewById(R.id.tv_card);
                return;
            }
            llItemRoot = itemView.findViewById(R.id.ll_item_root);
            tvInitials = itemView.findViewById(R.id.tv_contact_initials);
            llInitials = itemView.findViewById(R.id.ll_qr_card);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvPosition = itemView.findViewById(R.id.tv_contact_position);
            tvAddDate = itemView.findViewById(R.id.tv_contact_add_date);
            llDelete = itemView.findViewById(R.id.ll_delete);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);

        }

        public void setData(Contact contact) {
            if (isMe == IsMe.ME) {
                tvCard.setText(contact.getJob());
                Drawable background = tvCard.getBackground();
                setBackgroundColorAndRetainShape(contact.getColor(), background);
                tvCard.setOnClickListener(v -> clickListener.onContactClicked(contact, getAdapterPosition()));
            }

            else {
                if (contact.getLastName().equals("N/A")) {
                    tvName.setText(contact.getFirstName());
                    tvInitials.setText(contact.getFirstName().substring(0, 1));
                } else {
                    tvName.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
                    tvInitials.setText(String.format("%s%s", contact.getFirstName().substring(0, 1), contact.getLastName().substring(0, 1)));
                }
                tvPosition.setText(contact.getPosition());
                tvAddDate.setText(contact.getCreateDate());
                llItemRoot.setOnClickListener(v -> clickListener.onContactClicked(contact, getAdapterPosition()));
                llDelete.setOnClickListener(v -> deleteListener.onDeleteClicked(contact, getAdapterPosition()));
                Drawable background = llInitials.getBackground();
                setBackgroundColorAndRetainShape(contact.getColor(), background);
            }
        }

        private void setBackgroundColorAndRetainShape(final int color, final Drawable background) {
            if (background instanceof ShapeDrawable)
                ((ShapeDrawable) background.mutate()).getPaint().setColor(color);
            else if (background instanceof GradientDrawable)
                ((GradientDrawable) background.mutate()).setColor(color);
            else if (background instanceof ColorDrawable)
                ((ColorDrawable) background).setColor(color);
            else
                Log.w("TAG", "Not a valid background type");
        }
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
