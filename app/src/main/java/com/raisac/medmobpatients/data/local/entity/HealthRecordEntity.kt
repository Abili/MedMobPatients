package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for health records
 */
@Entity(tableName = "health_records")
data class HealthRecordEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val recordType: String,
    val date: Long,
    val documentUrl: String?,
    val createdAt: Long
)
