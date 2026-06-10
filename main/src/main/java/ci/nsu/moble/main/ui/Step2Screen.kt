package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Step2Screen(
    nav: NavController,
    vm: DepositViewModel
) {
    var periodError by remember { mutableStateOf(false) }
    var periodErrorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Шаг 2: Срок вклада",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Выбранная ставка: ${vm.selectedRate}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = getPeriodHint(vm.selectedRate),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.periodMonths,
            onValueChange = {
                vm.periodMonths = it.filter { c -> c.isDigit() }
                periodError = false
            },
            label = { Text("Срок вклада, месяцев") },
            placeholder = { Text(getPeriodHint(vm.selectedRate)) },
            isError = periodError,
            supportingText = {
                if (periodError) {
                    Text(periodErrorText)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.monthlyTopUp,
            onValueChange = {
                vm.monthlyTopUp = it.filter { c ->
                    c.isDigit() || c == '.'
                }
            },
            label = { Text("Ежемесячное пополнение") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    nav.navigateUp()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    if (!isPeriodValid(vm.selectedRate, vm.periodMonths)) {
                        periodError = true
                        periodErrorText = when (vm.selectedRate) {
                            15.0 -> "Для ставки 15% срок должен быть от 1 до 6 месяцев"
                            10.0 -> "Для ставки 10% срок должен быть от 7 до 12 месяцев"
                            else -> "Для ставки 5% срок должен быть от 13 месяцев и больше"
                        }
                        return@Button
                    }

                    nav.navigate("result")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}

private fun getPeriodHint(rate: Double): String {
    return when (rate) {
        15.0 -> "Введите срок от 1 до 6 месяцев"
        10.0 -> "Введите срок от 7 до 12 месяцев"
        else -> "Введите срок от 13 месяцев и больше"
    }
}

private fun isPeriodValid(
    rate: Double,
    periodText: String
): Boolean {
    val period = periodText.toIntOrNull() ?: return false

    return when (rate) {
        15.0 -> period in 1..6
        10.0 -> period in 7..12
        else -> period >= 13
    }
}