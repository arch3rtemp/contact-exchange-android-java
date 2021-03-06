package com.example.contactsexchangejava.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.contactsexchangejava.ui.model.Contact;

import java.util.List;

public interface ContactDao {

    @Query("SELECT * FROM contact WHERE is_me == 0 ")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contact WHERE is_me == 1")
    List<Contact> getAllMyCards();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Contact... contacts);

    @Query("DELETE FROM contact WHERE id == :id")
    void deleteContact(int id);
}
