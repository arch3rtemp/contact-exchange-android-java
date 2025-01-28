package dev.arch3rtemp.contactexchange.ui.home.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.home.adapter.listener.ContactClickListener;
import dev.arch3rtemp.contactexchange.ui.home.adapter.listener.DeleteClickListener;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.util.ColorUtils;

public class ContactHolder extends CommonViewHolder {
    private final TextView tvInitials;
    private final LinearLayout llInitials;
    private final TextView tvName;
    private final TextView tvPosition;
    private final TextView tvAddDate;
    private final LinearLayout llDelete;
    private final LinearLayout llItemRoot;
    private final SwipeRevealLayout swipeLayout;
    private final ContactClickListener clickListener;
    private final DeleteClickListener deleteListener;

    public ContactHolder(
            @NonNull View itemView,
            ContactClickListener clickListener,
            DeleteClickListener deleteListener
    ) {
        super(itemView);
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
        llItemRoot = itemView.findViewById(R.id.ll_item_root);
        tvInitials = itemView.findViewById(R.id.tv_contact_initials);
        llInitials = itemView.findViewById(R.id.ll_contact_initials);
        tvName = itemView.findViewById(R.id.tv_contact_name);
        tvPosition = itemView.findViewById(R.id.tv_contact_position);
        tvAddDate = itemView.findViewById(R.id.tv_contact_add_date);
        llDelete = itemView.findViewById(R.id.ll_delete);
        swipeLayout = itemView.findViewById(R.id.swipe_layout);
    }

    @Override
    public void setData(ContactUi contact) {
        tvName.setText(contact.name());
        tvInitials.setText(contact.formatInitials());
        tvPosition.setText(contact.position());
        tvAddDate.setText(contact.dateString());
        llItemRoot.setOnClickListener(v -> {
            swipeLayout.close(true);
            clickListener.onContactClick(contact);
        });
        llDelete.setOnClickListener(v -> {
            swipeLayout.close(true);
            deleteListener.onDeleteClick(contact);
        });
        Drawable background = llInitials.getBackground();
        background.setColorFilter(ColorUtils.createSrcInColorFilter(contact.color()));
    }
}
