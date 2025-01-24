package dev.arch3rtemp.contactexchange.ui.home;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.util.ColorUtils;

public class ContactHolder extends RecyclerView.ViewHolder {

    private TextView tvCard;
    private TextView tvInitials;
    private LinearLayout llInitials;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvAddDate;
    private LinearLayout llDelete;
    private SwipeRevealLayout swipeRevealLayout;
    private LinearLayout llItemRoot;
    private final boolean isMy;
    private final ContactRecyclerAdapter.IContactClickListener clickListener;
    private final ContactRecyclerAdapter.IDeleteClickListener deleteListener;

    public ContactHolder(
            @NonNull View itemView,
            boolean isMy,
            ContactRecyclerAdapter.IContactClickListener clickListener,
            ContactRecyclerAdapter.IDeleteClickListener deleteListener
    ) {
        super(itemView);
        this.isMy = isMy;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
        if (isMy) {
            tvCard = itemView.findViewById(R.id.tv_card);
            return;
        }
        llItemRoot = itemView.findViewById(R.id.ll_item_root);
        tvInitials = itemView.findViewById(R.id.tv_contact_initials);
        llInitials = itemView.findViewById(R.id.ll_contact_initials);
        tvName = itemView.findViewById(R.id.tv_contact_name);
        tvPosition = itemView.findViewById(R.id.tv_contact_position);
        tvAddDate = itemView.findViewById(R.id.tv_contact_add_date);
        llDelete = itemView.findViewById(R.id.ll_delete);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);

    }

    public void setData(Contact contact) {
        if (isMy) {
            tvCard.setText(contact.getJob());
            Drawable background = tvCard.getBackground();
            background.setColorFilter(ColorUtils.createSrcInColorFilter(contact.getColor()));
            tvCard.setOnClickListener(v -> clickListener.onContactClicked(contact, getAdapterPosition()));
        } else {
            tvName.setText(contact.getName());
            tvInitials.setText(contact.formatInitials());
            tvPosition.setText(contact.getPosition());
            tvAddDate.setText(contact.getDateString());
            llItemRoot.setOnClickListener(v -> clickListener.onContactClicked(contact, getAdapterPosition()));
            llDelete.setOnClickListener(v -> deleteListener.onDeleteClicked(contact, getAdapterPosition()));
            Drawable background = llInitials.getBackground();
            background.setColorFilter(ColorUtils.createSrcInColorFilter(contact.getColor()));
        }
    }
}
