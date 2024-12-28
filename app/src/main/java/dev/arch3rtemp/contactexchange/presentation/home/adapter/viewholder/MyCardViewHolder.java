package dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder;

import dev.arch3rtemp.contactexchange.databinding.CardListItemBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.CardClickListener;
import dev.arch3rtemp.ui.util.ColorUtils;

public class MyCardViewHolder extends BaseViewHolder {
    private final CardListItemBinding binding;
    private final CardClickListener listener;

    public MyCardViewHolder(CardListItemBinding binding, CardClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void setData(Card card) {
        binding.tvCard.setText(card.job());
        binding.tvCard.getBackground().setColorFilter(ColorUtils.createSrcInColorFilter(card.color()));

        binding.getRoot().setOnClickListener((v -> listener.onCardClick(card.id())));
    }
}
