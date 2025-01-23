package dev.arch3rtemp.contactexchange.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import dev.arch3rtemp.contactexchange.db.models.Contact;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact_table WHERE is_my == 2 ORDER BY name ASC")
    Observable<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact_table WHERE is_my == 1 ORDER BY job ASC")
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
