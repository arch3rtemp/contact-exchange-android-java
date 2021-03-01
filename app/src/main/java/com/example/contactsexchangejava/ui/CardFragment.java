package com.example.contactsexchangejava.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.contactsexchangejava.R;

public class CardFragment extends Fragment {

    View view;
    private boolean isMe;

    public ConstraintLayout clEdit;

    public CardFragment(Boolean me) {
        isMe = me;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isMe) {
            clEdit = view.findViewById(R.id.cl_edit);
            clEdit.setVisibility(View.GONE);
        }
    }

    public static CardFragment getInstance() {
        return new CardFragment(false);
    }
}
