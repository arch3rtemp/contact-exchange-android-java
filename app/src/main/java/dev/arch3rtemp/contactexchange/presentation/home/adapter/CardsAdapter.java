package dev.arch3rtemp.contactexchange.presentation.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import dev.arch3rtemp.contactexchange.databinding.CardListItemBinding;
import dev.arch3rtemp.contactexchange.databinding.ContactListItemBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.CardClickListener;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.ContactClickListener;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder.BaseViewHolder;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder.MyCardViewHolder;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder.ScannedCardViewHolder;

public class CardsAdapter extends ListAdapter<Card, BaseViewHolder> {

    private final CardClickListener cardClickListener;
    private final ContactClickListener contactClickListener;

    public CardsAdapter(CardClickListener cardClickListener, ContactClickListener contactClickListener) {
        super(new CardDiffUtil());
        this.cardClickListener = cardClickListener;
        this.contactClickListener = contactClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getCurrentList().get(position).isMy()) {
            return MY_CARD;
        }
        else {
            return SCANNED_CARD;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BaseViewHolder holder;

        switch (viewType) {
            case MY_CARD -> {
                var binding = CardListItemBinding.inflate(inflater, parent, false);
                holder = new MyCardViewHolder(binding, cardClickListener);
            }
            case SCANNED_CARD -> {
                var binding = ContactListItemBinding.inflate(inflater, parent, false);
                holder = new ScannedCardViewHolder(binding, contactClickListener);
            }
            default ->
                throw new IllegalStateException("Unexpected value: " + viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(getCurrentList().get(position));
    }

    private final int MY_CARD = 197;
    private final int SCANNED_CARD = 198;
}
