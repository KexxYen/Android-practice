package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.local.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {

    fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculation>>

    suspend fun getById(id: Long): DepositCalculation?

    suspend fun saveCalculation(calculation: DepositCalculation)

    suspend fun deleteById(id: Long)
}