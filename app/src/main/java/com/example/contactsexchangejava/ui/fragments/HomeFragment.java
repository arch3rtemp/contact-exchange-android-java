package com.example.contactsexchangejava.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.activities.CardActivity;
import com.example.contactsexchangejava.ui.adapter.ContactRecyclerAdapter;
import com.example.contactsexchangejava.ui.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ContactRecyclerAdapter.IContactClickListener {

    TextView card;
    RecyclerView rvCards;
    RecyclerView rvContacts;
    FloatingActionButton fab;
    ContactRecyclerAdapter rvCardAdapter;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llCardManager;
    LinearLayoutManager llContactManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setListeners();
        createCardRecyclerView(view);
        createContactRecyclerView(view);

    }

    private void initUI(View view) {
        fab = view.findViewById(R.id.fb_add);
    }

    private void setListeners() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardActivity.class);
            intent.putExtra("isCreate", true);
            startActivity(intent);
        });
    }

    public void createCardRecyclerView(View view) {
        card = view.findViewById(R.id.tv_card);
        rvCards = view.findViewById(R.id.rv_cards);
        rvCardAdapter = new ContactRecyclerAdapter(getMyCards());
        llCardManager = new LinearLayoutManager(getContext());
        llCardManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCards.setLayoutManager(llCardManager);
        rvCards.setAdapter(rvCardAdapter);
        rvCardAdapter.setContactClickListener(this);
        observeRecyclerListener();
//        rvCardAdapter.addContacts(getMyCards());
    }

    public void observeRecyclerListener() {
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

    public void createContactRecyclerView(View view) {
        rvContacts = view.findViewById(R.id.rv_contacts);
        rvContactAdapter = new ContactRecyclerAdapter(getContacts());
        llContactManager = new LinearLayoutManager(getContext());
        llContactManager.setOrientation(RecyclerView.VERTICAL);
        rvContacts.setLayoutManager(llContactManager);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setContactClickListener(this);
//        rvContactAdapter.addContacts(getContacts());

    }

    private List<Contact> getMyCards() {
        List<Contact> cards = new ArrayList<>();
        cards.add(new Contact("Archil", "Asanishvili","Georgian-American University", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", getResources().getColor(R.color.light_navy), true));
        cards.add(new Contact("Archil", "Asanishvili","Terabank", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", getResources().getColor(R.color.purple), true));
//        cards.add(new Contact("Archil", "Asanishvili","Terabank", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", getResources().getColor(R.color.purple), true));
        return cards;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "Parker", "GAU", "Project Manager", "parker@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.clay_warm), false));
        contacts.add(new Contact("Alice", "Worth", "GAU", "Office Manager", "worth@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.clay), false));
        contacts.add(new Contact("Brad", "Wilson", "GAU", "Graphic Designer", "wilson@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.dusky_rose), false));
        contacts.add(new Contact("Jack", "Jackson", "GAU", "Art Director", "jackson@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.soft_green),false));
//        contacts.addAll(contacts);
//        contacts.addAll(contacts);
        return contacts;
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }
  

    @Override
    public void contactClicked(Contact contact, int contactPosition) {
        
        Intent intent = new Intent(getContext(), CardActivity.class);

        intent.putExtra("isMe", contact.getMe());

        startActivity(intent);
    }
}
