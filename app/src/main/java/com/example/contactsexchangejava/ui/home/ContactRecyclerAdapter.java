package com.example.contactsexchangejava.ui.home;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactHolder> {

    private List<Contact> contacts;
    private final int ME = 0, NOT_ME = 1;
    private boolean isMe = false;

    public ContactRecyclerAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemViewType(int position) {
        if (contacts.get(position).getMe()) {
            isMe = true;
            return ME;
        }
        else if (!contacts.get(position).getMe())
            return NOT_ME;
        else
            return -1;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case ME:
                view = inflater.inflate(R.layout.card_list_item, parent, false);
                break;
            case NOT_ME:
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
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        TextView tvCard;
        TextView tvInitials;
        LinearLayout llInitials;
        TextView tvName;
        TextView tvPosition;
        TextView tvAddDate;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            if (isMe) {
                tvCard = itemView.findViewById(R.id.tv_card);
                return;
            }
            tvInitials = itemView.findViewById(R.id.tv_contact_initials);
            llInitials = itemView.findViewById(R.id.ll_initials);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvPosition = itemView.findViewById(R.id.tv_contact_position);
            tvAddDate = itemView.findViewById(R.id.tv_contact_add_date);
        }

        public void setData(Contact contact) {
            if (isMe) {
                tvCard.setText(contact.getJob());
                Drawable background = tvCard.getBackground();
                setBackgroundColorAndRetainShape(contact.getColor(), background);
                tvCard.setOnClickListener(v -> clickListener.contactClicked(contact, getAdapterPosition()));
                return;
            }

            String name = contact.getFirstName() + " " + contact.getLastName();
            tvName.setText(name);
            String initials = contact.getFirstName().substring(0, 1) + contact.getLastName().substring(0, 1);
            tvInitials.setText(initials);
            tvPosition.setText(contact.getPosition());
            tvAddDate.setText(contact.getCreateDate());
            itemView.setOnClickListener(v -> clickListener.contactClicked(contact, getAdapterPosition()));
            Drawable background = llInitials.getBackground();
            setBackgroundColorAndRetainShape(contact.getColor(), background);
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

    public IContactClickListener clickListener;
    public interface IContactClickListener {
        void contactClicked(Contact contact, int contactPosition);
    }
}
