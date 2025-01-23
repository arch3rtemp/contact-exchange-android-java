package dev.arch3rtemp.contactexchange.ui.home;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.constants.IsMe;
import dev.arch3rtemp.contactexchange.db.models.Contact;

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
    private final int isMe;
    private final ContactRecyclerAdapter.IContactClickListener clickListener;
    private final ContactRecyclerAdapter.IDeleteClickListener deleteListener;

    public ContactHolder(
            @NonNull View itemView,
            int isMe,
            ContactRecyclerAdapter.IContactClickListener clickListener,
            ContactRecyclerAdapter.IDeleteClickListener deleteListener
    ) {
        super(itemView);
        this.isMe = isMe;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
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
            tvName.setText(contact.getName());
            tvInitials.setText(contact.formatInitials());
            tvPosition.setText(contact.getPosition());
            tvAddDate.setText(contact.getCreatedAd());
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
