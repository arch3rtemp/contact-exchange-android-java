package com.example.contactsexchangejava.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsexchangejava.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CardFragment extends Fragment {

    View view;
    private ConstraintLayout clEdit;
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

    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            CreateOrEditCardFragment editCardFragment = new CreateOrEditCardFragment(false);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, editCardFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public static CardFragment getInstance() {
        return new CardFragment(false);
    }
}
