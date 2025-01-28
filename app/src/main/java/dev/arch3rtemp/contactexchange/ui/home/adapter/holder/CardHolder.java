package dev.arch3rtemp.contactexchange.ui.home.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.home.adapter.listener.ContactClickListener;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.util.ColorUtils;

public class CardHolder extends CommonViewHolder {
    private final View itemView;
    private final TextView tvCard;
    private final ContactClickListener clickListener;

    public CardHolder(@NonNull View itemView, ContactClickListener clickListener) {
        super(itemView);
        this.itemView = itemView;
        this.tvCard = itemView.findViewById(R.id.tv_card);
        this.clickListener = clickListener;
    }

    @Override
    public void setData(ContactUi contact) {
        tvCard.setText(contact.job());
        Drawable background = tvCard.getBackground();
        background.setColorFilter(ColorUtils.createSrcInColorFilter(contact.color()));
        itemView.setOnClickListener(v -> clickListener.onContactClick(contact));
    }
}
