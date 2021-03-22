package com.example.contactsexchangejava.ui.card;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.Objects;

public class CardFragment extends Fragment implements ICardContract.View {

    View view;
    private ConstraintLayout clEdit;
    private ConstraintLayout clDelete;
    private boolean isMe;
    AlertDialog deleteDialog;
    Button delete;
    Button cancel;
    TextView tvName;
    TextView tvPosition;
    TextView tvEmail;
    TextView tvPhoneMobile;
    TextView tvPhoneOffice;
    ICardContract.Presenter presenter;

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
        setPresenter(new CardPresenter(this));
        presenter.onViewCreated(getActivity());
        int id = Objects.requireNonNull(getActivity()).getIntent().getIntExtra("id", 0);
        getCard(id);
    }

    private void getCard(int id) {
        presenter.getContactById(id);
    }

    @Override
    public void onCardLoaded(Contact card) {
        String name = card.getFirstName() + " " + card.getLastName();
        tvName.setText(name);
        tvPosition.setText(card.getPosition());
        tvEmail.setText(card.getEmail());
        tvPhoneMobile.setText(card.getPhoneMobile());
        tvPhoneOffice.setText(card.getPhoneOffice());
    }

    private void setListeners() {
        clEdit.setOnClickListener(v -> {
            CreateOrEditCardFragment editCardFragment = new CreateOrEditCardFragment(false);

            FragmentTransaction transaction;
            transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_main_frame_container, editCardFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        clDelete.setOnClickListener(v -> createDeleteDialog(v));
    }

    private void createDeleteDialog(View v) {
        deleteDialog = new AlertDialog.Builder(getContext()).create();
        final View deletePopup = LayoutInflater.from(getContext()).inflate(R.layout.delete_popup, null, false);

        deleteDialog.setView(deletePopup);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
        delete = deletePopup.findViewById(R.id.btn_delete);
        cancel = deletePopup.findViewById(R.id.btn_cancel);


        delete.setOnClickListener(d -> {

            deleteDialog.dismiss();
            createDeletedFragment();
        });

        cancel.setOnClickListener(c -> {
            deleteDialog.dismiss();
        });
    }

    private void createDeletedFragment() {

        DeletedFragment deletedFragment = new DeletedFragment();
        FragmentTransaction transaction;
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, deletedFragment);
        transaction.commit();
    }

    public static CardFragment getInstance() {
        return new CardFragment(false);
    }

    @Override
    public void setPresenter(ICardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}