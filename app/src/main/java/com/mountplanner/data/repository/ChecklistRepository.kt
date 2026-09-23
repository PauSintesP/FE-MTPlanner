package com.mountplanner.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mountplanner.data.local.dao.ChecklistDao
import com.mountplanner.data.model.ChecklistItem
import com.mountplanner.data.model.toDomain
import com.mountplanner.data.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistRepository @Inject constructor(
    private val checklistDao: ChecklistDao,
    private val gson: Gson
) {
    fun getByExpedition(expeditionId: String): Flow<List<ChecklistItem>> =
        checklistDao.getByExpedition(expeditionId).map { list -> list.map { it.toDomain() } }

    fun getByTemplate(templateId: String): Flow<List<ChecklistItem>> =
        checklistDao.getByTemplate(templateId).map { list -> list.map { it.toDomain() } }

    suspend fun save(item: ChecklistItem) = checklistDao.insert(item.toEntity())
    
    suspend fun saveAll(items: List<ChecklistItem>) = checklistDao.insertAll(items.map { it.toEntity() })
    
    suspend fun update(item: ChecklistItem) = checklistDao.update(item.toEntity())
    
    suspend fun updateChecked(id: String, isChecked: Boolean) = checklistDao.updateChecked(id, isChecked)
    
    suspend fun updatePacked(id: String, isPacked: Boolean) = checklistDao.updatePacked(id, isPacked)
    
    suspend fun delete(item: ChecklistItem) = checklistDao.delete(item.toEntity())

    suspend fun deleteByExpedition(expeditionId: String) = checklistDao.deleteByExpedition(expeditionId)

    suspend fun loadTemplate(templateName: String, context: Context): List<ChecklistItem> = withContext(Dispatchers.IO) {
        try {
            context.assets.open("checklists/$templateName.json").use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val listType = object : TypeToken<List<ChecklistItem>>() {}.type
                gson.fromJson(reader, listType)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
