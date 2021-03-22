package com.example.contactsexchangejava.db;

import androidx.lifecycle.LiveData;

import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

import io.reactivex.Observable;

public interface DataCallBack {

    void createContact(Contact contact);
    void editContact(Contact contact);
    Observable<List<Contact>> loadContacts();
    Observable<List<Contact>> loadMyCards();
    Observable<Contact> loadContactById(int id);
    void deleteContact(int id);
}
