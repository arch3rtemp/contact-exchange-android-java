package dev.arch3rtemp.contactexchange.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import dev.arch3rtemp.contactexchange.data.model.CardEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface CardDao {

    @Query("SELECT * FROM contact_table WHERE is_my == 0 ORDER BY name ASC")
    Observable<List<CardEntity>> selectScannedCards();

    @Query("SELECT * FROM contact_table WHERE is_my == 1 ORDER BY job ASC")
    Observable<List<CardEntity>> selectMyCards();

    @Query("SELECT * FROM contact_table WHERE id == :id LIMIT 1")
    Observable<CardEntity> selectCardById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(CardEntity cardEntity);

    @Update
    Completable update(CardEntity cardEntity);

    @Query("DELETE FROM contact_table WHERE id == :id")
    Completable delete(int id);
}
