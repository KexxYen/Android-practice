package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.local.DepositCalculation
import ci.nsu.moble.main.data.local.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepositoryImpl(
    private val dao: DepositDao
) : DepositRepository {

    override fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getByUserId(userId)
    }

    override suspend fun getById(id: Long): DepositCalculation? {
        return dao.getById(id)
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}