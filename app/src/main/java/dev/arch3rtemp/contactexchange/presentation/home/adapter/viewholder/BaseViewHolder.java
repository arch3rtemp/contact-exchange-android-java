package dev.arch3rtemp.contactexchange.presentation.home.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.arch3rtemp.contactexchange.domain.model.Card;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public void setData(Card card);
}
