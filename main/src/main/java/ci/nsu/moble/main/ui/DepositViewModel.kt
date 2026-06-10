package ci.nsu.moble.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.TokenManager
import ci.nsu.moble.main.data.local.DepositCalculation
import ci.nsu.moble.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    var initialAmount by mutableStateOf("")
    var selectedRate by mutableStateOf(15.0)
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")

    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations

    fun loadCalculations() {
        val userId = TokenManager.userId

        viewModelScope.launch {
            repository.getCalculationsByUser(userId).collect {
                _calculations.value = it
            }
        }
    }

    fun saveCurrentCalculation() {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        saveCalculation(
            initialAmount = amount,
            periodMonths = months,
            interestRate = selectedRate,
            monthlyTopUp = topUp
        )
    }

    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ) {
        val finalAmount = calculateFinalAmount(
            initialAmount,
            periodMonths,
            interestRate,
            monthlyTopUp
        )

        val interestEarned = finalAmount - initialAmount - monthlyTopUp * periodMonths

        val calculation = DepositCalculation(
            userId = TokenManager.userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveCalculation(calculation)
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearForm() {
        initialAmount = ""
        selectedRate = 15.0
        periodMonths = ""
        monthlyTopUp = ""
    }

    fun calculatePreview(): Pair<Double, Double>? {
        val amount = initialAmount.toDoubleOrNull() ?: return null
        val months = periodMonths.toIntOrNull() ?: return null
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val finalAmount = calculateFinalAmount(
            initialAmount = amount,
            periodMonths = months,
            interestRate = selectedRate,
            monthlyTopUp = topUp
        )

        val interestEarned = finalAmount - amount - topUp * months

        return Pair(finalAmount, interestEarned)
    }

    private fun calculateFinalAmount(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ): Double {
        var amount = initialAmount
        val monthlyRate = interestRate / 100.0 / 12.0

        repeat(periodMonths) {
            amount += monthlyTopUp
            amount += amount * monthlyRate
        }

        return amount
    }
}