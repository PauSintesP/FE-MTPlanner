package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val templateId: String? = null,
    val category: String, // navigation | shelter | clothing | food | safety | technical
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
