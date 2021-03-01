package com.example.contactsexchangejava.ui;

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
import com.example.contactsexchangejava.ui.adapter.ContactRecyclerAdapter;
import com.example.contactsexchangejava.ui.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ContactRecyclerAdapter.IContactClickListener {

    TextView card;
    RecyclerView rvCards;
    RecyclerView rvContacts;
    ContactRecyclerAdapter rvCardAdapter;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llCardManager;
    LinearLayoutManager llContactManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createCardRecyclerView(view);
        createContactRecyclerView(view);
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
//        rvCardAdapter.addContacts(getMyCards());
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
        return cards;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "Parker", "GAU", "Project Manager", "parker@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.clay_warm), false));
        contacts.add(new Contact("Alice", "Worth", "GAU", "Office Manager", "worth@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.clay), false));
        contacts.add(new Contact("Brad", "Wilson", "GAU", "Graphic Designer", "wilson@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.dusky_rose), false));
        contacts.add(new Contact("Jack", "Jackson", "GAU", "Art Director", "jackson@gau.ge", "+995 577 783189", "+995 598 48 84 34", getResources().getColor(R.color.soft_green),false));
        contacts.addAll(contacts);
        contacts.addAll(contacts);
        return contacts;
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }
  

    @Override
    public void contactClicked(Contact contact, int contactPosition) {
        CardFragment cardFragment;
        if (contact.getMe()) {
            cardFragment = new CardFragment(true);
        } else {
            cardFragment = CardFragment.getInstance();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, cardFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
