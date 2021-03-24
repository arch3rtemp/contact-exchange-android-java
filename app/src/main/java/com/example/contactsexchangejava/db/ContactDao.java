package com.example.contactsexchangejava.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact_table WHERE first_name != null ORDER BY last_name ASC")
    Observable<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact_table WHERE is_me == 1 ORDER BY job ASC")
    Observable<List<Contact>> getAllMyCards();

    @Query("SELECT * FROM contact_table WHERE id = :id LIMIT 1")
    Observable<Contact> getContactById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Query("DELETE FROM contact_table WHERE id == :id")
    void delete(int id);
}
