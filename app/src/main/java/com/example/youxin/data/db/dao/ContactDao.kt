package com.example.youxin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.youxin.data.db.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun saveContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<ContactEntity>

    @Query("SELECT * FROM contacts ORDER BY nickName ASC")
    fun observeAllContactsSortedByName(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: String): ContactEntity

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts(): Int

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: String): Int
}