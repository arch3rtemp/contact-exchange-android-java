package dev.arch3rtemp.contactexchange.db;

import dev.arch3rtemp.contactexchange.db.models.Contact;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface DataCallBack {

    void createContact(Contact contact);
    void editContact(Contact contact);
    Observable<List<Contact>> loadContacts();
    Observable<List<Contact>> loadMyCards();
    Observable<Contact> loadContactById(int id);
    void deleteContact(int id);
}
