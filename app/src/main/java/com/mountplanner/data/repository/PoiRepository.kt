package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.PoiDao
import com.mountplanner.data.model.Poi
import com.mountplanner.data.model.toDomain
import com.mountplanner.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoiRepository @Inject constructor(
    private val poiDao: PoiDao
) {
    fun getAllPois(): Flow<List<Poi>> =
        poiDao.getAllPois().map { list -> list.map { it.toDomain() } }

    fun getByExpedition(expeditionId: String): Flow<List<Poi>> =
        poiDao.getByExpedition(expeditionId).map { list -> list.map { it.toDomain() } }

    fun getByCategory(category: String): Flow<List<Poi>> =
        poiDao.getByCategory(category).map { list -> list.map { it.toDomain() } }

    fun getById(id: String): Flow<Poi?> =
        poiDao.getById(id).map { it?.toDomain() }

    suspend fun save(poi: Poi) = poiDao.insert(poi.toEntity())
    
    suspend fun update(poi: Poi) = poiDao.update(poi.toEntity())
    
    suspend fun delete(poi: Poi) = poiDao.delete(poi.toEntity())

    suspend fun deleteByExpedition(expeditionId: String) = poiDao.deleteByExpedition(expeditionId)
}
