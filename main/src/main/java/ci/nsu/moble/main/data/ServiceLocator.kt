package ci.nsu.moble.main.data

import android.content.Context
import ci.nsu.moble.main.data.local.AppDatabase
import ci.nsu.moble.main.data.repository.DepositRepository
import ci.nsu.moble.main.data.repository.DepositRepositoryImpl

class ServiceLocator(context: Context) {

    val database by lazy {
        AppDatabase.getDatabase(context)
    }

    val authRepository by lazy {
        AuthRepository()
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(database.depositDao())
    }
}