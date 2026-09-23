package com.mountplanner.domain.usecase

import com.mountplanner.domain.model.Expedition
import com.mountplanner.domain.repository.ExpeditionRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateExpeditionUseCase @Inject constructor(
    private val expeditionRepository: ExpeditionRepository
) {
    suspend operator fun invoke(expedition: Expedition): String {
        val newExpedition = expedition.copy(
            id = UUID.randomUUID().toString(),
            status = "planning",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        expeditionRepository.save(newExpedition)
        return newExpedition.id
    }
}
