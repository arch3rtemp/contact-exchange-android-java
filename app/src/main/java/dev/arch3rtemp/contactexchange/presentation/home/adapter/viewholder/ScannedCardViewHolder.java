package dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder;

import dev.arch3rtemp.contactexchange.databinding.ContactListItemBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.ContactClickListener;
import dev.arch3rtemp.ui.util.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScannedCardViewHolder extends BaseViewHolder {
    private final ContactListItemBinding binding;
    private final ContactClickListener listener;

    public ScannedCardViewHolder(ContactListItemBinding binding, ContactClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void setData(Card card) {
        binding.tvContactName.setText(card.name());
        binding.tvContactInitials.setText(formatInitials(card.name()));
        binding.tvContactPosition.setText(card.job());
        binding.tvContactAddDate.setText(dateToString(card.createDate()));
        binding.tvContactName.setText(card.name());
        binding.llContactInitials.getBackground().setColorFilter(ColorUtils.createSrcInColorFilter(card.color()));

        binding.llItemRoot.setOnClickListener((v -> {
            binding.getRoot().close(true);
            listener.onContactClick(card.id());
        }));

        binding.llDelete.setOnClickListener((v -> listener.onDeleteClick(card)));
    }

    private String formatInitials(String name) {
        if (name.contains(" ")) {
            var spaceIndex = name.indexOf(" ") + 1;
            var firstLetter = name.substring(0, 1);
            var secondLetter = name.substring(spaceIndex, spaceIndex + 1);
             return firstLetter + secondLetter;
        } else {
            return name.substring(0, 1);
        }
    }

    private String dateToString(Date date) {
        var simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
