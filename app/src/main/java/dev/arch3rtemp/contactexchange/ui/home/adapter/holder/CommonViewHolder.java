package dev.arch3rtemp.contactexchange.ui.home.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;

public abstract class CommonViewHolder extends RecyclerView.ViewHolder {

    public CommonViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setData(ContactUi contact);
}
