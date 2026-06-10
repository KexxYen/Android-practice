package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.DecimalFormat

private val fmt = DecimalFormat("#,##0.00")

@Composable
fun ResultScreen(
    nav: NavController,
    vm: DepositViewModel
) {
    val result = vm.calculatePreview()

    if (result == null) {
        Text(
            text = "Нет данных для расчёта",
            modifier = Modifier.padding(24.dp)
        )
        return
    }

    val finalAmount = result.first
    val interestEarned = result.second
    val initialAmount = vm.initialAmount.toDoubleOrNull() ?: 0.0
    val periodMonths = vm.periodMonths.toIntOrNull() ?: 0
    val monthlyTopUp = vm.monthlyTopUp.toDoubleOrNull() ?: 0.0

    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Результат расчёта",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultRow("Стартовый взнос", "${fmt.format(initialAmount)} ₽")
                ResultRow("Срок вклада", "$periodMonths мес.")
                ResultRow("Процентная ставка", "${vm.selectedRate}%")

                if (monthlyTopUp > 0) {
                    ResultRow("Ежемес. пополнение", "${fmt.format(monthlyTopUp)} ₽")
                }

                HorizontalDivider()

                ResultRow(
                    label = "Итоговая сумма",
                    value = "${fmt.format(finalAmount)} ₽",
                    bold = true
                )

                ResultRow(
                    label = "Начисленные проценты",
                    value = "${fmt.format(interestEarned)} ₽",
                    bold = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                vm.saveCurrentCalculation()
                saved = true
            },
            enabled = !saved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (saved) {
                    "Сохранено ✓"
                } else {
                    "Сохранить"
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                vm.clearForm()
                nav.navigate("tabs") {
                    popUpTo("tabs") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

@Composable
private fun ResultRow(
    label: String,
    value: String,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = if (bold) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}