package dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.viewholder;

import dev.arch3rtemp.contactexchange.databinding.ContactListItemBinding;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.listener.ContactClickListener;

public class ScannedCardViewHolder extends BaseViewHolder {
    private final ContactListItemBinding binding;
    private final ContactClickListener listener;

    public ScannedCardViewHolder(ContactListItemBinding binding, ContactClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void setData(CardUi card) {
        binding.tvContactName.setText(card.name());
        binding.tvContactInitials.setText(card.formatInitials());
        binding.tvContactPosition.setText(card.job());
        binding.tvContactAddDate.setText(card.formattedCreatedAt());
        binding.llContactInitials.getBackground().setColorFilter(card.getSrcInColorFilter());

        binding.llItemRoot.setOnClickListener((v -> {
            binding.getRoot().close(true);
            listener.onContactClick(card.id());
        }));

        binding.llDelete.setOnClickListener((v -> {
            binding.getRoot().close(true);
            listener.onDeleteClick(card);
        }));
    }
}
