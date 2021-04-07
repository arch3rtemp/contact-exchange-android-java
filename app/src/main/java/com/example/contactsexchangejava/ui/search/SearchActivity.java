package com.example.contactsexchangejava.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBaseView;
import com.example.contactsexchangejava.ui.card.CardActivity;
import com.example.contactsexchangejava.ui.home.ContactRecyclerAdapter;
import com.example.contactsexchangejava.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ISearchContract.View, ContactRecyclerAdapter.IContactClickListener {
    ISearchContract.Presenter presenter;
    List<Contact> contacts = new ArrayList<>();
    RecyclerView rvContacts;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llContactManager;
    ConstraintLayout clContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initUI();
        getContacts();
    }

    private void getContacts() {
        presenter.getContacts();
    }

    private void initUI() {
        setPresenter(new SearchPresenter(this));
        presenter.onViewCreated(this);
        clContacts = findViewById(R.id.cl_contacts_search);
    }

    @Override
    public void contactClicked(Contact contact, int contactPosition) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("isMe", contact.getIsMe());
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void onGetContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
        createContactRecyclerView();
    }

    private void createContactRecyclerView() {
        rvContacts = findViewById(R.id.rv_contacts_search);
        rvContactAdapter = new ContactRecyclerAdapter(contacts);
        llContactManager = new LinearLayoutManager(this);
        llContactManager.setOrientation(RecyclerView.VERTICAL);
        rvContacts.setLayoutManager(llContactManager);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setContactClickListener(this);
//        rvContactAdapter.addContacts(getContacts());
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void setPresenter(ISearchContract.Presenter presenter) {
        this.presenter = presenter;
    }
}