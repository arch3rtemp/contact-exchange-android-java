package dev.arch3rtemp.contactexchange.presentation.home.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import dev.arch3rtemp.contactexchange.domain.model.Card;

public class CardDiffUtil extends DiffUtil.ItemCallback<Card> {
    @Override
    public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
        return oldItem.id() == newItem.id();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
        return oldItem.equals(newItem);
    }
}
