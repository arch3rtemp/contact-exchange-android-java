package com.example.contactsexchangejava.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactsexchangejava.R;

public class CardFragment extends Fragment {

    View view;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private boolean isMe;

    public CardFragment(Boolean isMe) {
        this.isMe = isMe;
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
        initUI(view);
        setListeners();
        if (!isMe) {
            clEdit = view.findViewById(R.id.cl_edit);
            clEdit.setVisibility(View.GONE);

        }
    }

    private void initUI(View view) {
        clEdit = view.findViewById(R.id.cl_edit);
        clDelete = view.findViewById(R.id.cl_delete);
    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            CreateOrEditCardFragment editCardFragment = new CreateOrEditCardFragment(false);

            FragmentTransaction transaction;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_main_frame_container, editCardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        clDelete.setOnClickListener(v -> {
            DeletedFragment deletedFragment = new DeletedFragment();

            FragmentTransaction transaction;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_main_frame_container, deletedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public static CardFragment getInstance() {
        return new CardFragment(false);
    }
}
