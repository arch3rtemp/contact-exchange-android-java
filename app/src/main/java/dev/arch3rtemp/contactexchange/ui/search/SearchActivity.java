package dev.arch3rtemp.contactexchange.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.contactexchange.ui.card.CardActivity;
import dev.arch3rtemp.contactexchange.ui.home.ContactRecyclerAdapter;

public class SearchActivity extends AppCompatActivity implements ISearchContract.View, ContactRecyclerAdapter.IContactClickListener {
    ISearchContract.Presenter presenter;
    List<Contact> contacts = new ArrayList<>();
    RecyclerView rvContacts;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llContactManager;
    ConstraintLayout clContacts;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initUI();
        setListeners();
        getContacts();
    }

    private void getContacts() {
        presenter.getContacts();
    }

    private void initUI() {
        setPresenter(new SearchPresenter(this));
        presenter.onViewCreated(this);
        clContacts = findViewById(R.id.cl_contacts_search);
        etSearch = findViewById(R.id.et_search);
    }

    private void setListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvContactAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onContactClicked(Contact contact, int contactPosition) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("isMe", contact.getIsMy());
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
        finishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void setPresenter(ISearchContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
