package ci.nsu.moble.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val fmt = DecimalFormat("#,##0.00")

@Composable
fun HistoryScreenWrapper(
    modifier: Modifier = Modifier,
    vm: DepositViewModel,
    onOpenDetail: (Long) -> Unit
) {
    val list by vm.calculations.collectAsState()

    val dateFmt = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    }

    LaunchedEffect(Unit) {
        vm.loadCalculations()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Мои расчёты",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenDetail(item.id)
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(dateFmt.format(Date(item.calculationDate)))
                            Text("Взнос: ${fmt.format(item.initialAmount)} ₽")
                            Text(
                                text = "Итог: ${fmt.format(item.finalAmount)} ₽",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}