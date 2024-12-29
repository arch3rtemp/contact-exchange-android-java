package dev.arch3rtemp.contactexchange.presentation.ui.home.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;

public class CardDiffUtil extends DiffUtil.ItemCallback<CardUi> {
    @Override
    public boolean areItemsTheSame(@NonNull CardUi oldItem, @NonNull CardUi newItem) {
        return oldItem.id() == newItem.id();
    }

    @Override
    public boolean areContentsTheSame(@NonNull CardUi oldItem, @NonNull CardUi newItem) {
        return oldItem.equals(newItem);
    }
}
