package dev.arch3rtemp.contactexchange.presentation.ui.home.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.arch3rtemp.contactexchange.presentation.model.CardUi;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public void setData(CardUi card);
}
