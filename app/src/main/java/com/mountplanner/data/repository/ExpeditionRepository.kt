package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.ExpeditionDao
import com.mountplanner.data.model.Expedition
import com.mountplanner.data.model.toDomain
import com.mountplanner.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpeditionRepository @Inject constructor(
    private val expeditionDao: ExpeditionDao
) {
    fun getAllExpeditions(): Flow<List<Expedition>> = 
        expeditionDao.getAllExpeditions().map { list -> list.map { it.toDomain() } }
    
    fun getActiveExpedition(): Flow<Expedition?> =
        expeditionDao.getActiveExpedition().map { it?.toDomain() }
    
    fun getById(id: String): Flow<Expedition?> =
        expeditionDao.getById(id).map { it?.toDomain() }
    
    suspend fun save(expedition: Expedition) = expeditionDao.insert(expedition.toEntity())
    suspend fun update(expedition: Expedition) = expeditionDao.update(expedition.toEntity())
    suspend fun delete(expedition: Expedition) = expeditionDao.delete(expedition.toEntity())
    suspend fun updateStatus(id: String, status: String) = expeditionDao.updateStatus(id, status)
    suspend fun setMountReporterData(id: String, tripId: String, shareToken: String) =
        expeditionDao.setMountReporterData(id, tripId, shareToken)
}
