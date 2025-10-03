package com.raisac.medmobpatients.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for vital signs
 */
@Entity(tableName = "vital_signs")
data class VitalSignsEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val bloodPressureSystolic: Int?,
    val bloodPressureDiastolic: Int?,
    val heartRate: Int?,
    val temperature: Double?,
    val oxygenSaturation: Int?,
    val weight: Double?,
    val height: Double?,
    val glucoseLevel: Int?,
    val notes: String,
    val recordedAt: Long
)
