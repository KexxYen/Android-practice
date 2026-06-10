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
import ci.nsu.moble.main.data.local.DepositCalculation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val fmt = DecimalFormat("#,##0.00")

@Composable
fun DetailScreen(
    nav: NavController,
    vm: DepositViewModel,
    id: Long
) {
    val calculations by vm.calculations.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadCalculations()
    }

    val calc: DepositCalculation? = calculations.firstOrNull { it.id == id }

    if (calc == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("Расчёт не найден")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { nav.navigateUp() }) {
                Text("Назад")
            }
        }
        return
    }

    val dateFmt = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Детали расчёта",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultRow("Дата", dateFmt.format(Date(calc.calculationDate)))
                ResultRow("Стартовый взнос", "${fmt.format(calc.initialAmount)} ₽")
                ResultRow("Срок вклада", "${calc.periodMonths} мес.")
                ResultRow("Процентная ставка", "${calc.interestRate}%")

                if ((calc.monthlyTopUp ?: 0.0) > 0) {
                    ResultRow(
                        "Ежемес. пополнение",
                        "${fmt.format(calc.monthlyTopUp)} ₽"
                    )
                }

                HorizontalDivider()

                ResultRow(
                    label = "Итоговая сумма",
                    value = "${fmt.format(calc.finalAmount)} ₽",
                    bold = true
                )

                ResultRow(
                    label = "Начисленные проценты",
                    value = "${fmt.format(calc.interestEarned)} ₽",
                    bold = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                vm.deleteCalculation(id)
                nav.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Удалить")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { nav.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
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