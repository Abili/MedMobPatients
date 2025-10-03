package com.raisac.medmobpatients.data.local.dao

import androidx.room.*
import com.raisac.medmobpatients.data.local.entity.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Emergency Contacts operations
 */
@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId ORDER BY isPrimary DESC, name ASC")
    fun getEmergencyContactsByUserId(userId: String): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts WHERE id = :contactId")
    fun getEmergencyContactById(contactId: String): Flow<EmergencyContactEntity?>

    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId AND isPrimary = 1")
    fun getPrimaryEmergencyContact(userId: String): Flow<EmergencyContactEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(contact: EmergencyContactEntity)

    @Update
    suspend fun updateEmergencyContact(contact: EmergencyContactEntity)

    @Delete
    suspend fun deleteEmergencyContact(contact: EmergencyContactEntity)

    @Query("DELETE FROM emergency_contacts WHERE id = :contactId")
    suspend fun deleteEmergencyContactById(contactId: String)
}
