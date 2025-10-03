package com.raisac.medmobpatients.data.mapper

import com.raisac.medmobpatients.data.local.entity.HealthRecordEntity
import com.raisac.medmobpatients.domain.model.HealthRecord
import com.raisac.medmobpatients.domain.model.RecordType

/**
 * Extension functions to map between HealthRecordEntity and HealthRecord domain model
 */
fun HealthRecordEntity.toDomain(): HealthRecord {
    return HealthRecord(
        id = id,
        userId = userId,
        title = title,
        description = description,
        recordType = RecordType.valueOf(recordType),
        date = date,
        documentUrl = documentUrl,
        createdAt = createdAt
    )
}

fun HealthRecord.toEntity(): HealthRecordEntity {
    return HealthRecordEntity(
        id = id,
        userId = userId,
        title = title,
        description = description,
        recordType = recordType.name,
        date = date,
        documentUrl = documentUrl,
        createdAt = createdAt
    )
}
