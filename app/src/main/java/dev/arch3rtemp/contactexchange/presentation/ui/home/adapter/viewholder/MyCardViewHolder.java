package dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.viewholder;

import dev.arch3rtemp.contactexchange.databinding.CardListItemBinding;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.listener.CardClickListener;

public class MyCardViewHolder extends BaseViewHolder {
    private final CardListItemBinding binding;
    private final CardClickListener listener;

    public MyCardViewHolder(CardListItemBinding binding, CardClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void setData(CardUi card) {
        binding.tvCard.setText(card.job());
        binding.tvCard.getBackground().setColorFilter(card.getSrcInColorFilter());

        binding.getRoot().setOnClickListener((v -> listener.onCardClick(card.id())));
    }
}
