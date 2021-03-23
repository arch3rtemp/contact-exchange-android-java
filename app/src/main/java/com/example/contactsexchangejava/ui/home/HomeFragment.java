package com.example.contactsexchangejava.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.card.CardActivity;
import com.example.contactsexchangejava.db.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ContactRecyclerAdapter.IContactClickListener, IHomeContract.View{

    View view;
    TextView card;
    RecyclerView rvCards;
    RecyclerView rvContacts;
    FloatingActionButton fab;
    ContactRecyclerAdapter rvCardAdapter;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llCardManager;
    LinearLayoutManager llContactManager;
    IHomeContract.Presenter presenter;
    List<Contact> myCards = new ArrayList<>();
    List<Contact> contacts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        setListeners();

        getMyCards();
        getContacts();
    }

    private void initUI() {
        fab = view.findViewById(R.id.fb_add);
        setPresenter(new HomePresenter(this));
        presenter.onViewCreated(getActivity());
    }

    private void setListeners() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardActivity.class);
            intent.putExtra("isCreate", true);
            startActivity(intent);
        });
    }

    private void createCardRecyclerView() {
        card = view.findViewById(R.id.tv_card);
        rvCards = view.findViewById(R.id.rv_cards);
        rvCardAdapter = new ContactRecyclerAdapter(myCards);
        llCardManager = new LinearLayoutManager(getContext());
        llCardManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCards.setLayoutManager(llCardManager);
        rvCards.setAdapter(rvCardAdapter);
        rvCardAdapter.setContactClickListener(this);
        observeRecyclerListener();
//        rvCardAdapter.addContacts(getMyCards());
    }

    private void createContactRecyclerView() {
        rvContacts = view.findViewById(R.id.rv_contacts);
        rvContactAdapter = new ContactRecyclerAdapter(contacts);
        llContactManager = new LinearLayoutManager(getContext());
        llContactManager.setOrientation(RecyclerView.VERTICAL);
        rvContacts.setLayoutManager(llContactManager);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setContactClickListener(this);
//        rvContactAdapter.addContacts(getContacts());
    }

    private void observeRecyclerListener() {
        rvCards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void getMyCards() {
        presenter.getMyCards();
    }

    private void getContacts() {
        presenter.getContacts();
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }
  

    @Override
    public void contactClicked(Contact contact, int contactPosition) {
        
        Intent intent = new Intent(getContext(), CardActivity.class);

        intent.putExtra("isMe", contact.getMe());
        intent.putExtra("id", contact.getId());
        intent.putExtra("contact", contact);

        startActivity(intent);
    }

    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        rvContactAdapter.clearAdapter();
        rvCardAdapter.clearAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        presenter.onDestroy();
    }

    @Override
    public void onGetMyCards(List<Contact> cards) {
        myCards.addAll(cards);
        createCardRecyclerView();
    }

    @Override
    public void onGetContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
        createContactRecyclerView();
    }
}
