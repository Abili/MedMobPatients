package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.raisac.medmobpatients.data.local.converter.StringListConverter

/**
 * Room database entity for medications
 */
@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    val startDate: Long,
    val endDate: Long?,
    @TypeConverters(StringListConverter::class)
    val reminderTimes: List<String>,
    val notes: String,
    val prescriptionUrl: String?,
    val isActive: Boolean,
    val createdAt: Long
)
