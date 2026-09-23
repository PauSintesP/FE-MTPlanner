package com.mountplanner.data.model

import com.mountplanner.data.local.entity.ChecklistItemEntity
import java.util.UUID

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val templateId: String? = null,
    val category: String,
    val name: String,
    val description: String? = null,
    val weightGrams: Int? = null,
    val isChecked: Boolean = false,
    val isPacked: Boolean = false,
    val quantity: Int = 1,
    val isOptional: Boolean = false,
    val sortOrder: Int = 0,
    val notes: String? = null
)

fun ChecklistItem.toEntity(): ChecklistItemEntity = ChecklistItemEntity(
    id = id,
    expeditionId = expeditionId,
    templateId = templateId,
    category = category,
    name = name,
    description = description,
    weightGrams = weightGrams,
    isChecked = isChecked,
    isPacked = isPacked,
    quantity = quantity,
    isOptional = isOptional,
    sortOrder = sortOrder,
    notes = notes
)

fun ChecklistItemEntity.toDomain(): ChecklistItem = ChecklistItem(
    id = id,
    expeditionId = expeditionId,
    templateId = templateId,
    category = category,
    name = name,
    description = description,
    weightGrams = weightGrams,
    isChecked = isChecked,
    isPacked = isPacked,
    quantity = quantity,
    isOptional = isOptional,
    sortOrder = sortOrder,
    notes = notes
)
